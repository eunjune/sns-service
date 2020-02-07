package com.github.prgrms.social.api.event.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.prgrms.social.api.event.JoinEvent;
import com.github.prgrms.social.api.model.commons.Id;
import com.github.prgrms.social.api.model.user.User;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;

@Slf4j
public class JoinEventListener implements AutoCloseable {

    private EventBus eventBus;

    private KafkaTemplate<String, String> kafkaTemplate;

    private ObjectMapper mapper;

    public JoinEventListener(EventBus eventBus, KafkaTemplate<String, String> kafkaTemplate, ObjectMapper mapper) {
        this.eventBus = eventBus;
        this.mapper = mapper;
        this.kafkaTemplate = kafkaTemplate;
        eventBus.register(this);
    }

    // 회원가입 이벤트 발생시 카프카에 이벤트 업로드
    @Subscribe
    public void handleJoinEvent(JoinEvent event) throws JsonProcessingException {
        String name = event.getName();
        Id<User, Long> userId = event.getUserId();
        log.info("user {}, userId {} joined!", name, userId);
        this.kafkaTemplate.send("event.join", this.mapper.writeValueAsString(event));
    }

    @Override
    public void close() throws Exception {
        eventBus.unregister(this);
    }
}
