package com.github.prgrms.social.api.configure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.prgrms.social.api.event.EventExceptionHandler;
import com.github.prgrms.social.api.event.listener.CommentCreatedEventListener;
import com.github.prgrms.social.api.event.listener.JoinEventListener;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class EventConfigure {

    @Value("${eventBus.asyncPool.core}") private int eventBusAsyncPoolCore;
    @Value("${eventBus.asyncPool.max}") private int eventBusAsyncPoolMax;
    @Value("${eventBus.asyncPool.queue}") private int eventBusAsyncPoolQueue;

    @Bean
    public TaskExecutor eventTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("EventBus-");

        // 사용 가능한 스레드 개수(코어 수에 결정됨)
        executor.setCorePoolSize(eventBusAsyncPoolCore);

        // 큐도 가득 차있다면 새로 만들어줄 수 있는 스레드 개수 제한
        executor.setMaxPoolSize(eventBusAsyncPoolMax);

        // 사용 가능한 스레드가 없을 때 대기하는 큐.
        executor.setQueueCapacity(eventBusAsyncPoolQueue);

        // maxpool에 의해 추가로 만들어주는 스레드들의 유효기간
        // executor.setKeepAliveSeconds();

        executor.afterPropertiesSet();
        return executor;
    }

    @Bean
    public EventExceptionHandler eventExceptionHandler() {
        return new EventExceptionHandler();
    }

    // 스레드 풀에서 관리되는 스레드에 의해 생성되는 이벤트.
    @Bean
    public EventBus eventBus(TaskExecutor eventTaskExecutor, EventExceptionHandler eventExceptionHandler) {
        return new AsyncEventBus(eventTaskExecutor, eventExceptionHandler);
    }

    @Bean(destroyMethod = "close")
    public JoinEventListener joinEventListener(
            EventBus eventBus,
            KafkaTemplate<String, String> kafkaTemplate,
            ObjectMapper mapper) {
        return new JoinEventListener(eventBus, kafkaTemplate, mapper);
    }

    @Bean(destroyMethod = "close")
    public CommentCreatedEventListener commentCreatedEventListener(
            EventBus eventBus,
            KafkaTemplate kafkaTemplate) {
        return new CommentCreatedEventListener(eventBus, kafkaTemplate);
    }

}
