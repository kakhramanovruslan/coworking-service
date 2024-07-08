package org.example.aspects;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.example.annotations.Auditable;
import org.example.entity.types.ActionType;
import org.example.entity.types.AuditType;
import org.example.service.AuditService;

@Aspect
@Slf4j
public class AuditingAspect {

    private static AuditService auditService;

    public static void setAuditService(AuditService auditService) {
        AuditingAspect.auditService = auditService;
    }

    @Pointcut("execution(@org.example.annotations.Auditable * *(..)) && @annotation(auditable)")
    public void annotatedByAuditable(Auditable auditable) {
    }

    @Around("annotatedByAuditable(auditable)")
    public Object audit(ProceedingJoinPoint pjp, Auditable auditable) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Auditable auditAnnotation = methodSignature.getMethod().getAnnotation(Auditable.class);

        Object[] args = pjp.getArgs();

        String username = "";
        for (int i = 0; i < args.length; i++) {
            if (methodSignature.getParameterNames()[i].equals("username")) {
                username = (String) args[i];
                break;
            }
        }

        String payload = auditAnnotation.username();
        if (payload.isEmpty()) payload = username;

        ActionType actionType = auditAnnotation.actionType();

        try {
            Object result = pjp.proceed();
            AuditingAspect.auditService.record(payload, actionType, AuditType.SUCCESS);
            return result;
        } catch (Throwable ex) {
            AuditingAspect.auditService.record(payload, actionType, AuditType.FAIL);
            throw ex;
        }
    }
}