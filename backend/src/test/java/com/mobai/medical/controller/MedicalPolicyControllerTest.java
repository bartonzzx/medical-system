package com.mobai.medical.controller;

import com.mobai.medical.param.MedicalPolicyParam;
import com.mobai.medical.service.MedicalPolicyService;
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

public class MedicalPolicyControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MedicalPolicyService medicalPolicyService;

    @InjectMocks
    private MedicalPolicyController medicalPolicyController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(medicalPolicyController).build();
    }

    // ------------------ getMedicalPolicyWithPage ------------------

    @Test
    public void testGetMedicalPolicyWithPage_ShouldCallServiceAndReturnResult() throws Exception {
        MedicalPolicyParam param = new MedicalPolicyParam();
        Msg mockResponse = Msg.success().data("result", "test");

        when(medicalPolicyService.getMedicalPolicyWithPage(param)).thenReturn(mockResponse);

        mockMvc.perform(get("/api/medical_policys"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"code\":20000,\"message\":\"响应成功\",\"success\":true,\"data\":{\"result\":\"test\"}}"));

        verify(medicalPolicyService, times(1)).getMedicalPolicyWithPage(param);
    }

    // ------------------ saveMedicalPolicy ------------------

//    @Test
//    public void testSaveMedicalPolicy_ShouldCallServiceAndReturnResult() throws Exception {
//        MedicalPolicyParam param = new MedicalPolicyParam();
//        Msg mockResponse = Msg.success();
//
//        when(medicalPolicyService.saveMedicalPolicy(param)).thenReturn(mockResponse);
//
//        mockMvc.perform(post("/api/medical_policys")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"title\":\"test\"}"))
//                .andExpect(status().isOk())
//                .andExpect(content().json("{\"code\":20000,\"message\":\"响应成功\",\"success\":true}"));
//
//        verify(medicalPolicyService, times(1)).saveMedicalPolicy(any(MedicalPolicyParam.class));
//    }

    // ------------------ updateMedicalPolicy ------------------

    @Test
    public void testUpdateMedicalPolicy_MissingTitle_ReturnsError() throws Exception {
        String jsonBody = "{\"message\":\"内容\",\"cityId\":1}";

        mockMvc.perform(put("/api/medical_policys/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(10000))
                .andExpect(jsonPath("$.message").value("标题不能为空"))
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    public void testUpdateMedicalPolicy_MissingMessage_ReturnsError() throws Exception {
        String jsonBody = "{\"title\":\"标题\",\"cityId\":1}";

        mockMvc.perform(put("/api/medical_policys/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(10000))
                .andExpect(jsonPath("$.message").value("内容不能为空"))
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    public void testUpdateMedicalPolicy_MissingCityId_ReturnsError() throws Exception {
        String jsonBody = "{\"title\":\"标题\",\"message\":\"内容\"}";

        mockMvc.perform(put("/api/medical_policys/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(10000))
                .andExpect(jsonPath("$.message").value("城市不能为空"))
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    public void testUpdateMedicalPolicy_ValidInput_CallsService() throws Exception {
        String jsonBody = "{\"title\":\"标题\",\"message\":\"内容\",\"cityId\":1}";
        Msg mockResponse = Msg.success();

        when(medicalPolicyService.updateMedicalPolicy(anyLong(), any(MedicalPolicyParam.class))).thenReturn(mockResponse);

        mockMvc.perform(put("/api/medical_policys/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"code\":20000,\"message\":\"响应成功\",\"success\":true}"));

        verify(medicalPolicyService, times(1)).updateMedicalPolicy(eq(1L), any(MedicalPolicyParam.class));
    }

    // ------------------ deleteMedicalPolicy ------------------

    @Test
    public void testDeleteMedicalPolicy_ShouldCallServiceAndReturnResult() throws Exception {
        Msg mockResponse = Msg.success();

        when(medicalPolicyService.deleteMedicalPolicy(1L)).thenReturn(mockResponse);

        mockMvc.perform(delete("/api/medical_policys/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"code\":20000,\"message\":\"响应成功\",\"success\":true}"));

        verify(medicalPolicyService, times(1)).deleteMedicalPolicy(1L);
    }
}
