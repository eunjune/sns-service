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

        // 사용 가능한 스레드 개수(코어 수에 결정됨)
        executor.setCorePoolSize(eventAsyncPoolCore);

        // 큐도 가득 차있다면 새로 만들어줄 수 있는 스레드 개수 제한
        executor.setMaxPoolSize(eventAsyncPoolMax);

        // 사용 가능한 스레드가 없을 때 대기하는 큐.
        executor.setQueueCapacity(eventAsyncPoolQueue);

        // maxpool에 의해 추가로 만들어지는 스레드들의 유효기간
        // executor.setKeepAliveSeconds();

        executor.afterPropertiesSet();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new EventExceptionHandler();
    }

}
