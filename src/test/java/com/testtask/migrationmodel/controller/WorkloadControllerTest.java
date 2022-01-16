package com.testtask.migrationmodel.controller;

import com.testtask.migrationmodel.entity.Workload;
import com.testtask.migrationmodel.repository.WorkloadRepository;
import com.testtask.migrationmodel.service.WorkloadService;
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

@WebMvcTest(WorkloadController.class)
public class WorkloadControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private WorkloadService workloadService;
    @MockBean
    private WorkloadRepository workloadRepository;

    @Test
    public void addTest() throws Exception {
        var workloadNull = new Workload(0L, "1.2.3", 1L, List.of(2L));
        var workload = new Workload(0L, "1.2.3", 1L, List.of(2L));
        var workloadJson = asJsonString(workload);

        Mockito.doReturn(workload).when(workloadService).add(workloadNull);

        mvc.perform(post("/api/workload")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(workloadNull)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(workloadJson));
    }

    @Test
    public void modifyTest() throws Exception {
        var workload = new Workload(0L, "1.2.3", 1L, List.of(2L));
        var workloadJson = asJsonString(workload);

        Mockito.doReturn(workload).when(workloadService).modify(0L,workload);

        mvc.perform(put("/api/workload/0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(workload)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(workloadJson));
    }

    @Test
    public void removeTest() throws Exception {
        mvc.perform(delete("/api/workload/0"))
                .andExpect(status().isOk());
    }
}
