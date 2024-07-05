package com.gv.user.management.facade;

import com.gv.user.management.auth.dto.AuthenticationResponse;
import com.gv.user.management.auth.dto.ChangePasswordRequest;
import com.gv.user.management.auth.dto.RegisterRequest;
import com.gv.user.management.dto.request.AdminUserUpdateRequest;
import com.gv.user.management.dto.request.UserRequest;
import com.gv.user.management.dto.response.UserResponse;
import com.gv.user.management.messaging.event.UserUpdatedEvent;
import com.gv.user.management.messaging.producer.UserUpdateEventProducer;
import com.gv.user.management.model.User;
import com.gv.user.management.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserServiceFacade {
    private final UserService userService;
    private final UserUpdateEventProducer producer;

    public List<UserResponse> getAll() {
        return userService.getAll();
    }

    public Page<UserResponse> getAll(final Pageable pageable) {
        return userService.getAll(pageable);
    }

    public UserResponse getById(final Long id) {
        return userService.getById(id);
    }

    public AuthenticationResponse add(final RegisterRequest request) {
        final var response = userService.add(request);
        producer.sendMessage(new UserUpdatedEvent(response.getUserId(), UserUpdatedEvent.EventType.CREATE));

        return response;
    }

    public UserResponse update(final Long id, final UserRequest request) {
        final UserResponse user = userService.update(id, request);
        producer.sendMessage(new UserUpdatedEvent(id, UserUpdatedEvent.EventType.UPDATE));
        return user;
    }

    public UserResponse update(final Long id, final AdminUserUpdateRequest request) {
        final UserResponse user = userService.update(id, request);
        producer.sendMessage(new UserUpdatedEvent(id, UserUpdatedEvent.EventType.UPDATE));
        return user;
    }

    public void deleteById(final Long id) {
        userService.deleteById(id);
        producer.sendMessage(new UserUpdatedEvent(id, UserUpdatedEvent.EventType.DELETE));
    }

    public void changePassword(final ChangePasswordRequest request, final User user) {
        userService.changePassword(request, user);
        producer.sendMessage(new UserUpdatedEvent(user.getId(), UserUpdatedEvent.EventType.PASSWORD_CHANGE));
    }
}
