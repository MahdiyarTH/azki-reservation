package com.azki.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ExecutionTimeAspect {

    @Around("execution(* *(..)) && (within(com.azki..*Repository*) || within(com.azki..*Service*))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long duration = System.currentTimeMillis() - start;

        if (duration < 50) {
            log.debug("Method {} executed in {} ms", joinPoint.getSignature(), duration);
        } else {
            log.warn("Method {} executed in {} ms", joinPoint.getSignature(), duration);
        }

        return proceed;
    }
}
