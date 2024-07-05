package com.gv.user.management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gv.user.management.auth.dto.AuthenticationResponse;
import com.gv.user.management.auth.dto.ChangePasswordRequest;
import com.gv.user.management.auth.dto.RegisterRequest;
import com.gv.user.management.builder.DtoBuilder;
import com.gv.user.management.config.ContainersConfig;
import com.gv.user.management.constant.Role;
import com.gv.user.management.dto.request.AdminUserUpdateRequest;
import com.gv.user.management.dto.request.UserRequest;
import com.gv.user.management.dto.response.UserResponse;
import com.gv.user.management.facade.UserServiceFacade;
import com.gv.user.management.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Import(ContainersConfig.class)
@AutoConfigureMockMvc
class UserControllerTest {

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

    @WithMockUser
    @Test
    void testGetAll() throws Exception {
        UserResponse userResponse = DtoBuilder.buildUserResponse();
        List<UserResponse> allUsers = new ArrayList<>();
        allUsers.add(userResponse);
        when(userService.getAll()).thenReturn(allUsers);

        mockMvc.perform(get("/api/users").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        verify(userService, times(1)).getAll();
    }

    @WithMockUser
    @Test
    void testGetAllByPageable() throws Exception {
        UserResponse userResponse = DtoBuilder.buildUserResponse();
        List<UserResponse> allUsers = new ArrayList<>();
        allUsers.add(userResponse);
        Page<UserResponse> page = new PageImpl<>(allUsers);
        when(userService.getAll(any(PageRequest.class))).thenReturn(page);

        mockMvc.perform(get("/api/users/page")
                        .param("page", "0")
                        .param("size", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)));

        verify(userService, times(1)).getAll(any(PageRequest.class));
    }

    @WithMockUser
    @Test
    void testGetById() throws Exception {
        UserResponse userResponse = DtoBuilder.buildUserResponse();
        when(userService.getById(1L)).thenReturn(userResponse);

        mockMvc.perform(get("/api/users/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstname").exists());

        verify(userService, times(1)).getById(1L);
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void testAdd() throws Exception {
        RegisterRequest request = DtoBuilder.buildRegisterRequest();
        AuthenticationResponse response = new AuthenticationResponse();
        when(userService.add(any(RegisterRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk());

        verify(userService, times(1)).add(any(RegisterRequest.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testAddAsUser() throws Exception {
        RegisterRequest request = DtoBuilder.buildRegisterRequest();

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isForbidden());

        verify(userService, times(0)).add(any(RegisterRequest.class));
    }

    @WithMockUser(roles = {"ADMIN", "MANAGER"})
    @Test
    void testUpdate() throws Exception {
        AdminUserUpdateRequest request = DtoBuilder.buildAdminUserUpdateRequest();
        UserResponse response = new UserResponse();
        when(userService.update(eq(1L), any(UserRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk());

        verify(userService, times(1)).update(eq(1L), any(UserRequest.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testUpdateAsUser() throws Exception {
        AdminUserUpdateRequest request = DtoBuilder.buildAdminUserUpdateRequest();

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isForbidden());

        verify(userService, times(0)).update(eq(1L), any(UserRequest.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testAddWithInvalidEmail() throws Exception {
        RegisterRequest request = DtoBuilder.buildRegisterRequest();
        request.setEmail("invalid-Email");

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isBadRequest());

        verify(userService, times(0)).add(any(RegisterRequest.class));
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void testAddAsManager() throws Exception {
        RegisterRequest request = DtoBuilder.buildRegisterRequest();

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isForbidden());

        verify(userService, times(0)).add(any(RegisterRequest.class));
    }

    @WithMockUser
    @Test
    void testUpdateSelf() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setRole(Role.USER);
        UserRequest request = DtoBuilder.buildUserRequest();
        UserResponse response = new UserResponse();
        when(userService.update(eq(1L), any(UserRequest.class))).thenReturn(response);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        mockMvc.perform(put("/api/users/me")
                        .principal(authenticationToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk());

        verify(userService, times(1)).update(eq(1L), any(UserRequest.class));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void testDeleteById() throws Exception {
        mockMvc.perform(delete("/api/users/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService, times(1)).deleteById(1L);
    }

    @WithMockUser
    @Test
    void testChangePassword() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setRole(Role.USER);
        ChangePasswordRequest request = DtoBuilder.buildChangePasswordRequest();
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        mockMvc.perform(patch("/api/users")
                        .principal(authenticationToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk());

        verify(userService, times(1)).changePassword(any(ChangePasswordRequest.class), eq(user));
    }
}
