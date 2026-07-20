package com.curso.solucion05;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/misiones")
public class MisionController {
    @GetMapping
    public String getMisiones() { return "[]"; }
}
