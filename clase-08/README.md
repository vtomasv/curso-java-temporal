# Clase 08: Seguridad en Aplicaciones Web

## Objetivos de la sesión
- Comprender los fundamentos de la seguridad en aplicaciones web y la arquitectura de Spring Security.
- Implementar autenticación y autorización sin estado utilizando JSON Web Tokens (JWT).
- Configurar el control de acceso basado en roles (RBAC) para proteger endpoints específicos.
- Integrar los conocimientos adquiridos para la Evaluación Sumativa 1.

## Cronograma propuesto (4 horas)
- **0:00 - 1:00:** Introducción a Spring Security, conceptos de autenticación vs autorización, y fundamentos de JWT.
- **1:00 - 2:00:** Ejercicio Guiado: Configuración inicial de Spring Security y generación de tokens JWT.
- **2:00 - 2:45:** Ejercicio Semi-guiado: Implementación de filtros de seguridad para validación de tokens.
- **2:45 - 3:15:** Ejercicio Desafío: Implementación de roles y protección de rutas.
- **3:15 - 4:00:** Evaluación Sumativa 1 (Aplicación práctica integradora).

## Ejercicios prácticos

### Ejercicio 1: Guiado - Configuración básica de Spring Security y Login
**Descripción:** En este ejercicio, configuraremos Spring Security en un proyecto existente y crearemos un endpoint de login que devuelva un token JWT básico tras validar las credenciales.

**Pasos a seguir:**
1. Añadir las dependencias de `spring-boot-starter-security` y la librería `jjwt` en el archivo `pom.xml`.
2. Crear una clase de configuración `SecurityConfig` anotada con `@Configuration` y `@EnableWebSecurity`.
3. Configurar el `SecurityFilterChain` para deshabilitar CSRF, establecer la política de creación de sesiones como `STATELESS` y permitir el acceso público al endpoint `/api/auth/login`.
4. Crear un `AuthController` con un método POST que reciba credenciales, las valide y retorne un token JWT generado.

**Asistencia de IA:**
- *Prompt para modo Chat (ChatGPT/Claude):* "Actúa como un profesor de Spring Boot. Explícame paso a paso cómo configurar `SecurityFilterChain` en Spring Boot 3 para una API REST sin estado (stateless) permitiendo acceso público solo a `/api/auth/login`."
- *Prompt para IDE/Agente (Claude Code/Codex):* "Añade las dependencias necesarias para Spring Security y JWT en el pom.xml. Luego, crea la clase SecurityConfig básica con la configuración stateless."

### Ejercicio 2: Semi-guiado - Filtro de validación JWT
**Descripción:** Implementar un filtro personalizado que intercepte las peticiones HTTP, extraiga el token JWT del header `Authorization` y valide su firma antes de permitir el acceso a los controladores protegidos.

**Pistas para resolverlo:**
- Necesitarás crear una clase que extienda de `OncePerRequestFilter`.
- Sobrescribe el método `doFilterInternal`.
- Recuerda extraer el token del header leyendo la cabecera `Authorization` y quitando el prefijo "Bearer ".
- Usa la librería de JWT para validar el token. Si es válido, crea un objeto `UsernamePasswordAuthenticationToken` y establécelo en el `SecurityContextHolder`.
- No olvides registrar este filtro en tu `SecurityConfig` antes del `UsernamePasswordAuthenticationFilter`.

**Asistencia de IA:**
- *Prompt para modo Chat (ChatGPT/Claude):* "Tengo que crear un filtro JWT extendiendo `OncePerRequestFilter` en Spring Boot. ¿Puedes darme la estructura básica del método `doFilterInternal` y explicarme cómo extraer el token del header Authorization correctamente?"
- *Prompt para IDE/Agente (Claude Code/Codex):* "Genera la clase JwtAuthenticationFilter que valide el token JWT usando la clave secreta definida en application.properties. Asegúrate de actualizar el SecurityContext si el token es válido."

### Ejercicio 3: Desafío - Control de acceso basado en roles (RBAC)
**Descripción:** Modificar la aplicación para soportar múltiples roles (por ejemplo, `ROLE_USER` y `ROLE_ADMIN`). El token JWT debe incluir el rol del usuario, y la aplicación debe restringir el acceso a ciertos endpoints según este rol.

**Requisitos:**
- Modificar la lógica de generación del JWT para incluir un *claim* personalizado con el rol del usuario.
- Actualizar el filtro JWT para leer el rol desde el token y asignarlo a la lista de `GrantedAuthority` del usuario autenticado.
- Habilitar la seguridad a nivel de métodos usando `@EnableMethodSecurity`.
- Crear dos endpoints de prueba: uno accesible por cualquier usuario autenticado y otro exclusivo para administradores usando `@PreAuthorize("hasRole('ADMIN')")`.

**Asistencia de IA:**
- *Prompt para modo Chat (ChatGPT/Claude):* "Quiero implementar control de acceso basado en roles (RBAC) con JWT en Spring Boot 3. ¿Cómo puedo incluir el rol del usuario como un claim en el token y luego leerlo en mi filtro para asignarlo a las autoridades de Spring Security?"
- *Prompt para IDE/Agente (Claude Code/Codex):* "Modifica el servicio de generación de JWT para incluir el rol del usuario. Luego, habilita la seguridad por métodos y crea un endpoint `/api/admin/dashboard` protegido para que solo los usuarios con rol ADMIN puedan acceder."