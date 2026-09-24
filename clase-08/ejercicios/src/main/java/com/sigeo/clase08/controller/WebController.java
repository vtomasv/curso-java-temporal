package com.sigeo.clase08.controller;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
@Controller
public class WebController {
    private final Environment environment;
    public WebController(Environment environment) { this.environment = environment; }
    @GetMapping("/login") public String login(Model model) {
        model.addAttribute("lab", environment.acceptsProfiles(Profiles.of("lab")));
        return "login";
    }
    @GetMapping("/") public String home() { return "redirect:/laboratorio"; }
    @GetMapping("/laboratorio") public String lab(Model model, java.security.Principal principal) {
        model.addAttribute("lab", environment.acceptsProfiles(Profiles.of("lab")));
        model.addAttribute("username", principal.getName());
        return "laboratorio";
    }
    @GetMapping("/formulario") public String showForm() { return "formulario"; }
    @PostMapping("/formulario") public String submitForm() { return "redirect:/formulario?success"; }
}
