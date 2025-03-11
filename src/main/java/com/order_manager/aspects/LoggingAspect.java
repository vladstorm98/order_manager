package com.order_manager.aspects;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Around("com.order_manager.aspects.Pointcuts.allMethods()")
    public Object aroundAllMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        log.info("Method {} was launched", methodName);
        long startTime = System.currentTimeMillis();

        try {
            Object result = joinPoint.proceed();
            log.info("Method {} was executed in {} ms", methodName, System.currentTimeMillis() - startTime);
            return result;
        } catch (Throwable e) {
            log.error("Method {} thrown an exception: {}", methodName, e.getMessage());
            throw e;
        }
    }
}
