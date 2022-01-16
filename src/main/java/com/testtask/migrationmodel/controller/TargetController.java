package com.testtask.migrationmodel.controller;

import com.testtask.migrationmodel.entity.TargetCloud;
import com.testtask.migrationmodel.repository.TargetCloudRepository;
import com.testtask.migrationmodel.service.TargetCloudService;
import com.testtask.migrationmodel.service.WorkloadService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/target")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TargetController {
    private final TargetCloudRepository targetCloudRepository;
    private final WorkloadService workloadService;
    private final TargetCloudService targetCloudService;

    @PostMapping
    public ResponseEntity<TargetCloud> add(@RequestBody TargetCloud targetCloud) {
        var lastId = targetCloudRepository.getLastId();

        if (lastId == null) {
            lastId = -1L;
        }

        workloadService.validate(targetCloud.getTargetId());

        var _targetCloud = new TargetCloud(
                lastId + 1,
                targetCloud.getCloudType(),
                targetCloud.getCloudCredentialsId(),
                targetCloud.getTargetId()
        );
        targetCloudRepository.save(_targetCloud);

        return ResponseEntity.ok(_targetCloud);
    }

    @DeleteMapping("/{id}")
    public void remove(@PathVariable Long id) {
        targetCloudRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TargetCloud> modify(@PathVariable Long id, @RequestBody TargetCloud targetCloud) {
        var _targetCloud = targetCloudService.validate(id);
        _targetCloud.setTargetId(targetCloud.getTargetId());
        _targetCloud.setCloudType(targetCloud.getCloudType());
        _targetCloud.setCloudCredentialsId(targetCloud.getCloudCredentialsId());
        targetCloudRepository.save(_targetCloud);

        return ResponseEntity.ok(_targetCloud);
    }
}
