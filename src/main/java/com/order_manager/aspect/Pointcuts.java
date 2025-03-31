package com.order_manager.aspect;

import org.aspectj.lang.annotation.Pointcut;

public class Pointcuts {

    @Pointcut("execution(* com.order_manager.controller..*.*(..))")
    public void allControllerMethods() {}

    @Pointcut("execution(* com.order_manager.service.ProductService.*(..))")
    public void allProductServiceMethods() {}
}
