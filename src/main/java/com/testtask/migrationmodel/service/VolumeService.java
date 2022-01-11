package com.testtask.migrationmodel.service;

import com.testtask.migrationmodel.entity.Volume;
import com.testtask.migrationmodel.repository.VolumeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VolumeService {
    private final VolumeRepository volumeRepository;

    public List<Volume> validate(List<Long> volumeIds){
        var volumes = new LinkedList<Volume>();

        for (var id : volumeIds) {
            var volumeOptional = volumeRepository.findById(id);

            if (volumeOptional.isEmpty()) {
                return null; //TODO: need to create error return
            }

            volumes.add(volumeOptional.get());
        }

        return volumes;
    }
}
