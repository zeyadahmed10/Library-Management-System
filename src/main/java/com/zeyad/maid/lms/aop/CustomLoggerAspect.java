package com.zeyad.maid.lms.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CustomLoggerAspect {
    private static final Logger logger = LoggerFactory.getLogger(CustomLoggerAspect.class);
    @Before(value = "@annotation(com.zeyad.maid.lms.annotation.CustomLogger)")
    public void logMethodInvocation(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        logger.info("Method {}() in class {} is invoked.", methodName, className);
    }
}
