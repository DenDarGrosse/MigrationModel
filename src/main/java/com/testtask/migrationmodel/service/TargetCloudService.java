package com.testtask.migrationmodel.service;

import com.testtask.migrationmodel.entity.TargetCloud;
import com.testtask.migrationmodel.repository.TargetCloudRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TargetCloudService {
    private final TargetCloudRepository targetCloudRepository;

    public TargetCloud validate(Long targetCloudId){
        var targetCloudData = targetCloudRepository.findById(targetCloudId);

        if(targetCloudData.isEmpty()){
            return null;
        }

        return targetCloudData.get();
    }
}
