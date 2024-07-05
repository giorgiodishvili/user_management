package com.gv.user.management.dto.request;

import com.gv.user.management.constant.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserUpdateRequest {
    private String firstname;
    private String lastname;
    private Role role;
}
