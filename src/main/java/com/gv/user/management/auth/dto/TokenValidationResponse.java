package com.gv.user.management.auth.dto;

import com.gv.user.management.constant.Role;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

@Data
@Builder
public class TokenValidationResponse {
    private boolean isValid;
    private String message;
    private Long id;
    private String username;
    private Role role;
    private List<SimpleGrantedAuthority> permissions;
}
