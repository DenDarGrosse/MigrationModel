package com.testtask.migrationmodel.controller;

import com.testtask.migrationmodel.entity.TargetCloud;
import com.testtask.migrationmodel.repository.TargetCloudRepository;
import com.testtask.migrationmodel.service.TargetCloudService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/target")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TargetController {
    private final TargetCloudRepository targetCloudRepository;
    private final TargetCloudService targetCloudService;

    @PostMapping
    public ResponseEntity<TargetCloud> add(@RequestBody TargetCloud targetCloud) {
        var _targetCloud = targetCloudService.add(targetCloud);
        return ResponseEntity.ok(_targetCloud);
    }

    @DeleteMapping("/{id}")
    public void remove(@PathVariable Long id) {
        targetCloudRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TargetCloud> modify(@PathVariable Long id, @RequestBody TargetCloud targetCloud) {
        var _targetCloud = targetCloudService.modify(id, targetCloud);
        return ResponseEntity.ok(_targetCloud);
    }
}
