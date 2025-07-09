package com.twothree.backend.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Around("execution(* com.twothree.backend.controller.*.*(..))")
    public Object logControllerMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        
        log.info("🚀 [CONTROLLER] {}.{}() 시작 - 파라미터: {}", 
                className, methodName, Arrays.toString(joinPoint.getArgs()));
        
        long startTime = System.currentTimeMillis();
        
        try {
            Object result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - startTime;
            
            log.info("✅ [CONTROLLER] {}.{}() 완료 - 실행시간: {}ms", 
                    className, methodName, executionTime);
            
            return result;
        } catch (Exception e) {
            long executionTime = System.currentTimeMillis() - startTime;
            log.error("❌ [CONTROLLER] {}.{}() 실패 - 실행시간: {}ms - 에러: {}", 
                    className, methodName, executionTime, e.getMessage(), e);
            throw e;
        }
    }

    @Around("execution(* com.twothree.backend.service.*.*(..))")
    public Object logServiceMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        
        log.debug("🔧 [SERVICE] {}.{}() 시작", className, methodName);
        
        long startTime = System.currentTimeMillis();
        
        try {
            Object result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - startTime;
            
            log.debug("✅ [SERVICE] {}.{}() 완료 - 실행시간: {}ms", 
                    className, methodName, executionTime);
            
            return result;
        } catch (Exception e) {
            long executionTime = System.currentTimeMillis() - startTime;
            log.error("❌ [SERVICE] {}.{}() 실패 - 실행시간: {}ms - 에러: {}", 
                    className, methodName, executionTime, e.getMessage(), e);
            throw e;
        }
    }

    @Around("execution(* com.twothree.backend.domain.*.*(..))")
    public Object logDomainMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        
        log.debug("🏗️ [DOMAIN] {}.{}() 시작", className, methodName);
        
        long startTime = System.currentTimeMillis();
        
        try {
            Object result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - startTime;
            
            log.debug("✅ [DOMAIN] {}.{}() 완료 - 실행시간: {}ms", 
                    className, methodName, executionTime);
            
            return result;
        } catch (Exception e) {
            long executionTime = System.currentTimeMillis() - startTime;
            log.error("❌ [DOMAIN] {}.{}() 실패 - 실행시간: {}ms - 에러: {}", 
                    className, methodName, executionTime, e.getMessage(), e);
            throw e;
        }
    }
} 