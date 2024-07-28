package com.nr.medicines.configurations;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LogAspect {
    @Before("execution(* com.nr.medicines.services.*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getSignature().getDeclaringTypeName();

        log.info("START - class={}, method={}, args={}", className, methodName, joinPoint.getArgs());
    }

    @After("execution(* com.nr.medicines.services.*.*(..))")
    public void logAfter(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getSignature().getDeclaringTypeName();

        log.info("END - class={}, method={}, args={}", className, methodName, joinPoint.getArgs());
    }
}
