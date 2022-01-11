package com.testtask.migrationmodel.controller;

import com.testtask.migrationmodel.entity.TargetCloud;
import com.testtask.migrationmodel.repository.TargetCloudRepository;
import com.testtask.migrationmodel.service.WorkloadService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/target")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TargetController {
    private final TargetCloudRepository targetCloudRepository;
    private final WorkloadService workloadService;

    @PostMapping
    public void add(@RequestBody TargetCloud targetCloud) {
        var lastId = targetCloudRepository.getLastId();

        if (lastId == null) {
            lastId = -1L;
        }

        var workload = workloadService.validate(targetCloud.getTargetId());
        if (workload == null) {
            return; //TODO: need to create error return
        }

        var _targetCloud = new TargetCloud(
                lastId+1,
                targetCloud.getCloudType(),
                targetCloud.getCloudCredentialsId(),
                workload.getId()
        );
        targetCloudRepository.save(_targetCloud);
    }

    @DeleteMapping("/{id}")
    public void remove(@PathVariable Long id) {
        targetCloudRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    public void modify(@PathVariable Long id, @RequestBody TargetCloud targetCloud) {
        var targetData = targetCloudRepository.findById(id);

        if (targetData.isPresent()) {
            var workload = workloadService.validate(targetCloud.getTargetId());
            if(workload == null){
                return;
            }

            var _targetCloud = targetData.get();
            _targetCloud.setTargetId(workload.getId());
            _targetCloud.setCloudType(targetCloud.getCloudType());
            _targetCloud.setCloudCredentialsId(targetCloud.getCloudCredentialsId());
            targetCloudRepository.save(_targetCloud);
        }
    }
}
