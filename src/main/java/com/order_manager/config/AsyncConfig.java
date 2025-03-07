package com.order_manager.config;

import com.order_manager.exception.AsyncExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncAnnotationBeanPostProcessor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(6);
        executor.setMaxPoolSize(12);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("Async-");
        executor.initialize();
        return executor;
    }

    @Bean
    public AsyncAnnotationBeanPostProcessor asyncAnnotationBeanPostProcessor() {
        AsyncAnnotationBeanPostProcessor processor = new AsyncAnnotationBeanPostProcessor();
        processor.setExceptionHandler(new AsyncExceptionHandler());
        return processor;
    }
}
