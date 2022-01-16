package com.testtask.migrationmodel.unit;

import com.testtask.migrationmodel.entity.CloudType;
import com.testtask.migrationmodel.entity.TargetCloud;
import com.testtask.migrationmodel.repository.TargetCloudRepository;
import com.testtask.migrationmodel.service.TargetCloudService;
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
public class TargetCloudServiceTest {
    @Autowired
    private TargetCloudService targetCloudService;
    @MockBean
    private TargetCloudRepository targetCloudRepository;

    @Test
    public void validateFound(){
        var targetCloud = new TargetCloud(0L, CloudType.aws,1L,2L);
        var targetCloudToReturn = Optional.of(targetCloud);

        Mockito.doReturn(targetCloudToReturn)
                .when(targetCloudRepository)
                .findById(0L);

        var _targetCloud = targetCloudService.validate(0L);
        Assert.assertEquals(targetCloud.getId(), _targetCloud.getId());
        Assert.assertEquals(targetCloud.getTargetId(), _targetCloud.getTargetId());
        Assert.assertEquals(targetCloud.getCloudType(), _targetCloud.getCloudType());
        Assert.assertEquals(targetCloud.getCloudCredentialsId(), _targetCloud.getCloudCredentialsId());
    }

    @Test
    public void validateNotFound(){
        Mockito.doReturn(Optional.empty())
                .when(targetCloudRepository)
                .findById(0L);

        try {
            var _migration = targetCloudService.validate(0L);
        }
        catch (Exception e){
            Assert.assertEquals("Can not find TargetCloud with id 0", e.getMessage());
            return;
        }

        Assert.fail();
    }
}
