package com.testtask.migrationmodel.controller;

import com.testtask.migrationmodel.entity.Migration;
import com.testtask.migrationmodel.entity.MigrationState;
import com.testtask.migrationmodel.repository.MigrationRepository;
import com.testtask.migrationmodel.service.MigrationService;
import com.testtask.migrationmodel.service.TargetCloudService;
import com.testtask.migrationmodel.service.WorkloadService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/migration")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MigrationController {
    private final MigrationRepository migrationRepository;
    private final WorkloadService workloadService;
    private final TargetCloudService targetCloudService;
    private final MigrationService migrationService;

    @PostMapping
    public void add(@RequestBody Migration migration) {
        var lastId = migrationRepository.getLastId();

        if (lastId == null) {
            lastId = -1L;
        }


        var source = workloadService.validate(migration.getSourceId());
        if (source == null) {
            return; //TODO: need to create error return
        }

        var target = targetCloudService.validate(migration.getTargetCloudId());
        if (target == null) {
            return;
        }

        var _migration = new Migration(
                lastId + 1,
                migration.getMountPoints(),
                source.getId(),
                target.getId(),
                MigrationState.notStarted
        );
        migrationRepository.save(_migration);
    }

    @DeleteMapping("/{id}")
    public void remove(@PathVariable Long id) {
        migrationRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    public void modify(@PathVariable Long id, @RequestBody Migration migration) {
        var migrationData = migrationRepository.findById(id);

        if (migrationData.isEmpty()) {
            return;
        }

        var source = workloadService.validate(migration.getSourceId());
        if (source == null) {
            return; //TODO: need to create error return
        }

        var target = targetCloudService.validate(migration.getTargetCloudId());
        if (target == null) {
            return;
        }

        var _migration = migrationData.get();
        _migration.setMigrationState(migration.getMigrationState());
        _migration.setSourceId(source.getId());
        _migration.setMountPoints(migration.getMountPoints());
        _migration.setTargetCloudId(target.getId());
        migrationRepository.save(_migration);
    }

    @PutMapping("/run/{id}")
    public void run(@PathVariable Long id) {
        var migrationData = migrationRepository.findById(id);

        if (migrationData.isEmpty()) {
            return;
        }

        var migration = migrationData.get();
        migrationService.run(migration);
    }

    @GetMapping("status/{id}")
    public MigrationState status(@PathVariable Long id) {
        var migrationData = migrationRepository.findById(id);

        if (migrationData.isEmpty()) {
            return null;
        }

        var migration = migrationData.get();
        return migration.getMigrationState();
    }
}
