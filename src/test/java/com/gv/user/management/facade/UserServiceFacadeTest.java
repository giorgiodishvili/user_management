package com.gv.user.management.facade;

import com.gv.user.management.auth.dto.AuthenticationResponse;
import com.gv.user.management.auth.dto.ChangePasswordRequest;
import com.gv.user.management.auth.dto.RegisterRequest;
import com.gv.user.management.builder.DtoBuilder;
import com.gv.user.management.config.ContainersConfig;
import com.gv.user.management.dto.request.AdminUserUpdateRequest;
import com.gv.user.management.dto.request.UserRequest;
import com.gv.user.management.dto.response.UserResponse;
import com.gv.user.management.messaging.event.UserUpdatedEvent;
import com.gv.user.management.messaging.producer.UserUpdateEventProducer;
import com.gv.user.management.model.User;
import com.gv.user.management.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@Import(ContainersConfig.class)
class UserServiceFacadeTest {

    @Autowired
    private UserServiceFacade userServiceFacade;

    @MockBean
    private UserService userService;

    @MockBean
    private UserUpdateEventProducer producer;

    @BeforeEach
    void setUp() {
        // Reset mocks before each test
        org.mockito.Mockito.reset(userService, producer);
    }

    @Test
    void testAddUserSendsEvent() {
        RegisterRequest request = new RegisterRequest();
        AuthenticationResponse response = new AuthenticationResponse();
        response.setUserId(1L);
        when(userService.add(any(RegisterRequest.class))).thenReturn(response);

        userServiceFacade.add(request);

        ArgumentCaptor<UserUpdatedEvent> eventCaptor = ArgumentCaptor.forClass(UserUpdatedEvent.class);
        verify(producer).sendMessage(eventCaptor.capture());

        UserUpdatedEvent capturedEvent = eventCaptor.getValue();
        assertThat(capturedEvent).isNotNull();
        assertThat(capturedEvent.getId()).isEqualTo(1L);
        assertThat(capturedEvent.getEventType()).isEqualTo(UserUpdatedEvent.EventType.CREATE);
    }

    @Test
    void testUpdateUserSendsEvent() {
        UserRequest request = DtoBuilder.buildUserRequest();
        UserResponse response = DtoBuilder.buildUserResponse();
        when(userService.update(eq(1L), any(UserRequest.class))).thenReturn(response);

        userServiceFacade.update(1L, request);

        ArgumentCaptor<UserUpdatedEvent> eventCaptor = ArgumentCaptor.forClass(UserUpdatedEvent.class);
        verify(producer).sendMessage(eventCaptor.capture());

        UserUpdatedEvent capturedEvent = eventCaptor.getValue();
        assertThat(capturedEvent).isNotNull();
        assertThat(capturedEvent.getId()).isEqualTo(1L);
        assertThat(capturedEvent.getEventType()).isEqualTo(UserUpdatedEvent.EventType.UPDATE);
    }

    @Test
    void testUpdateAdminUserSendsEvent() {
        AdminUserUpdateRequest request = DtoBuilder.buildAdminUserUpdateRequest();
        UserResponse response = DtoBuilder.buildUserResponse();
        when(userService.update(eq(1L), any(AdminUserUpdateRequest.class))).thenReturn(response);

        userServiceFacade.update(1L, request);

        ArgumentCaptor<UserUpdatedEvent> eventCaptor = ArgumentCaptor.forClass(UserUpdatedEvent.class);
        verify(producer).sendMessage(eventCaptor.capture());

        UserUpdatedEvent capturedEvent = eventCaptor.getValue();
        assertThat(capturedEvent).isNotNull();
        assertThat(capturedEvent.getId()).isEqualTo(1L);
        assertThat(capturedEvent.getEventType()).isEqualTo(UserUpdatedEvent.EventType.UPDATE);
    }

    @Test
    void testDeleteUserSendsEvent() {
        userServiceFacade.deleteById(1L);

        ArgumentCaptor<UserUpdatedEvent> eventCaptor = ArgumentCaptor.forClass(UserUpdatedEvent.class);
        verify(producer).sendMessage(eventCaptor.capture());

        UserUpdatedEvent capturedEvent = eventCaptor.getValue();
        assertThat(capturedEvent).isNotNull();
        assertThat(capturedEvent.getId()).isEqualTo(1L);
        assertThat(capturedEvent.getEventType()).isEqualTo(UserUpdatedEvent.EventType.DELETE);
    }

    @Test
    void testChangePasswordSendsEvent() {
        ChangePasswordRequest request = DtoBuilder.buildChangePasswordRequest();
        User user = new User();
        user.setId(1L);

        userServiceFacade.changePassword(request, user);

        ArgumentCaptor<UserUpdatedEvent> eventCaptor = ArgumentCaptor.forClass(UserUpdatedEvent.class);
        verify(producer).sendMessage(eventCaptor.capture());

        UserUpdatedEvent capturedEvent = eventCaptor.getValue();
        assertThat(capturedEvent).isNotNull();
        assertThat(capturedEvent.getId()).isEqualTo(1L);
        assertThat(capturedEvent.getEventType()).isEqualTo(UserUpdatedEvent.EventType.PASSWORD_CHANGE);
    }
}
