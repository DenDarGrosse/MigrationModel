package com.testtask.migrationmodel.service;

import com.testtask.migrationmodel.entity.Volume;
import com.testtask.migrationmodel.entity.Workload;
import com.testtask.migrationmodel.entity.Migration;
import com.testtask.migrationmodel.entity.MigrationState;
import com.testtask.migrationmodel.repository.MigrationRepository;
import com.testtask.migrationmodel.repository.TargetCloudRepository;
import com.testtask.migrationmodel.repository.WorkloadRepository;
import com.testtask.migrationmodel.util.IdUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MigrationService {
    private final VolumeService volumeService;
    private final WorkloadService workloadService;
    private final TargetCloudService targetCloudService;
    private final MigrationRepository migrationRepository;
    private final WorkloadRepository workloadRepository;
    private final IdUtil idUtil;

    public Migration add(Migration migration){
        var lastId = idUtil.getNextId(migrationRepository);

        workloadService.validate(migration.getSourceId());
        targetCloudService.validate(migration.getTargetCloudId());

        var _migration = new Migration(
                lastId,
                migration.getMountPoints(),
                migration.getSourceId(),
                migration.getTargetCloudId(),
                MigrationState.notStarted
        );
        migrationRepository.save(_migration);

        return _migration;
    }

    public Migration modify(Long id, Migration migration){
        var _migration = validate(id);
        _migration.setMigrationState(migration.getMigrationState());
        _migration.setSourceId(migration.getSourceId());
        _migration.setMountPoints(migration.getMountPoints());
        _migration.setTargetCloudId(migration.getTargetCloudId());
        migrationRepository.save(_migration);

        return _migration;
    }

    public Migration validate(Long migrationId){
        var migrationData = migrationRepository.findById(migrationId);

        if(migrationData.isEmpty()){
            throw new NoSuchElementException("Can not find Migration with id " + migrationId);
        }

        return migrationData.get();
    }

    public void setMigrationState(Migration migration, MigrationState migrationState) {
        migration.setMigrationState(migrationState);
        migrationRepository.save(migration);
    }

    //TODO: make async
    @SneakyThrows
    public void run(Migration migration) {
        setMigrationState(migration, MigrationState.running);

        //Thread.sleep(5*60*1000);

        var source = workloadService.validate(migration.getSourceId());
        var target = targetCloudService.validate(migration.getTargetCloudId());
        var targetWorkload = workloadService.validate(target.getTargetId());
        var selectedVolumes = migration.getMountPoints();
        var sourceVolumes = volumeService.validate(source.getVolumeIds());
        var volumesToCopy = sourceVolumes.stream()
                .filter(p -> selectedVolumes.contains(p.getMountPoint()))
                .map(Volume::getId)
                .collect(Collectors.toList());

        var newWorkload = new Workload(target.getTargetId(), targetWorkload.getIp(), targetWorkload.getCredentialsId(), volumesToCopy);
        workloadRepository.save(newWorkload);

        setMigrationState(migration, MigrationState.success);
    }
}
