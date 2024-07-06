package com.gv.user.management.controller;

import com.gv.user.management.auth.dto.AuthenticationResponse;
import com.gv.user.management.auth.dto.ChangePasswordRequest;
import com.gv.user.management.auth.dto.RegisterRequest;
import com.gv.user.management.dto.request.UserRequest;
import com.gv.user.management.dto.response.UserResponse;
import com.gv.user.management.facade.UserServiceFacade;
import com.gv.user.management.model.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
    private final UserServiceFacade userService;

    @Cacheable(value = "users")
    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    @GetMapping
    public List<UserResponse> getAll() {
        return userService.getAll();
    }

    @GetMapping("/page")
    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    public Page<UserResponse> getAllByPageable(final Pageable pageable) {
        return userService.getAll(pageable);
    }

    @Cacheable(value = "users", key = "#id")
    @GetMapping("{id}")
    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    public UserResponse getById(@PathVariable final Long id) {
        log.debug("Fetching user with ID: {}", id);
        return userService.getById(id);
    }

    @CacheEvict(value = "users", allEntries = true)
    @PostMapping
    @Secured({"ROLE_ADMIN"})
    public AuthenticationResponse add(@RequestBody @Valid final RegisterRequest request) {
        log.info("Creating user with email: {}", request.getEmail());
        return userService.add(request);
    }

    @CacheEvict(value = "users", key = "#id")
    @PutMapping("{id}")
    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    public UserResponse update(@PathVariable final Long id, @RequestBody final UserRequest request) {
        log.info("Updating user with id: {}", id);

        return userService.update(id, request);
    }

    @CacheEvict(value = "users", allEntries = true)
    @PutMapping("me")
    public UserResponse update(@AuthenticationPrincipal final User user, @RequestBody final UserRequest request) {
        log.info("Updating oneself with id: {}, request: {}", user.getId(), request);

        return userService.update(user.getId(), request);
    }

    @GetMapping("me")
    public UserResponse getMe(@AuthenticationPrincipal final User user) {
        log.debug("Get oneself with id: {}", user.getId());
        return userService.getById(user.getId());
    }

    @CacheEvict(value = "users", key = "#id")
    @DeleteMapping("{id}")
    @Secured({"ROLE_ADMIN"})
    public void deleteById(@PathVariable final Long id) {
        log.info("Deleting user with id: {}", id);

        userService.deleteById(id);
    }

    @PatchMapping
    public ResponseEntity<?> changePassword(
            @AuthenticationPrincipal final User connectedUser, @RequestBody final ChangePasswordRequest request) {
        log.info("Change password for user with id: {}", connectedUser.getId());

        userService.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }
}
