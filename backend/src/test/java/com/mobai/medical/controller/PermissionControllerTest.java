
package com.mobai.medical.controller;

import com.mobai.medical.service.PermissionService;
import com.mobai.medical.utils.Msg;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class PermissionControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Mock
    private PermissionService permissionService;

    @InjectMocks
    private PermissionController permissionController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testGetAllPermission() throws Exception {
        // Arrange
        String roleName = "admin";
        Msg expectedResponse = Msg.success().data("permissions", new ArrayList<>());

        when(permissionService.getAllPermission(roleName)).thenReturn(new ArrayList<>());

        // Act and Assert
        mockMvc.perform(get("/api/permissions").param("roleName", roleName))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse.toString()));

        verify(permissionService, times(1)).getAllPermission(roleName);
    }
}
