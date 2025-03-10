package com.order_manager.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.stereotype.Component;
import org.springframework.lang.NonNull;

import java.lang.reflect.Method;

@Component
@Slf4j
public class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    @Override
    public void handleUncaughtException(Throwable throwable, Method method, @NonNull Object... params) {
        log.error("Async error in method {}: {}", method.getName(), throwable.getMessage());
    }
}
