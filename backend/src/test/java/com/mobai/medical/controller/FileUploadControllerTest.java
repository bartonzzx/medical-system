package com.mobai.medical.controller;

import com.mobai.medical.service.FileUploadService;
import com.mobai.medical.utils.Msg;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class FileUploadControllerTest {

    @Mock
    private FileUploadService fileUploadService;

    @InjectMocks
    private FileUploadController fileUploadController;

    private MultipartFile mockFile;
    private Msg successMsg;

    @BeforeEach
    void setUp() {
        // 准备测试数据
        mockFile = new MockMultipartFile(
                "testFile",
                "test.txt",
                "text/plain",
                "test content".getBytes()
        );
        successMsg = Msg.success();
    }

    @Test
    void fileUpload_ShouldReturnSuccessMsg_WhenFileIsValid() {
        // Arrange
        when(fileUploadService.upload(any(MultipartFile.class))).thenReturn(successMsg);

        // Act
        Msg result = fileUploadController.fileUpload(mockFile);

        // Assert
        assertNotNull(result);
        assertEquals(successMsg, result);
        verify(fileUploadService, times(1)).upload(mockFile);
    }

    @Test
    void fileUpload_ShouldHandleNullFile() {
        // Arrange
        Msg errorMsg = Msg.fail();
        when(fileUploadService.upload(null)).thenReturn(errorMsg);

        // Act
        Msg result = fileUploadController.fileUpload(null);

        // Assert
        assertNotNull(result);
        assertEquals(errorMsg, result);
        verify(fileUploadService, times(1)).upload(null);
    }

    @Test
    void fileUpload_ShouldHandleServiceException() {
        // Arrange
        RuntimeException exception = new RuntimeException("Upload failed");
        when(fileUploadService.upload(any(MultipartFile.class))).thenThrow(exception);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            fileUploadController.fileUpload(mockFile);
        });
        verify(fileUploadService, times(1)).upload(mockFile);
    }
}
