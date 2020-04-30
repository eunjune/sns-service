package com.github.prgrms.social.api.event.listener;

import com.github.prgrms.social.api.event.JoinEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Async
@Component
public class JoinEventListener{

    @EventListener
    public void handleJoinEvent(JoinEvent joinEvent) {
        log.info("user {}, userId {} joined!", joinEvent.getName(), joinEvent.getId());
    }
}
