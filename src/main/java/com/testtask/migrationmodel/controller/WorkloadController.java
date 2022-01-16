package com.testtask.migrationmodel.controller;

import com.testtask.migrationmodel.entity.Workload;
import com.testtask.migrationmodel.repository.WorkloadRepository;
import com.testtask.migrationmodel.service.WorkloadService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/workload")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WorkloadController {
    private final WorkloadRepository workloadRepository;
    private final WorkloadService workloadService;

    @PostMapping
    public ResponseEntity<Workload> add(@RequestBody Workload workload) {
        var _workload = workloadService.add(workload);
        return ResponseEntity.ok(_workload);
    }

    @DeleteMapping("/{id}")
    public void remove(@PathVariable Long id) {
        workloadRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Workload> modify(@PathVariable Long id, @RequestBody Workload workload) {
        var _workload = workloadService.modify(id,workload);
        return ResponseEntity.ok(_workload);
    }
}
