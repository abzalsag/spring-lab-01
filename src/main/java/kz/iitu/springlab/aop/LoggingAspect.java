package kz.iitu.springlab.aop;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(1)
public class LoggingAspect {

    private static final Logger log =
            LoggerFactory.getLogger(LoggingAspect.class);

    @Before("kz.iitu.springlab.aop.Pointcuts.serviceOperation()")
    public void before(JoinPoint jp) {
        log.info("ENTER {} args={}",
                jp.getSignature().toShortString(),
                Arrays.toString(jp.getArgs()));
    }

    @AfterReturning(
            pointcut = "kz.iitu.springlab.aop.Pointcuts.serviceOperation()",
            returning = "result"
    )
    public void afterReturning(JoinPoint jp, Object result) {
        log.info("RETURN {} result={}",
                jp.getSignature().toShortString(),
                result);
    }

    @AfterThrowing(
            pointcut = "kz.iitu.springlab.aop.Pointcuts.serviceOperation()",
            throwing = "ex"
    )
    public void afterThrowing(JoinPoint jp, Throwable ex) {
        log.error("ERROR {} type={} message={}",
                jp.getSignature().toShortString(),
                ex.getClass().getSimpleName(),
                ex.getMessage());
    }
}