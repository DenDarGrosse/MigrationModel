package com.testtask.migrationmodel.controller;

import com.testtask.migrationmodel.entity.Volume;
import com.testtask.migrationmodel.repository.VolumeRepository;
import com.testtask.migrationmodel.service.VolumeService;
import com.testtask.migrationmodel.util.IdUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/volume")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VolumeController {
    private final VolumeRepository volumeRepository;
    private final VolumeService volumeService;
    private final IdUtil idUtil;

    @PostMapping
    public ResponseEntity<Volume> save(@RequestBody Volume volume) {
        var lastId = idUtil.getNextId(volumeRepository);

        var _volume = new Volume(lastId, volume.getMountPoint(), volume.getSize());
        volumeRepository.save(_volume);

        return ResponseEntity.ok(_volume);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Volume> modify(@PathVariable("id") Long id, @RequestBody Volume volume) {
            var _volume = volumeService.validate(id);
            _volume.setMountPoint(volume.getMountPoint());
            _volume.setSize(volume.getSize());
            volumeRepository.save(_volume);

            return ResponseEntity.ok(_volume);
    }

    @DeleteMapping("/{id}")
    public void remove(@PathVariable("id") Long id) {
        volumeRepository.deleteById(id);
    }
}
