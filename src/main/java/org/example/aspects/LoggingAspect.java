package org.example.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;


@Aspect
@Slf4j
public class LoggingAspect {

    @Pointcut("within(@org.example.annotations.Loggable *) && execution(* *(..))")
    public void loggableMethods() {}

    @Around("loggableMethods()")
    public Object loggableMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        log.info("Calling method " + methodName);

        System.out.println("12312312");

        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();

        log.info("Calling " + methodName + " finished with execution time " + (endTime - startTime) + " ms");
        return result;
    }
}
