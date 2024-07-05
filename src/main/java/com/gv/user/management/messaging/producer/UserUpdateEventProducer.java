package com.gv.user.management.messaging.producer;

import com.gv.user.management.messaging.event.UserUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserUpdateEventProducer {
    private final StreamBridge streamBridge;

    public void sendMessage(final UserUpdatedEvent message) {
        log.info("send [UserUpdatedEvent] event: {}", message);
        streamBridge.send("user-updated-out-0", message);
    }
}
