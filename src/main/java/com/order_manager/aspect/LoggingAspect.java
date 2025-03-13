package com.order_manager.aspect;


import com.order_manager.dto.ResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Around("com.order_manager.aspect.Pointcuts.allControllerMethods()")
    public Object aroundAllMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        log.info("Method {} was launched", methodName);
        long startTime = System.currentTimeMillis();

        try {
            Object result = joinPoint.proceed();
            log.info("Method {} was executed in {} ms", methodName, System.currentTimeMillis() - startTime);
            return result;
        } catch (Throwable e) {
            log.error("Method {} thrown an exception", methodName);
            throw e;
        }
    }

    @AfterReturning(pointcut = "com.order_manager.aspect.Pointcuts.allGetAllMethods()", returning = "response")
    public void afterAllGetAllMethods(Object response) {
        if (response instanceof List<?> responseList && responseList.getFirst() instanceof ResponseDTO) {
            String entityType = responseList.stream()
                    .map(ResponseDTO.class::cast)
                    .findFirst()
                    .orElseThrow()
                    .getClass()
                    .getSimpleName();

            String entity = entityType.substring(0, entityType.length() - 8);
            log.info("{}s were successfully retrieved", entity);
        }
    }

    @AfterReturning(pointcut = "com.order_manager.aspect.Pointcuts.allGetMethods()", returning = "response")
    public void afterAllGetMethods(ResponseDTO response) {
        String entityType = response.getClass().getSimpleName();

        String entity = entityType.endsWith("Response") ? entityType.substring(0, entityType.length() - 8) : entityType;
        log.info("{} was successfully retrieved", entity);
    }

    @AfterReturning(pointcut = "com.order_manager.aspect.Pointcuts.allCreateMethods()", returning = "response")
    public void afterAllCreateMethods(ResponseDTO response) {
        String entityType = response.getClass().getSimpleName();

        String entity = entityType.endsWith("Response") ? entityType.substring(0, entityType.length() - 8) : entityType;
        log.info("{} was successfully created", entity);
    }

    @AfterReturning(pointcut = "com.order_manager.aspect.Pointcuts.allUpdateMethods()", returning = "response")
    public void afterAllUpdateMethods(ResponseDTO response) {
        String entityType = response.getClass().getSimpleName();

        String entity = entityType.endsWith("Response") ? entityType.substring(0, entityType.length() - 8) : entityType;
        log.info("{} was successfully updated", entity);
    }

    @AfterReturning(pointcut = "com.order_manager.aspect.Pointcuts.allDeleteMethods()")
    public void afterAllDeleteMethods(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();

        String entity = methodName.substring(6);
        log.info("{} was successfully Deleted", entity);
    }
}
