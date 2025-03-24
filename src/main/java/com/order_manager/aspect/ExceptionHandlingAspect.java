package com.order_manager.aspect;

import com.order_manager.exception.ExternalServiceException;
import feign.FeignException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExceptionHandlingAspect {

    @Around("com.order_manager.aspect.Pointcuts.allProductServiceMethods()")
    public Object aroundAllProductServiceMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (FeignException e) {
            throw new ExternalServiceException("FeignException in " + joinPoint.getSignature().getName());
        }
    }
}
