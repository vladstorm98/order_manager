package com.order_manager.aspects;

import org.aspectj.lang.annotation.Pointcut;

public class Pointcuts {

    @Pointcut("execution(* com.order_manager.controller..*.*(..))")
    public void allMethods() {}

    @Pointcut("execution(* com.order_manager.controller..*.getAll*(..))")
    public void allGetAllMethods() {}

    @Pointcut("execution(* com.order_manager.controller..*.create*(..))")
    public void allCreateMethods() {}

    @Pointcut("execution(* com.order_manager.controller..*.update*(..))")
    public void allUpdateMethods() {}

    @Pointcut("execution(* com.order_manager.controller..*.delete*(..))")
    public void allDeleteMethods() {}
}
