package com.order_manager.aspects;

import com.order_manager.dto.ResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

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
            log.error("Method {} thrown an exception", methodName);
            throw e;
        }
    }

    @AfterReturning(pointcut = "com.order_manager.aspects.Pointcuts.allGetAllMethods()", returning = "response")
    public void afterAllGetAllMethods(Object response) {
        if (response instanceof List<?> responseList && responseList.getFirst() instanceof ResponseDTO) {
            String entityType = responseList.stream()
                    .map(ResponseDTO.class::cast)
                    .findFirst()
                    .orElseThrow()
                    .getClass()
                    .getSimpleName();

            String entity = StringUtils.removeEnd(entityType, "Response");
            log.info("{}s were successfully retrieved", entity);
        }
    }

    @AfterReturning(pointcut = "com.order_manager.aspects.Pointcuts.allCreateMethods()", returning = "response")
    public void afterAllCreateMethods(ResponseDTO response) {
        String entity = getEntity(response);

        try {
            Method idMethod = response.getClass().getMethod("id");
            Object id = idMethod.invoke(response);
            log.info("{} with id #{} was created", entity, id);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
            getWarn(entity);
        }
    }

    @AfterReturning(pointcut = "com.order_manager.aspects.Pointcuts.allUpdateMethods()", returning = "response")
    public void afterAllUpdateMethods(ResponseDTO response) {
        String entity = getEntity(response);

        try {
            Method idMethod = response.getClass().getMethod("id");
            Object id = idMethod.invoke(response);
            log.info("{} with id #{} was updated", entity, id);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
            getWarn(entity);
        }
    }

    @AfterReturning(pointcut = "com.order_manager.aspects.Pointcuts.allDeleteMethods()")
    public void afterAllDeleteMethods(JoinPoint joinPoint) {
        String entityType = joinPoint.getSignature().getName();
        String entity = StringUtils.removeStart(entityType, "delete");

        Object id = joinPoint.getArgs()[0] instanceof Long ? joinPoint.getArgs()[0] : null;
        log.info("{} with id #{} was deleted", entity, id);
    }

    private static String getEntity(ResponseDTO response) {
        String entityType = response.getClass().getSimpleName();
        return StringUtils.removeEnd(entityType, "Response");
    }

    private static void getWarn(String entity) {
        log.warn("Failed to extract ID from {}", entity);
    }
}
