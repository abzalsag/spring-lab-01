package kz.iitu.springlab.notify;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component("repeating")
@Order(3)
public class RepeatingNotifier implements Notifier {

    private static final Logger log =
            LoggerFactory.getLogger(RepeatingNotifier.class);

    private final int repeatCount;

    public RepeatingNotifier(
            @Value("${app.repeat-count:3}") int repeatCount
    ) {
        this.repeatCount = repeatCount;
    }

    @PostConstruct
    public void init() {
        log.info(
                "REPEATING >> initialized with repeatCount={}",
                repeatCount
        );
    }

    @Override
    public String send(String message) {
        return String.join(
                " ",
                Collections.nCopies(repeatCount, message)
        );
    }

    @Override
    public String channel() {
        return "repeating";
    }
}