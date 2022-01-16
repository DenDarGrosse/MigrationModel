package com.testtask.migrationmodel.controller;

import com.testtask.migrationmodel.entity.Credentials;
import com.testtask.migrationmodel.repository.CredentialsRepository;
import com.testtask.migrationmodel.service.CredentialsService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.testtask.migrationmodel.controller.JsonUtil.asJsonString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CredentialsController.class)
public class CredentialsControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private CredentialsService credentialsService;
    @MockBean
    private CredentialsRepository credentialsRepository;

    @Test
    public void addTest() throws Exception {
        var credentialsNull = new Credentials(null, "name", "pass", "domain");
        var credentials = new Credentials(0L, "name", "pass", "domain");
        var credentialsJson = asJsonString(credentials);

        Mockito.doReturn(credentials).when(credentialsService).add(credentialsNull);

        mvc.perform(post("/api/credentials")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(credentialsNull)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(credentialsJson));
    }

    @Test
    public void modifyTest() throws Exception {
        var credentials = new Credentials(0L, "name", "pass", "domain");
        var credentialsJson = asJsonString(credentials);

        Mockito.doReturn(credentials).when(credentialsService).modify(0L, credentials);

        mvc.perform(put("/api/credentials/0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(credentials)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(credentialsJson));
    }

    @Test
    public void removeTest() throws Exception {
        mvc.perform(delete("/api/credentials/0"))
                .andExpect(status().isOk());
    }
}
