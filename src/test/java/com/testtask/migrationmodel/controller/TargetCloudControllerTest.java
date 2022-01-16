package com.testtask.migrationmodel.controller;

import com.testtask.migrationmodel.entity.CloudType;
import com.testtask.migrationmodel.entity.TargetCloud;
import com.testtask.migrationmodel.repository.TargetCloudRepository;
import com.testtask.migrationmodel.service.TargetCloudService;
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

@WebMvcTest(TargetController.class)
public class TargetCloudControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private TargetCloudService targetCloudService;
    @MockBean
    private TargetCloudRepository targetCloudRepository;

    @Test
    public void addTest() throws Exception {
        var targetCloudNull = new TargetCloud(null, CloudType.aws,1L,2L);
        var targetCloud = new TargetCloud(0L, CloudType.aws,1L,2L);
        var targetCloudJson = asJsonString(targetCloud);

        Mockito.doReturn(targetCloud).when(targetCloudService).add(targetCloudNull);

        mvc.perform(post("/api/target")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(targetCloudNull)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(targetCloudJson));
    }

    @Test
    public void modifyTest() throws Exception {
        var targetCloud = new TargetCloud(0L, CloudType.aws,1L,2L);
        var targetCloudJson = asJsonString(targetCloud);

        Mockito.doReturn(targetCloud).when(targetCloudService).modify(0L, targetCloud);

        mvc.perform(put("/api/target/0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(targetCloud)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(targetCloudJson));
    }

    @Test
    public void removeTest() throws Exception {
        mvc.perform(delete("/api/target/0"))
                .andExpect(status().isOk());
    }
}
