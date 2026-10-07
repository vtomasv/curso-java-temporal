package com.sigeo.evaluacion02.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain apiSecurity(HttpSecurity http) throws Exception {
        // TODO(EV02-E07):
        // - /api/** siempre autenticado;
        // - POST /api/solicitudes requiere OPERADOR;
        // - POST /api/solicitudes/*/aprobacion requiere SUPERVISOR;
        // - negar rutas no declaradas;
        // - HTTP Basic y sesión STATELESS;
        // - ignorar CSRF solo para /api/**.
        return http
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    UserDetailsService users(PasswordEncoder encoder) {
        return new InMemoryUserDetailsManager(
                User.withUsername("operador")
                        .password(encoder.encode("ev02-lab"))
                        .roles("OPERADOR")
                        .build(),
                User.withUsername("supervisor")
                        .password(encoder.encode("ev02-lab"))
                        .roles("SUPERVISOR")
                        .build()
        );
    }
}
