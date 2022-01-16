package com.testtask.migrationmodel.controller;

import com.testtask.migrationmodel.entity.CloudType;
import com.testtask.migrationmodel.entity.Migration;
import com.testtask.migrationmodel.entity.MigrationState;
import com.testtask.migrationmodel.entity.TargetCloud;
import com.testtask.migrationmodel.repository.MigrationRepository;
import com.testtask.migrationmodel.service.MigrationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static com.testtask.migrationmodel.controller.JsonUtil.asJsonString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MigrationController.class)
public class MigrationControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private MigrationService migrationService;
    @MockBean
    private MigrationRepository migrationRepository;

    @Test
    public void addTest() throws Exception {
        var migrationNull = new Migration(null, List.of("C"), 1L, 2L, MigrationState.notStarted);
        var migration = new Migration(0L, List.of("C"), 1L, 2L, MigrationState.notStarted);
        var migrationJson = asJsonString(migration);

        Mockito.doReturn(migration).when(migrationService).add(migrationNull);

        mvc.perform(post("/api/migration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(migrationNull)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(migrationJson));
    }

    @Test
    public void modifyTest() throws Exception {
        var migration = new Migration(0L, List.of("C"), 1L, 2L, MigrationState.notStarted);
        var migrationJson = asJsonString(migration);

        Mockito.doReturn(migration).when(migrationService).modify(0L, migration);

        mvc.perform(put("/api/migration/0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(migration)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(migrationJson));
    }

    @Test
    public void removeTest() throws Exception {
        mvc.perform(delete("/api/migration/0"))
                .andExpect(status().isOk());
    }

    @Test
    public void runTest() throws Exception {
        var migration = new Migration(0L, List.of("C"), 1L, 2L, MigrationState.notStarted);

        Mockito.doReturn(migration).when(migrationService).validate(0L);

        mvc.perform(put("/api/migration/run/0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Migration started"));
    }

    @Test
    public void statusTest() throws Exception {
        var migration = new Migration(0L, List.of("C"), 1L, 2L, MigrationState.notStarted);

        Mockito.doReturn(migration).when(migrationService).validate(0L);

        mvc.perform(get("/api/migration/status/0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("\"notStarted\""));
    }
}
