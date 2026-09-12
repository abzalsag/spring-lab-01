package kz.iitu.springlab.lifecycle;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Clock;

@Component
public class LifecycleDemo {

    private static final Logger log =
            LoggerFactory.getLogger(LifecycleDemo.class);

    private final Clock clock;

    public LifecycleDemo(Clock clock) {
        this.clock = clock;
        log.info("LIFECYCLE >> constructor");
    }

    @PostConstruct
    public void init() {
        log.info("LIFECYCLE >> @PostConstruct at {}", clock.instant());
    }

    @PreDestroy
    public void destroy() {
        log.info("LIFECYCLE >> @PreDestroy at {}", clock.instant());
    }
}