package com.testtask.migrationmodel.service;

import com.testtask.migrationmodel.entity.Volume;
import com.testtask.migrationmodel.entity.Workload;
import com.testtask.migrationmodel.entity.Migration;
import com.testtask.migrationmodel.entity.MigrationState;
import com.testtask.migrationmodel.repository.MigrationRepository;
import com.testtask.migrationmodel.repository.TargetCloudRepository;
import com.testtask.migrationmodel.repository.WorkloadRepository;
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

    @SneakyThrows
    public void run(Migration migration) {
        setMigrationState(migration, MigrationState.running);

        Thread.sleep(5*60*1000);

        var source = workloadService.validate(migration.getSourceId());
        var target = targetCloudService.validate(migration.getTargetCloudId());
        var selectedVolumes = migration.getMountPoints();
        var sourceVolumes = volumeService.validate(source.getVolumeIds());
        var volumesToCopy = sourceVolumes.stream()
                .filter(p -> selectedVolumes.contains(p.getMountPoint()))
                .map(Volume::getId)
                .collect(Collectors.toList());

        var newWorkload = new Workload(source.getId(), source.getIp(), source.getCredentialsId(), volumesToCopy);
        workloadRepository.save(newWorkload);

        setMigrationState(migration, MigrationState.success);
    }
}
