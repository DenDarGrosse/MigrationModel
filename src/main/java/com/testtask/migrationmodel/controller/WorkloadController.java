package com.testtask.migrationmodel.controller;

import com.testtask.migrationmodel.entity.Workload;
import com.testtask.migrationmodel.repository.WorkloadRepository;
import com.testtask.migrationmodel.service.CredentialsService;
import com.testtask.migrationmodel.service.VolumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/workload")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WorkloadController {
    private final WorkloadRepository workloadRepository;
    private final VolumeService volumeService;
    private final CredentialsService credentialsService;

    @PostMapping
    public void add(@RequestBody Workload workload) {
        var lastId = workloadRepository.getLastId();

        if (lastId == null) {
            lastId = -1L;
        }

        var volumes = volumeService.validate(workload.getVolumeIds());
        if (volumes == null) {
            return;
        }

        var credentials = credentialsService.validate(workload.getCredentialsId());
        if (credentials == null) {
            return;
        }

        var _workload = workloadRepository.save(
                new Workload(
                        lastId + 1,
                        workload.getIp(),
                        workload.getCredentialsId(),
                        workload.getVolumeIds()));
    }

    @DeleteMapping("/{id}")
    public void remove(@PathVariable Long id) {
        workloadRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    public void modify(@PathVariable Long id, @RequestBody Workload workload) {
        //TODO: need to not let change ip of source
        var workloadData = workloadRepository.findById(id);

        if (workloadData.isPresent()) {
            var volumes = volumeService.validate(workload.getVolumeIds());
            if (volumes == null) {
                return;
            }

            var _workload = workloadData.get();
            _workload.setCredentialsId(workload.getCredentialsId());
            _workload.setVolumeIds(workload.getVolumeIds());
            workloadRepository.save(_workload);
        }
    }
}
