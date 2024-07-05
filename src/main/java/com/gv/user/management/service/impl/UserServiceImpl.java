package com.gv.user.management.service.impl;

import com.gv.user.management.auth.dto.AuthenticationResponse;
import com.gv.user.management.auth.dto.ChangePasswordRequest;
import com.gv.user.management.auth.dto.RegisterRequest;
import com.gv.user.management.auth.service.AuthenticationService;
import com.gv.user.management.dto.request.AdminUserUpdateRequest;
import com.gv.user.management.dto.request.UserRequest;
import com.gv.user.management.dto.response.UserResponse;
import com.gv.user.management.mapper.UserMapper;
import com.gv.user.management.model.User;
import com.gv.user.management.repository.UserRepository;
import com.gv.user.management.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UserResponse> getAll() {
        return userRepository.findAll().stream().map(userMapper::map).collect(Collectors.toList());
    }

    @Override
    public Page<UserResponse> getAll(final Pageable pageable) {
        return userRepository.findAll(pageable).map(userMapper::map);
    }

    @Override
    public UserResponse getById(final Long id) {
        return userRepository
                .findById(id)
                .map(userMapper::map)
                .orElseThrow(() -> new EntityNotFoundException("User not found by id: " + id));
    }

    @Transactional
    @Override
    public AuthenticationResponse add(final RegisterRequest request) {
        return authenticationService.register(request);
    }

    @Transactional
    @Override
    public UserResponse update(final Long id, final UserRequest request) {
        final var userOptional = userRepository.findById(id);
        return userOptional
                .map((usr) -> {
                    userMapper.fromDto(request, usr);
                    return userMapper.map(userRepository.save(usr));
                })
                .orElseThrow(() -> new EntityNotFoundException("User not found by id: " + id));
    }

    @Transactional
    @Override
    public UserResponse update(final Long id, final AdminUserUpdateRequest request) {
        final var userOptional = userRepository.findById(id);
        return userOptional
                .map((usr) -> {
                    userMapper.fromDto(request, usr);
                    return userMapper.map(userRepository.save(usr));
                })
                .orElseThrow(() -> new EntityNotFoundException("User not found by id: " + id));
    }

    @Transactional
    @Override
    public void deleteById(final Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void changePassword(final ChangePasswordRequest request, final User user) {

        // check if the current password is correct
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }
        // check if the two new passwords are the same
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Password are not the same");
        }

        // update the password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // save the new password
        userRepository.save(user);
    }
}
