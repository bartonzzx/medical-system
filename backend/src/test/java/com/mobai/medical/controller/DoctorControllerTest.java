package com.mobai.medical.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobai.medical.param.DoctorParam;
import com.mobai.medical.service.DoctorService;
import com.mobai.medical.utils.Msg;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for the DoctorController class.
 */
@SpringBootTest
public class DoctorControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DoctorService doctorService;

    @InjectMocks
    private DoctorController doctorController;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(doctorController).build();
    }

    @Test
    public void testGetDoctorWithPage() throws Exception {
        Msg mockResponse = new Msg();
        when(doctorService.getDoctorWithPage(any(DoctorParam.class))).thenReturn(mockResponse);

        mockMvc.perform(get("/api/doctors"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(doctorService, times(1)).getDoctorWithPage(any(DoctorParam.class));
    }

    @Test
    public void testGetLevelAndType() throws Exception {
        Msg mockResponse = new Msg();
        when(doctorService.getLevelAndType()).thenReturn(mockResponse);

        mockMvc.perform(get("/api/doctors/info"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(doctorService, times(1)).getLevelAndType();
    }

    @Test
    public void testSaveDoctor_ValidParam() throws Exception {
        DoctorParam param = new DoctorParam();
        param.setName("张三");
        param.setPhoneNumber("13800000000");
        param.setLevelId(1);
        param.setTypeId(1L);
        param.setSex(1);
        param.setAge(30);
        Msg mockResponse = new Msg();
        when(doctorService.saveDoctor(any(DoctorParam.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/api/doctors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(param)))
                .andExpect(status().isOk());

        verify(doctorService, times(1)).saveDoctor(any(DoctorParam.class));
    }

    @Test
    public void testUpdateDoctor() throws Exception {
        Long id = 1L;
        DoctorParam param = new DoctorParam();
        Msg mockResponse = new Msg();
        when(doctorService.updateDoctor(eq(id), any(DoctorParam.class))).thenReturn(mockResponse);

        mockMvc.perform(put("/api/doctors/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Dr. Updated\"}"))
               .andExpect(status().isOk());

        verify(doctorService, times(1)).updateDoctor(eq(id), any(DoctorParam.class));
    }

    @Test
    public void testDeleteDoctor() throws Exception {
        Long id = 1L;
        Msg mockResponse = new Msg();
        when(doctorService.deleteDoctorById(eq(id))).thenReturn(mockResponse);

        mockMvc.perform(delete("/api/doctors/{id}", id))
               .andExpect(status().isOk());

        verify(doctorService, times(1)).deleteDoctorById(eq(id));
    }

    @Test
    public void testResetPwd() throws Exception {
        Long id = 1L;
        Msg mockResponse = new Msg();
        when(doctorService.resetPwd(eq(id))).thenReturn(mockResponse);

        mockMvc.perform(put("/api/doctors/reset/{id}", id))
               .andExpect(status().isOk());

        verify(doctorService, times(1)).resetPwd(eq(id));
    }
}
