package com.testtask.migrationmodel.controller;

import com.testtask.migrationmodel.entity.Volume;
import com.testtask.migrationmodel.repository.VolumeRepository;
import com.testtask.migrationmodel.service.VolumeService;
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

@WebMvcTest(VolumeController.class)
public class VolumeControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private VolumeService volumeService;
    @MockBean
    private VolumeRepository volumeRepository;

    @Test
    public void addTest() throws Exception {
        var volumeNull = new Volume(null, "C:/", 1);
        var volume = new Volume(0L, "C:/", 1);
        var volumeJson = asJsonString(volume);

        Mockito.doReturn(volume).when(volumeService).add(volumeNull);

        mvc.perform(post("/api/volume")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(volumeNull)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(volumeJson));
    }

    @Test
    public void modifyTest() throws Exception {
        var volumeModify = new Volume(0L, "D:/", 2);
        var volumeJson = asJsonString(volumeModify);

        Mockito.doReturn(volumeModify).when(volumeService).modify(0L, volumeModify);

        mvc.perform(put("/api/volume/0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(volumeModify)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(volumeJson));
    }

    @Test
    public void removeTest() throws Exception {
        mvc.perform(delete("/api/volume/0"))
                .andExpect(status().isOk());
    }
}
