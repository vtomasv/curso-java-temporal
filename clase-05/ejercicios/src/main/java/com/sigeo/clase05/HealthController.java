package com.sigeo.clase05;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HealthController {

    @GetMapping("/health")
    public Map<String, String> health() {
        // TODO(C05-E01): Implementar endpoint /api/health que retorne un JSON con "status": "UP" y "version": "1.0.0"
        throw new UnsupportedOperationException("TODO C05-E01");
    }
}
