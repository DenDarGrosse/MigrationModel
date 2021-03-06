package com.testtask.migrationmodel.controller;

import com.testtask.migrationmodel.entity.Migration;
import com.testtask.migrationmodel.entity.MigrationState;
import com.testtask.migrationmodel.repository.MigrationRepository;
import com.testtask.migrationmodel.service.MigrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/migration")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MigrationController {
    private final MigrationRepository migrationRepository;
    private final MigrationService migrationService;

    @PostMapping
    public ResponseEntity<Migration> add(@RequestBody Migration migration) {
        var _migration = migrationService.add(migration);
        return ResponseEntity.ok(_migration);
    }

    @DeleteMapping("/{id}")
    public void remove(@PathVariable Long id) {
        migrationRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Migration> modify(@PathVariable Long id, @RequestBody Migration migration) {
        var _migration = migrationService.modify(id, migration);
        return ResponseEntity.ok(_migration);
    }

    @PutMapping("/run/{id}")
    public ResponseEntity<String> run(@PathVariable Long id) {
        var migration = migrationService.validate(id);
        migrationService.run(migration);
        return ResponseEntity.ok("Migration started");
    }

    @GetMapping("status/{id}")
    public MigrationState status(@PathVariable Long id) {
        var migration = migrationService.validate(id);
        return migration.getMigrationState();
    }
}
