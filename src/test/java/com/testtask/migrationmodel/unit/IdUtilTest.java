package com.testtask.migrationmodel.unit;

import com.testtask.migrationmodel.repository.CredentialsRepository;
import com.testtask.migrationmodel.service.CredentialsService;
import com.testtask.migrationmodel.util.IdUtil;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
public class IdUtilTest {
    @Autowired
    private IdUtil idUtil;
    @MockBean
    private CredentialsRepository credentialsRepository;

    @Test
    public void getFirstId(){
        Mockito.doReturn(null)
                .when(credentialsRepository)
                .getLastId();

        var id = idUtil.getNextId(credentialsRepository);
        Assert.assertEquals(new Long(0), id);
    }

    @Test
    public void getNotFirstId() {
        Mockito.doReturn(0L)
                .when(credentialsRepository)
                .getLastId();

        var id = idUtil.getNextId(credentialsRepository);
        Assert.assertEquals(new Long(1), id);
    }
}
