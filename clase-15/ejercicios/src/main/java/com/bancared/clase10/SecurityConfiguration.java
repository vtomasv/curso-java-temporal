package com.bancared.clase10;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
@Configuration
public class SecurityConfiguration {
    @Bean UserDetailsService usuarios(@Value("${banking.role}")String role,@Value("${banking.service-password}")String servicePassword){
        // Credenciales públicas para datos sintéticos locales.
        if("bank".equals(role))return new InMemoryUserDetailsManager(User.withUsername("intermediario").password("{noop}"+servicePassword).roles("SERVICE").build());
        return new InMemoryUserDetailsManager(User.withUsername("docente").password("{noop}laboratorio").roles("DOCENTE").build(),User.withUsername("ana").password("{noop}laboratorio").roles("CLIENTE").build(),User.withUsername("bruno").password("{noop}laboratorio").roles("CLIENTE").build());
    }
    @Bean SecurityFilterChain seguridad(HttpSecurity http,@Value("${banking.role}")String role)throws Exception{
        if("bank".equals(role))return http.csrf(c->c.disable()).sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).authorizeHttpRequests(a->a.anyRequest().hasRole("SERVICE")).httpBasic(Customizer.withDefaults()).build();
        return http.authorizeHttpRequests(a->a.requestMatchers("/app.css","/app.js","/extras.js","/login","/error").permitAll().requestMatchers("/api/lab/**").hasRole("DOCENTE").anyRequest().authenticated()).formLogin(Customizer.withDefaults()).httpBasic(Customizer.withDefaults()).build();
    }
}
