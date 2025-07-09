package com.mobai.medical.service;

import com.mobai.medical.mapper.AccountMapper;
import com.mobai.medical.mapper.DoctorMapper;
import com.mobai.medical.param.DoctorParam;
import com.mobai.medical.utils.Msg;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest
class DoctorServiceTest {

    @Mock
    private AccountMapper accountMapper;

    @Mock
    private DoctorMapper doctorMapper;

    private DoctorService doctorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        doctorService = new DoctorService();
        // Use reflection to set the mocked dependencies
        try {
            java.lang.reflect.Field accountMapperField = DoctorService.class.getDeclaredField("accountMapper");
            accountMapperField.setAccessible(true);
            accountMapperField.set(doctorService, accountMapper);
            
            java.lang.reflect.Field doctorMapperField = DoctorService.class.getDeclaredField("doctorMapper");
            doctorMapperField.setAccessible(true);
            doctorMapperField.set(doctorService, doctorMapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testUpdateDoctor_ShouldSucceed_WhenPhoneNumberIsUnique() {
        // Given
        DoctorParam param = new DoctorParam();
        param.setPhoneNumber("11111111111");
        param.setAccountId(77L);
        param.setName("邱鹏");
        param.setAge(22);
        param.setSex(1);
        param.setLevelId(1);
        param.setTypeId(1L);
        
        Long doctorId = 53L;
        
        // Mock: phone number is unique (not used by other accounts)
        when(accountMapper.checkPhoneExcludeAccount("11111111111", 77L)).thenReturn(0);
        when(accountMapper.updateAccount(any())).thenReturn(1);
        when(doctorMapper.updateDoctor(any())).thenReturn(1);
        
        // When
        Msg result = doctorService.updateDoctor(doctorId, param);
        
        // Then
        assertEquals(20000, result.getCode());
        assertEquals("修改成功", result.getMessage());
        assertTrue(result.isSuccess());
        
        // Verify the correct method was called
        verify(accountMapper).checkPhoneExcludeAccount("11111111111", 77L);
        verify(accountMapper, never()).checkPhone(anyString());
    }

    @Test
    void testUpdateDoctor_ShouldFail_WhenPhoneNumberIsUsedByOtherAccount() {
        // Given
        DoctorParam param = new DoctorParam();
        param.setPhoneNumber("11111111111");
        param.setAccountId(77L);
        param.setName("邱鹏");
        
        Long doctorId = 53L;
        
        // Mock: phone number is used by another account
        when(accountMapper.checkPhoneExcludeAccount("11111111111", 77L)).thenReturn(1);
        
        // When
        Msg result = doctorService.updateDoctor(doctorId, param);
        
        // Then
        assertEquals(10001, result.getCode());
        assertEquals("手机号已被使用", result.getMessage());
        assertEquals(false, result.isSuccess());
        
        // Verify no update operations were called
        verify(accountMapper, never()).updateAccount(any());
        verify(doctorMapper, never()).updateDoctor(any());
    }
}