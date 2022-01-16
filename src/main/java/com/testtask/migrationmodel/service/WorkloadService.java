package com.testtask.migrationmodel.service;

import com.testtask.migrationmodel.entity.Workload;
import com.testtask.migrationmodel.repository.WorkloadRepository;
import com.testtask.migrationmodel.util.IdUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WorkloadService {
    private final WorkloadRepository workloadRepository;
    private final VolumeService volumeService;
    private final CredentialsService credentialsService;
    private final IdUtil idUtil;

    public Workload add(Workload workload){
        var lastId = idUtil.getNextId(workloadRepository);

        volumeService.validate(workload.getVolumeIds());
        credentialsService.validate(workload.getCredentialsId());

        var _workload = new Workload(
                lastId,
                workload.getIp(),
                workload.getCredentialsId(),
                workload.getVolumeIds());
        workloadRepository.save(_workload);

        return _workload;
    }

    public Workload modify(Long id, Workload workload){
        //TODO: need to not let change ip of source
        var _workload = validate(id);
        _workload.setCredentialsId(workload.getCredentialsId());
        _workload.setVolumeIds(workload.getVolumeIds());
        workloadRepository.save(_workload);

        return _workload;
    }

    public Workload validate(Long workloadId) {
        var workloadData = workloadRepository.findById(workloadId);

        if (workloadData.isEmpty()) {
            throw new NoSuchElementException("Can not find Workload with id " + workloadId);
        }

        return workloadData.get();
    }
}
