package com.gv.user.management.auth.dto;

import com.gv.user.management.annotations.ValidPassword;
import com.gv.user.management.constant.Role;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String username;
    private String firstname;
    private String lastname;

    @Email
    private String email;

    @ValidPassword
    private String password;

    private Role role;
}
