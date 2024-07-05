package com.gv.user.management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gv.user.management.builder.DtoBuilder;
import com.gv.user.management.config.ContainersConfig;
import com.gv.user.management.dto.request.AdminUserUpdateRequest;
import com.gv.user.management.dto.response.UserResponse;
import com.gv.user.management.facade.UserServiceFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Import(ContainersConfig.class)
@AutoConfigureMockMvc
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserServiceFacade userService;

    @BeforeEach
    void setUp() {
        Mockito.reset(userService);
    }

    @WithMockUser(roles = {"ADMIN"})
    @Test
    void testUpdateAsAdmin() throws Exception {
        AdminUserUpdateRequest request = DtoBuilder.buildAdminUserUpdateRequest();
        UserResponse response = DtoBuilder.buildUserResponse();
        when(userService.update(eq(1L), any(AdminUserUpdateRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/admin/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstname").exists());

        verify(userService, times(1)).update(eq(1L), any(AdminUserUpdateRequest.class));
    }

    @WithMockUser(roles = {"MANAGER"})
    @Test
    void testUpdateAsManager() throws Exception {
        AdminUserUpdateRequest request = DtoBuilder.buildAdminUserUpdateRequest();
        UserResponse response = new UserResponse();
        when(userService.update(eq(1L), any(AdminUserUpdateRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/admin/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isForbidden());

        verify(userService, times(0)).update(eq(1L), any(AdminUserUpdateRequest.class));
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void testUpdateAsUserForbidden() throws Exception {
        AdminUserUpdateRequest request = DtoBuilder.buildAdminUserUpdateRequest();

        mockMvc.perform(put("/api/admin/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isForbidden());

        verify(userService, times(0)).update(eq(1L), any(AdminUserUpdateRequest.class));
    }

    @Test
    void testUpdateWithoutAuthentication() throws Exception {
        AdminUserUpdateRequest request = DtoBuilder.buildAdminUserUpdateRequest();

        mockMvc.perform(put("/api/admin/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isForbidden());

        verify(userService, times(0)).update(eq(1L), any(AdminUserUpdateRequest.class));
    }
}
