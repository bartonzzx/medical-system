package com.mobai.medical.controller;

import com.mobai.medical.param.MaterialParam;
import com.mobai.medical.service.MaterialService;
import com.mobai.medical.utils.Msg;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class MaterialControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MaterialService materialService;

    @InjectMocks
    private MaterialController materialController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(materialController).build();
    }

    @Test
    public void testGetPolicyWithPage() throws Exception {
        MaterialParam param = new MaterialParam();
        when(materialService.getAllMaterialWithPage(param)).thenReturn(Msg.success());

        mockMvc.perform(get("/api/materials"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    public void testSaveMedicalPolicy() throws Exception {
        MaterialParam param = new MaterialParam();
        param.setTitle("test");
        param.setMessage("content");

        when(materialService.saveMaterial(param)).thenReturn(Msg.success());

        mockMvc.perform(post("/api/materials")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"test\",\"message\":\"content\"}"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    public void testUpdateMedicalPolicy_TitleEmpty() throws Exception {
        MaterialParam param = new MaterialParam();
        param.setMessage("content");

        mockMvc.perform(put("/api/materials/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"message\":\"content\"}"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.success").value(false))
               .andExpect(jsonPath("$.message").value("标题不能为空"));
    }

    @Test
    public void testUpdateMedicalPolicy_MessageEmpty() throws Exception {
        MaterialParam param = new MaterialParam();
        param.setTitle("test");

        mockMvc.perform(put("/api/materials/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"test\"}"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.success").value(false))
               .andExpect(jsonPath("$.message").value("内容不能为空"));
    }

    @Test
    public void testUpdateMedicalPolicy_Success() throws Exception {
        MaterialParam param = new MaterialParam();
        param.setTitle("test");
        param.setMessage("content");

        when(materialService.updateMaterial(1, param)).thenReturn(Msg.success());

        mockMvc.perform(put("/api/materials/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"test\",\"message\":\"content\"}"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    public void testDeletePolicy() throws Exception {
        when(materialService.deleteMaterial(1L)).thenReturn(Msg.success());

        mockMvc.perform(delete("/api/materials/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.success").value(true));
    }
}
