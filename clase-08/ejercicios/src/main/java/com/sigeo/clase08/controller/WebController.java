package com.sigeo.clase08.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class WebController {

    @GetMapping("/formulario")
    public String showForm() {
        return "formulario";
    }

    @PostMapping("/formulario")
    public String submitForm() {
        return "redirect:/formulario?success";
    }
}
