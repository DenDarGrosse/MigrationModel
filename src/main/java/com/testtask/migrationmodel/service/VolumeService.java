package com.testtask.migrationmodel.service;

import com.testtask.migrationmodel.entity.Volume;
import com.testtask.migrationmodel.repository.VolumeRepository;
import com.testtask.migrationmodel.util.IdUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VolumeService {
    private final VolumeRepository volumeRepository;
    private final IdUtil idUtil;

    public Volume add(Volume volume){
        var lastId = idUtil.getNextId(volumeRepository);

        var _volume = new Volume(lastId, volume.getMountPoint(), volume.getSize());
        volumeRepository.save(_volume);

        return _volume;
    }

    public Volume modify(Long id, Volume volume){
        var _volume = validate(id);
        _volume.setMountPoint(volume.getMountPoint());
        _volume.setSize(volume.getSize());
        volumeRepository.save(_volume);

        return _volume;
    }

    public List<Volume> validate(List<Long> volumeIds) {
        var volumes = new LinkedList<Volume>();

        for (var id : volumeIds) {
            var volume = validate(id);
            volumes.add(volume);
        }

        return volumes;
    }

    public Volume validate(Long id) {
        var volumeData = volumeRepository.findById(id);

        if (volumeData.isEmpty()) {
            throw new NoSuchElementException("Can not find Volume with id " + id);
        }

        return volumeData.get();
    }
}
