package com.testtask.migrationmodel.service;

import com.testtask.migrationmodel.entity.Volume;
import com.testtask.migrationmodel.entity.Workload;
import com.testtask.migrationmodel.entity.Migration;
import com.testtask.migrationmodel.entity.MigrationState;
import com.testtask.migrationmodel.repository.MigrationRepository;
import com.testtask.migrationmodel.repository.TargetCloudRepository;
import com.testtask.migrationmodel.repository.WorkloadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MigrationService {
    private final VolumeService volumeService;
    private final WorkloadService workloadService;
    private final TargetCloudService targetCloudService;
    private final MigrationRepository migrationRepository;
    private final WorkloadRepository workloadRepository;

    public void setMigrationState(Migration migration, MigrationState migrationState) {
        migration.setMigrationState(migrationState);
        migrationRepository.save(migration);
    }

    public void run(Migration migration) {
        //TODO: сделать паузу на 5 минут

        setMigrationState(migration, MigrationState.running);

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
