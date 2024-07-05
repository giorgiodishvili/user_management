package com.gv.user.management.messaging.event;

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
public class UserUpdatedEvent {
    private Long id;
    private EventType eventType;

    public enum EventType {
        CREATE,
        UPDATE,
        DELETE,
        PASSWORD_CHANGE
    }
}
