package com.testtask.migrationmodel.unit;

import com.testtask.migrationmodel.entity.Credentials;
import com.testtask.migrationmodel.repository.CredentialsRepository;
import com.testtask.migrationmodel.service.CredentialsService;
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
public class CredentialsServiceTest {
    @Autowired
    private CredentialsService credentialsService;
    @MockBean
    private CredentialsRepository credentialsRepository;

    @Test
    public void validateFoundTest(){
        var credentials = new Credentials(0L,"name","password", "domain");
        var credentialsToReturn = Optional.ofNullable(credentials);

        Mockito.doReturn(credentialsToReturn)
                .when(credentialsRepository)
                .findById(0L);

        var _credentials = credentialsService.validate(0L);
        Assert.assertEquals(credentials.getId(), _credentials.getId());
        Assert.assertEquals(credentials.getUsername(), _credentials.getUsername());
        Assert.assertEquals(credentials.getPassword(), _credentials.getPassword());
        Assert.assertEquals(credentials.getDomain(), _credentials.getDomain());
    }

    @Test
    public void validateNotFoundTest(){
        Mockito.doReturn(Optional.empty())
                .when(credentialsRepository)
                .findById(0L);

        try {
            var _credentials = credentialsService.validate(0L);
        }
        catch (Exception e){
            Assert.assertEquals("Can not find Credentials with id 0", e.getMessage());
            return;
        }

        Assert.fail();
    }
}
