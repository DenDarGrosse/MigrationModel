package com.testtask.migrationmodel.unit;

import com.testtask.migrationmodel.entity.*;
import com.testtask.migrationmodel.repository.CredentialsRepository;
import com.testtask.migrationmodel.repository.TargetCloudRepository;
import com.testtask.migrationmodel.repository.WorkloadRepository;
import com.testtask.migrationmodel.service.TargetCloudService;
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
public class TargetCloudServiceTest {
    @Autowired
    private TargetCloudService targetCloudService;
    @MockBean
    private TargetCloudRepository targetCloudRepository;
    @MockBean
    private WorkloadRepository workloadRepository;
    @MockBean
    private CredentialsRepository credentialsRepository;

    @Captor
    private ArgumentCaptor<TargetCloud> targetCloudArgumentCaptor;

    private void assertTargetCloud(TargetCloud origin, TargetCloud compare) {
        Assert.assertEquals(origin.getId(), compare.getId());
        Assert.assertEquals(origin.getTargetId(), compare.getTargetId());
        Assert.assertEquals(origin.getCloudType(), compare.getCloudType());
        Assert.assertEquals(origin.getCloudCredentialsId(), compare.getCloudCredentialsId());
    }

    @Test
    public void validateFound() {
        var targetCloud = new TargetCloud(0L, CloudType.aws, 1L, 2L);
        var targetCloudToReturn = Optional.of(targetCloud);

        Mockito.doReturn(targetCloudToReturn)
                .when(targetCloudRepository)
                .findById(0L);

        var _targetCloud = targetCloudService.validate(0L);
        assertTargetCloud(targetCloud, _targetCloud);
    }

    @Test
    public void validateNotFound() {
        Mockito.doReturn(Optional.empty())
                .when(targetCloudRepository)
                .findById(0L);

        try {
            var _migration = targetCloudService.validate(0L);
        } catch (Exception e) {
            Assert.assertEquals("Can not find TargetCloud with id 0", e.getMessage());
            return;
        }

        Assert.fail();
    }

    @Test
    public void addTest() {
        var targetCloud = new TargetCloud(null, CloudType.aws, 1L, 2L);
        var workload = new Workload(2L, "1.2.3", 1L, List.of(4L));
        var credentials = new Credentials(1L, "name", "pass", "domain");

        Mockito.doReturn(Optional.of(targetCloud)).when(targetCloudRepository).findById(0L);
        Mockito.doReturn(Optional.of(workload)).when(workloadRepository).findById(2L);
        Mockito.doReturn(Optional.of(credentials)).when(credentialsRepository).findById(1L);

        Mockito.doReturn(null).when(targetCloudRepository).getLastId();

        var _targetCloud = targetCloudService.add(targetCloud);

        targetCloud.setId(0L);

        Mockito.verify(targetCloudRepository).save(targetCloudArgumentCaptor.capture());
        var targetCloudAnswer = targetCloudArgumentCaptor.getValue();

        assertTargetCloud(targetCloud, _targetCloud);
        assertTargetCloud(targetCloud, targetCloudAnswer);
    }

    @Test
    public void modifyTest() {
        var targetCloud = new TargetCloud(0L, CloudType.aws, 1L, 2L);
        var targetCloudModify = new TargetCloud(0L, CloudType.azure, 2L, 3L);

        Mockito.doReturn(Optional.of(targetCloud)).when(targetCloudRepository).findById(0L);

        var _targetCloud = targetCloudService.modify(0L, targetCloudModify);

        Mockito.verify(targetCloudRepository).save(targetCloudArgumentCaptor.capture());
        var targetCloudAnswer = targetCloudArgumentCaptor.getValue();

        assertTargetCloud(targetCloud, _targetCloud);
        assertTargetCloud(targetCloud, targetCloudAnswer);
    }
}
