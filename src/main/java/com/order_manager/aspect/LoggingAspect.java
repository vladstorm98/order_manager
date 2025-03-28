package com.order_manager.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Around("com.order_manager.aspect.Pointcuts.allControllerMethods()")
    public Object aroundAllControllerMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        var methodName = joinPoint.getSignature().getName();
        log.info("Method {} was launched", methodName);
        var startTime = System.currentTimeMillis();

        try {
            var result = joinPoint.proceed();
            log.info("Method {} was executed in {} ms", methodName, System.currentTimeMillis() - startTime);
            return result;
        } catch (Throwable e) {
            log.error("Method {} thrown an exception", methodName);
            throw e;
        }
    }
}
