package com.testtask.migrationmodel.service;

import com.testtask.migrationmodel.entity.Workload;
import com.testtask.migrationmodel.repository.WorkloadRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WorkloadService {
    private final WorkloadRepository workloadRepository;

    @SneakyThrows
    public Workload validate(Long workloadId) {
        var workloadData = workloadRepository.findById(workloadId);

        if (workloadData.isEmpty()) {
            throw new NoSuchElementException("Can not find Workload with id " + workloadId);
        }

        return workloadData.get();
    }
}
