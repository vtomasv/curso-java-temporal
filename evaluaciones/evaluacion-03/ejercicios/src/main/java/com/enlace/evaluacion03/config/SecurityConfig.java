package com.enlace.evaluacion03.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserDetailsService userDetailsService(PasswordEncoder encoder) {
        String password = encoder.encode("laboratorio");
        return new InMemoryUserDetailsManager(
                User.withUsername("operador").password(password).roles("OPERADOR").build(),
                User.withUsername("oficial1").password(password).roles("OFICIAL").build(),
                User.withUsername("oficial2").password(password).roles("OFICIAL").build(),
                User.withUsername("comandante1").password(password).roles("COMANDANTE").build(),
                User.withUsername("comandante2").password(password).roles("COMANDANTE").build(),
                User.withUsername("auditor").password(password).roles("AUDITOR").build()
        );
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/app.css", "/login").permitAll()
                        .requestMatchers("/ordenes/nueva").hasAnyRole("OPERADOR", "COMANDANTE")
                        .requestMatchers("/ordenes/*/firmar").hasAnyRole("OFICIAL", "COMANDANTE")
                        .requestMatchers("/ordenes/*/despachar").hasAnyRole("OPERADOR", "COMANDANTE")
                        .requestMatchers("/ordenes/*/confirmar").hasAnyRole("OFICIAL", "COMANDANTE")
                        .requestMatchers("/ordenes/*/cancelar").hasRole("COMANDANTE")
                        .anyRequest().authenticated())
                .formLogin(form -> form.defaultSuccessUrl("/", true))
                .logout(logout -> logout.logoutSuccessUrl("/login?logout"))
                .build();
    }
}
