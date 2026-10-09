package kz.iitu.springlab.aop;

import kz.iitu.springlab.audit.Audited;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(3)
public class AuditAspect {

    private static final Logger log = LoggerFactory.getLogger(AuditAspect.class);

    @Around("@annotation(audited)")
    public Object audit(ProceedingJoinPoint pjp, Audited audited) throws Throwable {
        String actionName = audited.action();
        log.info("AUDIT START: action='{}'", actionName);

        try {
            Object result = pjp.proceed();
            log.info("AUDIT SUCCESS: action='{}'", actionName);
            return result;
        } catch (Throwable ex) {
            log.error("AUDIT FAILURE: action='{}', error={}", actionName, ex.getMessage());
            throw ex;
        }
    }
}