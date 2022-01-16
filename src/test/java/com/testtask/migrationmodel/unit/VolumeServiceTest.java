package com.testtask.migrationmodel.unit;

import com.testtask.migrationmodel.entity.Volume;
import com.testtask.migrationmodel.repository.VolumeRepository;
import com.testtask.migrationmodel.service.VolumeService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;


@SpringBootTest
public class VolumeServiceTest {
    @Autowired
    private VolumeService volumeService;
    @MockBean
    private VolumeRepository volumeRepository;

    @Test
    public void validateFound() {
        var volume = new Volume(0L, "C:/", 1);
        var volumeToReturn = Optional.of(volume);

        Mockito.doReturn(volumeToReturn)
                .when(volumeRepository)
                .findById(0L);

        var _volume = volumeService.validate(0L);
        Assert.assertEquals(volume.getId(), _volume.getId());
        Assert.assertEquals(volume.getMountPoint(), _volume.getMountPoint());
        Assert.assertEquals(volume.getSize(), _volume.getSize());
    }

    @Test
    public void validateNotFound() {
        Mockito.doReturn(Optional.empty())
                .when(volumeRepository)
                .findById(0L);

        try{
            var _volume = volumeService.validate(0L);
        }
        catch (Exception e){
            Assert.assertEquals("Can not find Volume with id 0", e.getMessage());
            return;
        }

        Assert.fail();
    }
}
