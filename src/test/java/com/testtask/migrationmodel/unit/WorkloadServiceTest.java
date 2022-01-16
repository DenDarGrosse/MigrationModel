package com.testtask.migrationmodel.unit;

import com.testtask.migrationmodel.entity.*;
import com.testtask.migrationmodel.repository.CredentialsRepository;
import com.testtask.migrationmodel.repository.VolumeRepository;
import com.testtask.migrationmodel.repository.WorkloadRepository;
import com.testtask.migrationmodel.service.WorkloadService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class WorkloadServiceTest {
    @Autowired
    private WorkloadService workloadService;
    @MockBean
    private WorkloadRepository workloadRepository;
    @MockBean
    private CredentialsRepository credentialsRepository;
    @MockBean
    private VolumeRepository volumeRepository;

    @Captor
    private ArgumentCaptor<Workload> workloadArgumentCaptor;

    private void assertWorkload(Workload original, Workload compare) {
        Assert.assertEquals(original.getId(), compare.getId());
        Assert.assertEquals(original.getCredentialsId(), compare.getCredentialsId());
        Assert.assertEquals(original.getVolumeIds(), compare.getVolumeIds());
        Assert.assertEquals(original.getIp(), compare.getIp());
    }

    @Test
    public void validateFound() {
        var workload = new Workload(0L, "1.2.3", 1L, List.of(2L));
        var workloadToReturn = Optional.of(workload);

        Mockito.doReturn(workloadToReturn)
                .when(workloadRepository)
                .findById(0L);

        var _workload = workloadService.validate(0L);
        assertWorkload(workload, _workload);
    }

    @Test
    public void validateNotFound() {
        Mockito.doReturn(Optional.empty())
                .when(workloadRepository)
                .findById(0L);

        try {
            var _workload = workloadService.validate(0L);
        } catch (Exception e) {
            Assert.assertEquals("Can not find Workload with id 0", e.getMessage());
            return;
        }

        Assert.fail();
    }

    @Test
    public void addTest() {
        var workload = new Workload(2L, "1.2.3", 1L, List.of(4L));
        var credentials = new Credentials(1L, "name", "pass", "domain");
        var volume = new Volume(4L, "C", 1);

        Mockito.doReturn(Optional.of(workload)).when(workloadRepository).findById(2L);
        Mockito.doReturn(Optional.of(credentials)).when(credentialsRepository).findById(1L);
        Mockito.doReturn(Optional.of(volume)).when(volumeRepository).findById(4L);

        Mockito.doReturn(null).when(workloadRepository).getLastId();

        var _workload = workloadService.add(workload);

        workload.setId(0L);

        Mockito.verify(workloadRepository).save(workloadArgumentCaptor.capture());
        var workloadAnswer = workloadArgumentCaptor.getValue();

        assertWorkload(workload, _workload);
        assertWorkload(workload, workloadAnswer);
    }

    @Test
    public void modifyTest() {
        var workload = new Workload(2L, "1.2.3", 1L, List.of(4L));
        var workloadModify = new Workload(2L, "3.2.1", 2L, List.of(1L));

        Mockito.doReturn(Optional.of(workload)).when(workloadRepository).findById(2L);

        var _workload = workloadService.modify(2L, workloadModify);

        Mockito.verify(workloadRepository).save(workloadArgumentCaptor.capture());
        var workloadAnswer = workloadArgumentCaptor.getValue();

        assertWorkload(workload, _workload);
        assertWorkload(workload, workloadAnswer);
    }
}
