package kz.iitu.springlab.web;

import kz.iitu.springlab.security.AppUserRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    private final AppUserRepository users;
    private final JdbcTemplate jdbc;

    public AdminController(AppUserRepository users, JdbcTemplate jdbc) {
        this.users = users;
        this.jdbc = jdbc;
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("users", users.findAll());
        model.addAttribute("tokens", jdbc.queryForList(
                "SELECT username, series, last_used FROM persistent_logins ORDER BY last_used DESC"));
        return "admin";
    }
}