package com.testtask.migrationmodel.controller;

import com.testtask.migrationmodel.entity.Migration;
import com.testtask.migrationmodel.entity.MigrationState;
import com.testtask.migrationmodel.repository.MigrationRepository;
import com.testtask.migrationmodel.service.MigrationService;
import com.testtask.migrationmodel.service.TargetCloudService;
import com.testtask.migrationmodel.service.WorkloadService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Migration> add(@RequestBody Migration migration) {
        var lastId = migrationRepository.getLastId();

        if (lastId == null) {
            lastId = -1L;
        }

        workloadService.validate(migration.getSourceId());
        targetCloudService.validate(migration.getTargetCloudId());

        var _migration = new Migration(
                lastId + 1,
                migration.getMountPoints(),
                migration.getSourceId(),
                migration.getTargetCloudId(),
                MigrationState.notStarted
        );
        migrationRepository.save(_migration);

        return ResponseEntity.ok(_migration);
    }

    @DeleteMapping("/{id}")
    public void remove(@PathVariable Long id) {
        migrationRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Migration> modify(@PathVariable Long id, @RequestBody Migration migration) {
        var _migration = migrationService.validate(id);
        _migration.setMigrationState(migration.getMigrationState());
        _migration.setSourceId(migration.getSourceId());
        _migration.setMountPoints(migration.getMountPoints());
        _migration.setTargetCloudId(migration.getTargetCloudId());
        migrationRepository.save(_migration);

        return ResponseEntity.ok(_migration);
    }

    @PutMapping("/run/{id}")
    public void run(@PathVariable Long id) {
        var migration = migrationService.validate(id);
        migrationService.run(migration);
    }

    @GetMapping("status/{id}")
    public MigrationState status(@PathVariable Long id) {
        var migration = migrationService.validate(id);
        return migration.getMigrationState();
    }
}
