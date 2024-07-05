package com.gv.user.management.service;

import com.gv.user.management.auth.dto.AuthenticationResponse;
import com.gv.user.management.auth.dto.ChangePasswordRequest;
import com.gv.user.management.auth.dto.RegisterRequest;
import com.gv.user.management.dto.request.AdminUserUpdateRequest;
import com.gv.user.management.dto.request.UserRequest;
import com.gv.user.management.dto.response.UserResponse;
import com.gv.user.management.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    List<UserResponse> getAll();

    Page<UserResponse> getAll(Pageable pageable);

    UserResponse getById(Long id);

    AuthenticationResponse add(RegisterRequest request);

    UserResponse update(Long id, UserRequest request);

    UserResponse update(Long id, AdminUserUpdateRequest request);

    void deleteById(Long id);

    void changePassword(ChangePasswordRequest request, User connectedUser);
}
