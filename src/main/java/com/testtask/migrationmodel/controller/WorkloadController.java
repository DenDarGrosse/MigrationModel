package com.testtask.migrationmodel.controller;

import com.testtask.migrationmodel.entity.Workload;
import com.testtask.migrationmodel.repository.WorkloadRepository;
import com.testtask.migrationmodel.service.CredentialsService;
import com.testtask.migrationmodel.service.VolumeService;
import com.testtask.migrationmodel.service.WorkloadService;
import com.testtask.migrationmodel.util.IdUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/workload")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WorkloadController {
    private final WorkloadRepository workloadRepository;
    private final VolumeService volumeService;
    private final CredentialsService credentialsService;
    private final WorkloadService workloadService;
    private final IdUtil idUtil;

    @PostMapping
    public ResponseEntity<Workload> add(@RequestBody Workload workload) {
        var lastId = idUtil.getNextId(workloadRepository);

        volumeService.validate(workload.getVolumeIds());
        credentialsService.validate(workload.getCredentialsId());

        var _workload = new Workload(
                lastId + 1,
                workload.getIp(),
                workload.getCredentialsId(),
                workload.getVolumeIds());
        workloadRepository.save(_workload);

        return ResponseEntity.ok(_workload);
    }

    @DeleteMapping("/{id}")
    public void remove(@PathVariable Long id) {
        workloadRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Workload> modify(@PathVariable Long id, @RequestBody Workload workload) {
        //TODO: need to not let change ip of source
        var _workload = workloadService.validate(id);
        _workload.setCredentialsId(workload.getCredentialsId());
        _workload.setVolumeIds(workload.getVolumeIds());
        workloadRepository.save(_workload);

        return ResponseEntity.ok(_workload);
    }
}
