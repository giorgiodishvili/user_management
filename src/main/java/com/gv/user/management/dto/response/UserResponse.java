package com.gv.user.management.dto.response;

import com.gv.user.management.constant.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserResponse {
    private String firstname;
    private String lastname;
    private String username;
    private String email;
    private Role role;
}
