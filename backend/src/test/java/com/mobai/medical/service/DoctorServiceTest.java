package com.mobai.medical.service;

import com.mobai.medical.mapper.AccountMapper;
import com.mobai.medical.mapper.DoctorMapper;
import com.mobai.medical.utils.Msg;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DoctorServiceTest {

    @Mock
    private DoctorMapper doctorMapper;

    @Mock
    private AccountMapper accountMapper;

    @InjectMocks
    private DoctorService doctorService;

    @BeforeEach
    void setUp() {
        // Setup any common test data here
    }

    @Test
    void testResetPwd_Success() {
        // Arrange
        Long doctorId = 57L;
        Long accountId = 81L;
        
        when(doctorMapper.getAccountIdByDoctorId(doctorId)).thenReturn(accountId);
        when(accountMapper.resetPwd(eq(accountId), anyString())).thenReturn(1);

        // Act
        Msg result = doctorService.resetPwd(doctorId);

        // Assert
        assertTrue(result.isSuccess());
        assertEquals("重置成功", result.getMessage());
        
        // Verify the interactions
        verify(doctorMapper).getAccountIdByDoctorId(doctorId);
        verify(accountMapper).resetPwd(eq(accountId), anyString());
    }

    @Test
    void testResetPwd_DoctorNotFound() {
        // Arrange
        Long doctorId = 999L;
        
        when(doctorMapper.getAccountIdByDoctorId(doctorId)).thenReturn(null);

        // Act
        Msg result = doctorService.resetPwd(doctorId);

        // Assert
        assertFalse(result.isSuccess());
        assertEquals("医师不存在", result.getMessage());
        
        // Verify that resetPwd was not called when doctor doesn't exist
        verify(doctorMapper).getAccountIdByDoctorId(doctorId);
        verify(accountMapper, never()).resetPwd(any(), anyString());
    }

    @Test
    void testResetPwd_ResetFailed() {
        // Arrange
        Long doctorId = 57L;
        Long accountId = 81L;
        
        when(doctorMapper.getAccountIdByDoctorId(doctorId)).thenReturn(accountId);
        when(accountMapper.resetPwd(eq(accountId), anyString())).thenReturn(0);

        // Act
        Msg result = doctorService.resetPwd(doctorId);

        // Assert
        assertFalse(result.isSuccess());
        assertEquals("重置失败", result.getMessage());
        
        // Verify the interactions
        verify(doctorMapper).getAccountIdByDoctorId(doctorId);
        verify(accountMapper).resetPwd(eq(accountId), anyString());
    }

    @Test
    void testResetPwd_PasswordEncryption() {
        // Arrange
        Long doctorId = 57L;
        Long accountId = 81L;
        
        when(doctorMapper.getAccountIdByDoctorId(doctorId)).thenReturn(accountId);
        when(accountMapper.resetPwd(eq(accountId), anyString())).thenReturn(1);

        // Act
        doctorService.resetPwd(doctorId);

        // Assert - verify that the password passed to resetPwd is encrypted
        verify(accountMapper).resetPwd(eq(accountId), argThat(password -> {
            // The password should be encrypted (BCrypt hashes start with $2a$, $2b$, etc.)
            return password.startsWith("$2") && password.length() == 60;
        }));
    }
}