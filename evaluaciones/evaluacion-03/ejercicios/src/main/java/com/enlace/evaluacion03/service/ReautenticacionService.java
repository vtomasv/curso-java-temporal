package com.enlace.evaluacion03.service;

import com.enlace.evaluacion03.config.Decisiones;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
public class ReautenticacionService {

    private static final String ATRIBUTO = "sicor.reautorizaciones";
    private static final Duration VENTANA = Duration.ofMinutes(2);

    private final UserDetailsService usuarios;
    private final PasswordEncoder encoder;

    public ReautenticacionService(UserDetailsService usuarios, PasswordEncoder encoder) {
        this.usuarios = usuarios;
        this.encoder = encoder;
    }

    public boolean autenticar(Authentication actual, String usuario, String password,
                              String ordenId, HttpSession session) {
        if (actual == null || !actual.getName().equals(usuario)) return false;
        UserDetails details = usuarios.loadUserByUsername(usuario);
        if (!encoder.matches(password, details.getPassword())) return false;

        Map<String, Instant> permisos = permisos(session);
        switch (Decisiones.REAUTENTICACION) {
            case PENDIENTE, POR_CADA_LECTURA -> permisos.put(clave(usuario, ordenId), Instant.MAX);
            case VENTANA_CORTA_POR_ORDEN -> {
                // TODO(EV03-E03): decida cuándo comienza/termina la ventana y evite
                // que otro usuario u otra orden reutilicen la autorización.
                permisos.put(clave(usuario, ordenId), Instant.now().plus(VENTANA));
            }
        }
        return true;
    }

    public boolean consumir(Authentication actual, String ordenId, HttpSession session) {
        if (actual == null) return false;
        Map<String, Instant> permisos = permisos(session);
        String clave = clave(actual.getName(), ordenId);
        Instant hasta = permisos.get(clave);
        if (hasta == null) return false;

        if (Decisiones.REAUTENTICACION == Decisiones.Reautenticacion.VENTANA_CORTA_POR_ORDEN) {
            if (Instant.now().isAfter(hasta)) {
                permisos.remove(clave);
                return false;
            }
            return true;
        }

        permisos.remove(clave);
        return true;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Instant> permisos(HttpSession session) {
        Object existente = session.getAttribute(ATRIBUTO);
        if (existente instanceof Map<?, ?>) return (Map<String, Instant>) existente;
        Map<String, Instant> nuevo = new HashMap<>();
        session.setAttribute(ATRIBUTO, nuevo);
        return nuevo;
    }

    private String clave(String usuario, String ordenId) {
        return usuario + ":" + ordenId;
    }
}
