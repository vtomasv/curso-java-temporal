# Solución Clase 08: Spring Security y JWT

Este documento explica paso a paso cómo resolver cada uno de los ejercicios de la clase.

## C08-E01 — Denegar por defecto

**Por qué:** En seguridad, el principio de "denegar por defecto" (default deny) asegura que si olvidamos configurar una ruta, esta quedará protegida automáticamente, evitando fugas de información.

**Solución en `SecurityConfig.java`:**
```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/public/**", "/formulario").permitAll()
            .anyRequest().authenticated()
        )
        // ... otras configuraciones
    return http.build();
}
```

## C08-E02 — Usuarios de laboratorio

**Por qué:** Necesitamos usuarios para probar la aplicación, pero no debemos guardar contraseñas en texto plano en el código fuente. Usamos `BCryptPasswordEncoder` y leemos las contraseñas de variables de entorno o properties.

**Solución en `SecurityConfig.java`:**
```java
@Bean
public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
    UserDetails lector = User.builder()
        .username("lector")
        .password(passwordEncoder.encode("password")) // En un caso real, leer de env
        .roles("LECTOR")
        .build();
        
    UserDetails operador = User.builder()
        .username("operador")
        .password(passwordEncoder.encode("password"))
        .roles("OPERADOR")
        .build();
        
    UserDetails supervisor = User.builder()
        .username("supervisor")
        .password(passwordEncoder.encode("password"))
        .roles("SUPERVISOR")
        .build();

    return new InMemoryUserDetailsManager(lector, operador, supervisor);
}
```

## C08-E03 — Roles por operación

**Por qué:** Diferentes operaciones requieren diferentes niveles de privilegio. Usamos `@PreAuthorize` para aplicar estas reglas a nivel de método.

**Solución en `SolicitudController.java`:**
```java
@GetMapping
@PreAuthorize("hasAnyRole('LECTOR', 'OPERADOR', 'SUPERVISOR')")
public List<Solicitud> getAll() {
    return solicitudService.findAll();
}

@PostMapping
@PreAuthorize("hasRole('OPERADOR')")
public Solicitud create(@RequestBody Solicitud solicitud) {
    return solicitudService.create(solicitud);
}

@PostMapping("/{id}/approve")
@PreAuthorize("hasRole('SUPERVISOR')")
public ResponseEntity<Void> approve(@PathVariable Long id) {
    solicitudService.approve(id);
    return ResponseEntity.ok().build();
}
```

## C08-E04 — Resource server local

**Por qué:** Para validar JWTs, configuramos Spring Security como un OAuth2 Resource Server. Esto le dice a Spring que extraiga el token del header `Authorization: Bearer ...` y lo valide.

**Solución en `SecurityConfig.java`:**
```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        // ... authorizeHttpRequests ...
        .oauth2ResourceServer(oauth2 -> oauth2
            .jwt(Customizer.withDefaults())
        );
    return http.build();
}
```
*Nota: Se requiere configurar `spring.security.oauth2.resourceserver.jwt.issuer-uri` en `application.yaml`.*

## C08-E05 — Editar solo lo propio

**Por qué:** El control de acceso basado en roles (RBAC) no es suficiente cuando los usuarios solo deben acceder a sus propios recursos. Esto se conoce como control de acceso basado en atributos (ABAC) o "ownership".

**Solución en `SolicitudService.java`:**
```java
@PreAuthorize("hasRole('SUPERVISOR') or @securityService.isOwner(authentication, #id)")
public Solicitud update(Long id, Solicitud solicitud) {
    // Lógica de actualización
    return solicitud;
}
```
*Nota: Requiere crear un bean `securityService` que verifique si el usuario actual es el propietario de la solicitud con el ID dado.*

## C08-E06 — CSRF y formulario

**Por qué:** CSRF (Cross-Site Request Forgery) es un ataque donde un sitio malicioso engaña al navegador del usuario para que envíe una petición a nuestro sitio. Spring Security protege contra esto por defecto usando tokens sincronizados.

**Solución en `SecurityConfig.java`:**
```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        // ...
        .csrf(csrf -> csrf
            .ignoringRequestMatchers("/api/**") // Deshabilitar CSRF para la API REST (usa JWT)
        );
    return http.build();
}
```
*Nota: Thymeleaf incluye automáticamente el token CSRF en los formularios si se usa `th:action`.*

## C08-E07 — Escáner de secretos

**Por qué:** Los secretos (contraseñas, tokens, claves API) nunca deben subirse al repositorio. Si se suben por accidente, deben ser revocados y eliminados del historial.

**Solución:**
1. Eliminar el secreto del archivo.
2. Usar herramientas como `git filter-repo` o BFG Repo-Cleaner para eliminarlo del historial.
3. Configurar herramientas como `trufflehog` o `git-secrets` en un pre-commit hook o en CI/CD para prevenir futuros commits con secretos.

## C08-E08 — Abuse cases

**Por qué:** Las pruebas de seguridad (abuse cases) verifican que el sistema se comporta correctamente ante entradas maliciosas o inesperadas.

**Solución:**
Documentar en `security-review.md` los hallazgos de las pruebas, por ejemplo:
- **IDOR:** Intentar acceder a `/api/solicitudes/2` siendo el propietario de la solicitud 1.
- **Mass Assignment:** Intentar enviar `{"estado": "APROBADO"}` en la creación de una solicitud.
- **Errores verbosos:** Verificar que las respuestas de error no expongan stack traces.
