//package org.example.aspects;
//
//import jakarta.servlet.ServletContext;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.*;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.example.annotations.Auditable;
//import org.example.dto.Authentication;
//import org.example.dto.AuthRequest;
//import org.example.entity.types.ActionType;
//import org.example.entity.types.AuditType;
//import org.example.service.AuditService;
//import org.springframework.stereotype.Component;
//
//
///**
// * Aspect for auditing annotated methods with {@link Auditable} annotation.
// */
//@Aspect
//@Component
//@Slf4j
//@RequiredArgsConstructor
//public class AuditingAspect {
//
//    private final AuditService auditService;
//
//    private final ServletContext servletContext;
//
//    /**
//     * Pointcut definition to match methods annotated with {@link Auditable}.
//     * @param auditable The Auditable annotation instance.
//     */
//    @Pointcut("execution(@org.example.annotations.Auditable * *(..)) && @annotation(auditable)")
//    public void annotatedByAuditable(Auditable auditable) {
//    }
//
//    /**
//     * Advice to perform auditing around methods annotated with {@link Auditable}.
//     * @param pjp The ProceedingJoinPoint for the intercepted method.
//     * @param auditable The Auditable annotation instance.
//     * @return The result of the intercepted method.
//     * @throws Throwable If an error occurs during method execution.
//     */
//    @Around("annotatedByAuditable(auditable)")
//    public Object audit(ProceedingJoinPoint pjp, Auditable auditable) throws Throwable {
//        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
//        Auditable auditAnnotation = methodSignature.getMethod().getAnnotation(Auditable.class);
//
//        ActionType actionType = auditAnnotation.actionType();
//        String payload = "";
//
//        if (actionType == ActionType.AUTHORIZATION || actionType == ActionType.REGISTRATION) {
//            payload = extractUsernameFromArgs(pjp.getArgs());
//        } else {
//            payload = payloadParser(auditAnnotation.username());
//        }
//
//        try {
//            Object result = pjp.proceed();
//            auditService.record(payload, actionType, AuditType.SUCCESS);
//            return result;
//        } catch (Throwable ex) {
//            auditService.record(payload, actionType, AuditType.FAIL);
//            throw ex;
//        }
//    }
//
//    private String payloadParser(String payload) {
//        if (payload.isEmpty()) {
//            Authentication authUser = (Authentication) servletContext.getAttribute("authentication");
//            if (authUser != null) {
//                return authUser.getUsername();
//            }
//            return "anonymous";
//        }
//        return payload;
//    }
//
//    private String extractUsernameFromArgs(Object[] args) {
//        for (Object arg : args) {
//            if (arg instanceof AuthRequest) {
//                return ((AuthRequest) arg).username();
//            }
//        }
//        return "anonymous";
//    }
//}
