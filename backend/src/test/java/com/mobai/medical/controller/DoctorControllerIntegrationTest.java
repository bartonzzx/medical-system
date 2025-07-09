package com.mobai.medical.controller;

import com.mobai.medical.mapper.AccountMapper;
import com.mobai.medical.mapper.DoctorMapper;
import com.mobai.medical.service.DoctorService;
import com.mobai.medical.utils.Msg;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DoctorControllerIntegrationTest {

    @Mock
    private DoctorService doctorService;

    @InjectMocks
    private DoctorController doctorController;

    @Test
    void testResetPwdController_Success() {
        // Arrange
        Long doctorId = 57L;
        Msg expectedResult = Msg.success().mess("重置成功");
        
        when(doctorService.resetPwd(doctorId)).thenReturn(expectedResult);

        // Act
        Msg result = doctorController.resetPwd(doctorId);

        // Assert
        assertTrue(result.isSuccess());
        assertEquals("重置成功", result.getMessage());
        
        // Verify the service was called with correct parameter
        verify(doctorService).resetPwd(doctorId);
    }

    @Test
    void testResetPwdController_DoctorNotFound() {
        // Arrange
        Long doctorId = 999L;
        Msg expectedResult = Msg.fail().mess("医师不存在");
        
        when(doctorService.resetPwd(doctorId)).thenReturn(expectedResult);

        // Act
        Msg result = doctorController.resetPwd(doctorId);

        // Assert
        assertFalse(result.isSuccess());
        assertEquals("医师不存在", result.getMessage());
        
        // Verify the service was called with correct parameter
        verify(doctorService).resetPwd(doctorId);
    }

    @Test
    void testResetPwdController_ResetFailed() {
        // Arrange
        Long doctorId = 57L;
        Msg expectedResult = Msg.fail().mess("重置失败");
        
        when(doctorService.resetPwd(doctorId)).thenReturn(expectedResult);

        // Act
        Msg result = doctorController.resetPwd(doctorId);

        // Assert
        assertFalse(result.isSuccess());
        assertEquals("重置失败", result.getMessage());
        
        // Verify the service was called with correct parameter
        verify(doctorService).resetPwd(doctorId);
    }
}