package com.testtask.migrationmodel.unit;

import com.testtask.migrationmodel.entity.Volume;
import com.testtask.migrationmodel.repository.VolumeRepository;
import com.testtask.migrationmodel.service.VolumeService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;


@SpringBootTest
public class VolumeServiceTest {
    @Autowired
    private VolumeService volumeService;
    @MockBean
    private VolumeRepository volumeRepository;
    @Captor
    private ArgumentCaptor<Volume> volumeArgumentCaptor;

    private void assertVolume(Volume original, Volume compare) {
        Assert.assertEquals(original.getId(), compare.getId());
        Assert.assertEquals(original.getMountPoint(), compare.getMountPoint());
        Assert.assertEquals(original.getSize(), compare.getSize());
    }

    @Test
    public void validateFound() {
        var volume = new Volume(0L, "C:/", 1);
        var volumeToReturn = Optional.of(volume);

        Mockito.doReturn(volumeToReturn)
                .when(volumeRepository)
                .findById(0L);

        var _volume = volumeService.validate(0L);
        assertVolume(volume, _volume);
    }

    @Test
    public void validateNotFound() {
        Mockito.doReturn(Optional.empty())
                .when(volumeRepository)
                .findById(0L);

        try {
            var _volume = volumeService.validate(0L);
        } catch (Exception e) {
            Assert.assertEquals("Can not find Volume with id 0", e.getMessage());
            return;
        }

        Assert.fail();
    }

    @Test
    public void addTest() {
        var volume = new Volume(null, "C:/", 1);

        Mockito.doReturn(null).when(volumeRepository).getLastId();

        var _volume = volumeService.add(volume);

        volume.setId(0L);

        Mockito.verify(volumeRepository).save(volumeArgumentCaptor.capture());
        var volumeAnswer = volumeArgumentCaptor.getValue();

        assertVolume(volume, _volume);
        assertVolume(volume, volumeAnswer);
    }

    @Test
    public void modifyTest() {
        var volume = new Volume(0L, "C:/", 1);
        var volumeModify = new Volume(0L, "D:/", 2);

        Mockito.doReturn(Optional.of(volume)).when(volumeRepository).findById(0L);

        var _volume = volumeService.modify(0L, volumeModify);

        Mockito.verify(volumeRepository).save(volumeArgumentCaptor.capture());
        var volumeAnswer = volumeArgumentCaptor.getValue();

        assertVolume(volume, _volume);
        assertVolume(volume, volumeAnswer);
    }
}
