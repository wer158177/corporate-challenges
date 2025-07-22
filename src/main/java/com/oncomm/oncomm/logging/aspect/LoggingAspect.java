package com.oncomm.oncomm.logging.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Before("execution(* com.oncomm.oncomm..*(..))")
    public void logBefore(JoinPoint joinPoint) {
        log.info("[START] {}", joinPoint.getSignature());
    }

    @AfterReturning(value = "execution(* com.oncomm.oncomm..*(..))", returning = "result")
    public void logAfter(JoinPoint joinPoint, Object result) {
        log.info("[END] {} => result: {}", joinPoint.getSignature(), result);
    }
}