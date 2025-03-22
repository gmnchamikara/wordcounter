package com.example.wordcounter.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Around("execution(* com.example.wordcounter.controller.*.*(..))")
    public Object logController(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        logger.info("Received request: {} with args: {}",
                joinPoint.getSignature(), joinPoint.getArgs());

        try {
            Object result = joinPoint.proceed();
            if (result instanceof ResponseEntity) {
                logger.info("Response status: {}", ((ResponseEntity<?>) result).getStatusCode());
            }
            logger.info("Completed request: {} in {}ms",
                    joinPoint.getSignature(),
                    System.currentTimeMillis() - startTime);
            return result;
        } catch (Exception e) {
            logger.error("Error in {}: {}", joinPoint.getSignature(), e.getMessage());
            throw e;
        }
    }
}
