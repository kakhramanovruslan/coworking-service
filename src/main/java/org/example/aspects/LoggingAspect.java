//package org.example.aspects;
//
//
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.springframework.stereotype.Component;
//
///**
// * Aspect for logging methods annotated with {@link org.example.annotations.Loggable}.
// */
//@Slf4j
//@Component
//@Aspect
//public class LoggingAspect {
//
//    /**
//     * Pointcut definition to match methods annotated with {@link org.example.annotations.Loggable}.
//     */
//    @Pointcut("@annotation(org.example.annotations.Loggable) && execution(* *(..))")
//    public void loggableMethods(){}
//
//    /**
//     * Advice to log method entry, execution time, and exit.
//     * @param joinPoint The ProceedingJoinPoint for the intercepted method.
//     * @return The result of the intercepted method.
//     * @throws Throwable If an error occurs during method execution.
//     */
//    @Around("loggableMethods()")
//    public Object loggableMethods(ProceedingJoinPoint joinPoint) throws Throwable {
//        String methodName = joinPoint.getSignature().toShortString();
//        log.info("Calling method " + methodName);
//
//        long startTime = System.currentTimeMillis();
//        Object result = joinPoint.proceed();
//        long endTime = System.currentTimeMillis();
//
//        log.info("Execution of method " + methodName +
//                " finished. Execution time is " + (endTime - startTime) + " ms.");
//        return result;
//    }
//}
