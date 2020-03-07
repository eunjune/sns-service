package com.github.prgrms.social.api.configure.support;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class LodAdvice {

    @Pointcut("execution(* com.github.prgrms.social.api.controller..*(..))")
    public void controllerMethod() { }

    @Pointcut("execution(* com.github.prgrms.social.api.service..*(..))")
    public void serviceMethod() { }

    @Pointcut("execution(* com.github.prgrms.social.api.model..*(..))")
    public void modelMethod() { }

    @Before("controllerMethod()")
    public void controllerBeforePrintLog(JoinPoint jointPoint) {
        log.info("[{}] Controller Method Before", jointPoint.getSignature().getName());
    }

    @Before("serviceMethod()")
    public void serviceBeforePrintLog(JoinPoint jointPoint) {
        log.info("[{}] Service Method Before", jointPoint.getSignature().getName());
    }

    @Before("modelMethod()")
    public void modelBeforePrintLog(JoinPoint jointPoint) {
        log.info("[{}] Model Method Before", jointPoint.getSignature().getName());
    }

    @AfterReturning(pointcut = "controllerMethod()")
    public void controllerSuccessPrintLog(JoinPoint jointPoint) {
        log.info("[{}] Controller Method Success", jointPoint.getSignature().getName());
    }

    @AfterReturning(pointcut = "serviceMethod()")
    public void serviceSuccessPrintLog(JoinPoint jointPoint) {
        log.info("[{}] Service Method Success", jointPoint.getSignature().getName());
    }

    @AfterReturning(pointcut = "modelMethod()")
    public void modelSuccessPrintLog(JoinPoint jointPoint) {
        log.info("[{}] Model Method Success", jointPoint.getSignature().getName());
    }

}
