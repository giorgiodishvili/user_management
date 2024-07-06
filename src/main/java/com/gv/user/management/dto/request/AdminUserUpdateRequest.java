package com.gv.user.management.dto.request;

import com.gv.user.management.constant.Role;

public record AdminUserUpdateRequest(String firstname, String lastname, Role role) {}
