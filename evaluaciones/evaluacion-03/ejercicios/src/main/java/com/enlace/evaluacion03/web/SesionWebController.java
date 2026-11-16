package com.enlace.evaluacion03.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SesionWebController {

    @GetMapping("/login")
    String login() {
        return "login";
    }
}
