package com.testtask.migrationmodel.controller;

import com.testtask.migrationmodel.entity.Volume;
import com.testtask.migrationmodel.repository.VolumeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/volume")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VolumeController {
    private final VolumeRepository volumeRepository;

    @PostMapping
    public void save(@RequestBody Volume volume) {
        var lastId = volumeRepository.getLastId();

        if (lastId == null) {
            lastId = -1L;
        }

        var _volume = volumeRepository.save(new Volume(lastId + 1, volume.getMountPoint(), volume.getSize()));
    }

    @PutMapping("/{id}")
    public void modify(@PathVariable("id") Long id, @RequestBody Volume volume) {
        var volumeData = volumeRepository.findById(id);

        if (volumeData.isPresent()) {
            var _volume = volumeData.get();
            _volume.setMountPoint(volume.getMountPoint());
            _volume.setSize(volume.getSize());
            volumeRepository.save(_volume);
        }
    }

    @DeleteMapping("/{id}")
    public void remove(@PathVariable("id") Long id) {
        volumeRepository.deleteById(id);
    }
}
