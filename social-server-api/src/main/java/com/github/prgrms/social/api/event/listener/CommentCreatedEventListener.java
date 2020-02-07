package com.github.prgrms.social.api.event.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.prgrms.social.api.event.CommentCreatedEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;

@Slf4j
public class CommentCreatedEventListener implements AutoCloseable{

    private EventBus eventBus;

    private KafkaTemplate<String, CommentCreatedEvent> kafkaTemplate;

    @Value("${spring.kafka.topic.comment-created}")
    private String commentCreatedTopic;

    public CommentCreatedEventListener(EventBus eventBus, KafkaTemplate<String, CommentCreatedEvent> kafkaTemplate) {
        this.eventBus = eventBus;
        this.kafkaTemplate = kafkaTemplate;
        eventBus.register(this);
    }

    // 댓글 작성 이벤트 발생시 카프카에 이벤트 업로드
    @Subscribe
    public void handleCommentCreatedEvent(CommentCreatedEvent event) throws JsonProcessingException {
        Long postWriterSeq = event.getPostWriterSeq();
        String commentWriter = event.getCommentWriter();
        log.info("commentWriter {} , postWriterId {}!", commentWriter, postWriterSeq);
        this.kafkaTemplate.send(commentCreatedTopic, event);
    }

    @Override
    public void close() throws Exception {
        eventBus.unregister(this);
    }
}
