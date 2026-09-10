package com.sigeo.clase05;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HealthController {

    @GetMapping("/health")
    public Map<String, String> health() {
        HashMap<String, String> health = new HashMap<String, String>();
        health.put("status", "UP");
        health.put("version", "1.0.1");
        return health;
    }

    @GetMapping("/autor")
    public Map<String, String> autor() {
        HashMap<String, String> autor = new HashMap<String, String>();
        autor.put("Nombre", "Tomás");
        autor.put("Apellido", "Vera");
        return autor;
    }
}
