package com.gv.user.management.auth.dto;

import com.gv.user.management.annotations.ValidPassword;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChangePasswordRequest {

    @ValidPassword
    private String currentPassword;

    @ValidPassword
    private String newPassword;

    @ValidPassword
    private String confirmationPassword;
}
