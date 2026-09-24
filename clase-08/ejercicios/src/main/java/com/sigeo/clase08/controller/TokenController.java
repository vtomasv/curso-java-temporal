package com.sigeo.clase08.controller;

import com.sigeo.clase08.service.LabTokenService;
import java.util.Map;
import java.util.Set;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class TokenController {
    private final LabTokenService tokens;
    private final Environment environment;
    public TokenController(LabTokenService tokens, Environment environment) {
        this.tokens = tokens; this.environment = environment;
    }
    @PostMapping("/lab/token")
    public Map<String, String> token(Authentication auth, @RequestParam(defaultValue="valid") String scenario) {
        if (!Set.of("valid", "expired", "issuer", "audience", "future").contains(scenario))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        if (!scenario.equals("valid") && !environment.acceptsProfiles(Profiles.of("lab")))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        return Map.of("token", tokens.issue(auth, scenario), "scenario", scenario);
    }
    // Si este método se ejecuta, el filtro ya verificó firma, emisor, audiencia y tiempos.
    @GetMapping("/api/jwt/validar")
    public Map<String, Object> validate(@AuthenticationPrincipal Jwt jwt, Authentication auth) {
        return Map.of("valid", true, "subject", jwt.getSubject(), "issuer", jwt.getIssuer().toString(),
            "audience", jwt.getAudience(), "expiresAt", jwt.getExpiresAt(), "notBefore", jwt.getNotBefore(),
            "algorithm", jwt.getHeaders().get("alg"), "authorities", auth.getAuthorities(),
            "checks", Map.of("signature", true, "issuer", true, "audience", true, "expiration", true, "notBefore", true));
    }
    @GetMapping("/api/public/info")
    public Map<String, String> info() { return Map.of("clase", "08", "modo", "laboratorio didáctico"); }
}
