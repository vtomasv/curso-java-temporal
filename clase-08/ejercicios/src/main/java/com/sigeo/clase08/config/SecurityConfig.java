package com.sigeo.clase08.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // TODO(C08-E01): Configurar rutas públicas mínimas (/public/**) y proteger el resto.
        // TODO(C08-E04): Configurar OAuth2 Resource Server para validar JWT.
        // TODO(C08-E06): Configurar CSRF para proteger formularios web, pero permitir API REST.
        throw new UnsupportedOperationException("TODO C08-E01, C08-E04, C08-E06");
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        // TODO(C08-E02): Crear usuarios en memoria con BCrypt y roles distintos (LECTOR, OPERADOR, SUPERVISOR).
        // No usar contraseñas en texto plano.
        throw new UnsupportedOperationException("TODO C08-E02");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
