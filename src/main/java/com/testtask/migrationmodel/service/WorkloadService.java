package com.testtask.migrationmodel.service;

import com.testtask.migrationmodel.entity.Workload;
import com.testtask.migrationmodel.repository.WorkloadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WorkloadService {
    private final WorkloadRepository workloadRepository;

    public Workload validate(Long workloadId) {
        //TODO: refactor this function
        if(!workloadRepository.existsById(workloadId)){
            return null;
        }

        var _workload = workloadRepository.findById(workloadId).get();
        return _workload;
    }
}
