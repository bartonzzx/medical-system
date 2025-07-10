package com.mobai.medical.controller;

import com.mobai.medical.entity.AccountEntity;
import com.mobai.medical.service.RegisterService;
import com.mobai.medical.utils.Msg;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class RegisterControllerTest {

    @Mock
    private RegisterService registerService;

    @InjectMocks
    private RegisterController registerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void register_ValidAccount_ReturnsSuccess() {
        // Arrange
        AccountEntity account = createValidAccount();
        Msg expectedMsg = Msg.success().data("account", account);
        when(registerService.register(account)).thenReturn(expectedMsg);

        // Act
        Msg result = registerController.register(account);

        // Assert
        assertNotNull(result);
        assertEquals(expectedMsg, result);
        verify(registerService, times(1)).register(account);
    }

    @Test
    void register_NullAccount_ReturnsError() {
        // Arrange
        Msg expectedMsg = Msg.fail();
        when(registerService.register(null)).thenReturn(expectedMsg);

        // Act
        Msg result = registerController.register(null);

        // Assert
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals(expectedMsg, result);
    }

    @Test
    void register_DuplicateUsername_ReturnsError() {
        // Arrange
        AccountEntity account = createValidAccount();
        Msg expectedMsg = Msg.fail();
        when(registerService.register(account)).thenReturn(expectedMsg);

        // Act
        Msg result = registerController.register(account);

        // Assert
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals("响应失败", result.getMessage());
    }

    @Test
    void register_DuplicatePhone_ReturnsError() {
        // Arrange
        AccountEntity account = createValidAccount();
        Msg expectedMsg = Msg.fail();
        when(registerService.register(account)).thenReturn(expectedMsg);

        // Act
        Msg result = registerController.register(account);

        // Assert
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals("响应失败", result.getMessage());
    }

    @Test
    void register_ServiceException_ReturnsError() {
        // Arrange
        AccountEntity account = createValidAccount();
        when(registerService.register(account))
                .thenThrow(new RuntimeException("数据库连接失败"));

        // Act
        Msg result = registerController.register(account);

        // Assert
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), result.getCode());
    }

    @Test
    void register_MissingRequiredFields_ReturnsValidationError() {
        // Arrange
        AccountEntity invalidAccount = new AccountEntity(); // 缺少必填字段
        Msg expectedMsg = Msg.fail();
        when(registerService.register(invalidAccount)).thenReturn(expectedMsg);

        // Act
        Msg result = registerController.register(invalidAccount);

        // Assert
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals("响应失败", result.getMessage());
    }

    private AccountEntity createValidAccount() {
        AccountEntity account = new AccountEntity();
        account.setUname("testUser");
        account.setPwd("password123");
        account.setPhoneNumber("13812345678");
        account.setUtype("ROLE_3"); // 患者角色
        account.setRealname("测试用户");
        account.setCreatetime(new Date());
        account.setUpdatetime(new Date());
        return account;
    }
}
