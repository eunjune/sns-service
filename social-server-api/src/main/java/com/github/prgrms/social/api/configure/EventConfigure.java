package com.github.prgrms.social.api.configure;

import com.github.prgrms.social.api.event.EventExceptionHandler;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
@EnableAsync
public class EventConfigure implements AsyncConfigurer {

    @Value("${event.asyncPool.core}") private int eventAsyncPoolCore;
    @Value("${event.asyncPool.max}") private int eventAsyncPoolMax;
    @Value("${event.asyncPool.queue}") private int eventAsyncPoolQueue;

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("EventBus-");
        executor.setCorePoolSize(eventAsyncPoolCore);
        executor.setMaxPoolSize(eventAsyncPoolMax);
        executor.setQueueCapacity(eventAsyncPoolQueue);
        executor.afterPropertiesSet();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new EventExceptionHandler();
    }

}
