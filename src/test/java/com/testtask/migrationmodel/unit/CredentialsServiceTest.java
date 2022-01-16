package com.testtask.migrationmodel.unit;

import com.testtask.migrationmodel.entity.Credentials;
import com.testtask.migrationmodel.repository.CredentialsRepository;
import com.testtask.migrationmodel.service.CredentialsService;
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
public class CredentialsServiceTest {
    @Autowired
    private CredentialsService credentialsService;
    @MockBean
    private CredentialsRepository credentialsRepository;

    @Captor
    ArgumentCaptor<Credentials> credentialsArgumentCaptor;

    private void assertCredentials(Credentials origin, Credentials compare) {
        Assert.assertEquals(origin.getId(), compare.getId());
        Assert.assertEquals(origin.getUsername(), compare.getUsername());
        Assert.assertEquals(origin.getPassword(), compare.getPassword());
        Assert.assertEquals(origin.getDomain(), compare.getDomain());
    }

    @Test
    public void validateFoundTest() {
        var credentials = new Credentials(0L, "name", "password", "domain");
        var credentialsToReturn = Optional.ofNullable(credentials);

        Mockito.doReturn(credentialsToReturn)
                .when(credentialsRepository)
                .findById(0L);

        var _credentials = credentialsService.validate(0L);
        assertCredentials(credentials, _credentials);
    }

    @Test
    public void validateNotFoundTest() {
        Mockito.doReturn(Optional.empty())
                .when(credentialsRepository)
                .findById(0L);

        try {
            var _credentials = credentialsService.validate(0L);
        } catch (Exception e) {
            Assert.assertEquals("Can not find Credentials with id 0", e.getMessage());
            return;
        }

        Assert.fail();
    }

    @Test
    public void addTest() {
        var credentials = new Credentials(null, "name", "pass", "domain");

        Mockito.doReturn(null).when(credentialsRepository).getLastId();

        var _credentials = credentialsService.add(credentials);

        credentials.setId(0L);

        Mockito.verify(credentialsRepository).save(credentialsArgumentCaptor.capture());
        var credentialsAnswer = credentialsArgumentCaptor.getValue();

        assertCredentials(credentials, _credentials);
        assertCredentials(credentials, credentialsAnswer);
    }

    @Test
    public void modifyTest() {
        var credentials = new Credentials(0L, "name", "pass", "domain");
        var credentialsModify = new Credentials(0L, "name2", "pass2", "domain2");

        Mockito.doReturn(Optional.of(credentials)).when(credentialsRepository).findById(0L);

        var _credentials = credentialsService.modify(0L, credentialsModify);

        Mockito.verify(credentialsRepository).save(credentialsArgumentCaptor.capture());
        var credentialsAnswer = credentialsArgumentCaptor.getValue();

        assertCredentials(credentials, _credentials);
        assertCredentials(credentials, credentialsAnswer);
    }
}
