package com.gv.user.management.builder;

import com.gv.user.management.auth.dto.ChangePasswordRequest;
import com.gv.user.management.auth.dto.RegisterRequest;
import com.gv.user.management.constant.Role;
import com.gv.user.management.dto.request.AdminUserUpdateRequest;
import com.gv.user.management.dto.request.UserRequest;
import com.gv.user.management.dto.response.UserResponse;

public class DtoBuilder {

    public static UserResponse buildUserResponse() {
        return new UserResponse("firstName", "lastName", "username", "email@gmail.com", Role.USER);
    }

    public static RegisterRequest buildRegisterRequest() {
        return new RegisterRequest(
                "user", "firstname", "lastname", "giorgi.odishvili@gmail.com", "Password123!", Role.ADMIN);
    }

    public static AdminUserUpdateRequest buildAdminUserUpdateRequest() {
        return new AdminUserUpdateRequest("newName", "newLastName", Role.USER);
    }

    public static ChangePasswordRequest buildChangePasswordRequest() {
        return ChangePasswordRequest.builder()
                .newPassword("newPassword123!")
                .confirmationPassword("newPassword123!")
                .currentPassword("oldPassword123!")
                .build();
    }

    public static UserRequest buildUserRequest() {
        return new UserRequest("newName", "newLastName");
    }
}
