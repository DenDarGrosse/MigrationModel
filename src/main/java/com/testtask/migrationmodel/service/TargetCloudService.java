package com.testtask.migrationmodel.service;

import com.testtask.migrationmodel.entity.TargetCloud;
import com.testtask.migrationmodel.repository.TargetCloudRepository;
import com.testtask.migrationmodel.util.IdUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TargetCloudService {
    private final TargetCloudRepository targetCloudRepository;
    private final CredentialsService credentialsService;
    private final WorkloadService workloadService;
    private final IdUtil idUtil;

    public TargetCloud add(TargetCloud targetCloud){
        var lastId = idUtil.getNextId(targetCloudRepository);

        workloadService.validate(targetCloud.getTargetId());
        credentialsService.validate(targetCloud.getCloudCredentialsId());

        var _targetCloud = new TargetCloud(
                lastId,
                targetCloud.getCloudType(),
                targetCloud.getCloudCredentialsId(),
                targetCloud.getTargetId()
        );
        targetCloudRepository.save(_targetCloud);

        return _targetCloud;
    }

    public TargetCloud modify(Long id, TargetCloud targetCloud){
        var _targetCloud = validate(id);
        _targetCloud.setTargetId(targetCloud.getTargetId());
        _targetCloud.setCloudType(targetCloud.getCloudType());
        _targetCloud.setCloudCredentialsId(targetCloud.getCloudCredentialsId());
        targetCloudRepository.save(_targetCloud);

        return _targetCloud;
    }

    public TargetCloud validate(Long targetCloudId) {
        var targetCloudData = targetCloudRepository.findById(targetCloudId);

        if (targetCloudData.isEmpty()) {
            throw new NoSuchElementException("Can not find TargetCloud with id " + targetCloudId);
        }

        return targetCloudData.get();
    }
}
