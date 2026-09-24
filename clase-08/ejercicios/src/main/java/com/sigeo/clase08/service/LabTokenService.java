package com.sigeo.clase08.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.*;
import java.security.KeyPair;
import java.time.Instant;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class LabTokenService {
    private final KeyPair keys;
    private final String issuer;
    private final String audience;
    private final long ttl;
    public LabTokenService(KeyPair keys, @Value("${lab.jwt.issuer}") String issuer,
            @Value("${lab.jwt.audience}") String audience, @Value("${lab.jwt.ttl-seconds}") long ttl) {
        this.keys = keys; this.issuer = issuer; this.audience = audience; this.ttl = ttl;
    }
    // Sólo el usuario de la sesión decide sub/roles. Nunca se aceptan desde el navegador.
    public String issue(Authentication auth, String scenario) {
        var now = Instant.now();
        var claims = new JWTClaimsSet.Builder()
            .subject(auth.getName())
            .issuer(scenario.equals("issuer") ? "https://otro-emisor.local" : issuer)
            .audience(scenario.equals("audience") ? "otra-api" : audience)
            .issueTime(Date.from(scenario.equals("expired") ? now.minusSeconds(600) : now))
            .notBeforeTime(Date.from(scenario.equals("future") ? now.plusSeconds(120) : now.minusSeconds(600)))
            .expirationTime(Date.from(scenario.equals("expired") ? now.minusSeconds(120) : now.plusSeconds(ttl)))
            .claim("roles", auth.getAuthorities().stream().map(a -> a.getAuthority().replaceFirst("^ROLE_", "")).toList())
            .build();
        var jwt = new SignedJWT(new JWSHeader(JWSAlgorithm.RS256), claims);
        try {
            jwt.sign(new RSASSASigner(keys.getPrivate()));
            return jwt.serialize();
        } catch (JOSEException e) { throw new IllegalStateException("No se pudo emitir el JWT", e); }
    }
}
