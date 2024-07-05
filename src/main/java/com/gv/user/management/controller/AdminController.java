package com.gv.user.management.controller;

import com.gv.user.management.dto.request.AdminUserUpdateRequest;
import com.gv.user.management.dto.response.UserResponse;
import com.gv.user.management.facade.UserServiceFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/admin", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminController {
    private final UserServiceFacade userService;

    @CacheEvict(value = "users", key = "#id")
    @PutMapping("users/{id}")
    @Secured({"ROLE_ADMIN"})
    public UserResponse update(@PathVariable final Long id, @RequestBody final AdminUserUpdateRequest request) {
        return userService.update(id, request);
    }
}
