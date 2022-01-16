package com.testtask.migrationmodel.unit;

import com.testtask.migrationmodel.entity.Workload;
import com.testtask.migrationmodel.repository.WorkloadRepository;
import com.testtask.migrationmodel.service.WorkloadService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class WorkloadServiceTest {
    @Autowired
    private WorkloadService workloadService;
    @MockBean
    private WorkloadRepository workloadRepository;

    @Test
    public void validateFound(){
        var workload = new Workload(0L,"1.2.3", 1L, List.of(2L));
        var workloadToReturn = Optional.of(workload);

        Mockito.doReturn(workloadToReturn)
                .when(workloadRepository)
                .findById(0L);

        var _workload = workloadService.validate(0L);
        Assert.assertEquals(workload.getId(), _workload.getId());
        Assert.assertEquals(workload.getCredentialsId(), _workload.getCredentialsId());
        Assert.assertEquals(workload.getVolumeIds(), _workload.getVolumeIds());
        Assert.assertEquals(workload.getIp(), _workload.getIp());
    }

    @Test
    public void validateNotFound(){
        Mockito.doReturn(Optional.empty())
                .when(workloadRepository)
                .findById(0L);

        try{
            var _workload = workloadService.validate(0L);
        }
        catch (Exception e){
            Assert.assertEquals("Can not find Workload with id 0", e.getMessage());
            return;
        }

        Assert.fail();
    }
}
