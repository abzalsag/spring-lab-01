package kz.iitu.springlab.web;

import kz.iitu.springlab.notify.NotificationService;
import kz.iitu.springlab.scope.TicketOffice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/lab2")
public class Lab2Controller {

    private final NotificationService notificationService;
    private final TicketOffice ticketOffice;

    public Lab2Controller(
            NotificationService notificationService,
            TicketOffice ticketOffice
    ) {
        this.notificationService = notificationService;
        this.ticketOffice = ticketOffice;
    }

    @GetMapping("/notify")
    public Map<String, Object> notify(
            @RequestParam(defaultValue = "Hello IoC") String text
    ) {
        Map<String, Object> response = new LinkedHashMap<>();

        response.put(
                "primary",
                notificationService.viaPrimary(text)
        );

        response.put(
                "console",
                notificationService.viaConsole(text)
        );

        response.put(
                "all",
                notificationService.viaAll(text)
        );

        response.put(
                "beans",
                notificationService.beanNames()
        );

        return response;
    }

    @GetMapping("/scopes")
    public Map<String, Object> scopes() {
        return ticketOffice.demo();
    }

    @GetMapping("/custom")
    public Map<String, String> custom(
            @RequestParam String text
    ) {
        Map<String, String> response = new LinkedHashMap<>();

        response.put("channel", "repeating");
        response.put(
                "result",
                notificationService.viaCustom(text)
        );

        return response;
    }
}