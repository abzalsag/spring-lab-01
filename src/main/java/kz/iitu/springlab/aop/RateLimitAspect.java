package kz.iitu.springlab.aop;

import kz.iitu.springlab.audit.RateLimit;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Aspect
@Component
@Order(4)
public class RateLimitAspect {

    private static final Logger log = LoggerFactory.getLogger(RateLimitAspect.class);
    private static final long WINDOW_MS = 60_000;


    private final Map<String, Deque<Long>> calls = new ConcurrentHashMap<>();

    @Around("@annotation(rateLimit)")
    public Object limit(ProceedingJoinPoint pjp, RateLimit rateLimit) throws Throwable {
        String key = pjp.getSignature().toShortString();
        long now = System.currentTimeMillis();
        Deque<Long> window = calls.computeIfAbsent(key, k -> new ArrayDeque<>());

        synchronized (window) {

            while (!window.isEmpty() && window.peekFirst() < now - WINDOW_MS) {
                window.pollFirst();
            }
            if (window.size() >= rateLimit.perMinute()) {
                log.warn("[RATE] BLOCKED {} — limit {}/min exceeded", key, rateLimit.perMinute());
                throw new RateLimitExceededException(
                        "Rate limit exceeded: " + rateLimit.perMinute() + " calls per minute for " + key);
            }
            window.addLast(now);
            log.info("[RATE] {} — {}/{} in the last minute", key, window.size(), rateLimit.perMinute());
        }
        return pjp.proceed();
    }
}