# Clase 08: Spring Security y JWT

**Bloque:** Seguridad y Autorización
**Duración:** 4 horas

## Objetivos de Aprendizaje
- Comprender el modelo de amenaza de SIGEO y aplicar principios de seguridad.
- Configurar Spring Security (SecurityFilterChain, PasswordEncoder, gestión de usuarios).
- Diferenciar entre sesión web/CSRF y API token/JWT.
- Implementar OAuth2 Resource Server para validación de JWT (issuer, audience, expiración).
- Aplicar autorización basada en roles y propiedad (ownership) mediante `@PreAuthorize`.
- Configurar CORS, headers de seguridad y prevenir vulnerabilidades comunes.
- Gestionar secretos de forma segura y realizar pruebas de seguridad.

## Cronograma de la Clase

| Minutos | Actividad | Instrucción docente |
|---|---|---|
| 00–10 | Threat modeling rápido | Identificar activos, actores y abusos. |
| 10–35 | Fundamentos y filtro de seguridad | Dibujar autenticación y autorización. |
| 35–60 | Demo SecurityFilterChain | Proteger endpoints y generar usuarios de laboratorio. |
| 60–80 | Ejercicios E01–E03 | 401/403, password y roles. |
| 80–95 | Receso | Preparar tokens de prueba. |
| 95–120 | JWT, CSRF, CORS y ownership | Mostrar ataques simples controlados. |
| 120–160 | Laboratorio E04–E06 | Securizar API y vistas. |
| 160–185 | Desafíos E07–E08 | Auditoría y secretos. |
| 185–195 | Cierre y tarea | Entregar threat model actualizado. |

## Ejercicios de Clase

### C08-E01 — Denegar por defecto
**Especificación:** Configurar rutas públicas mínimas y proteger el resto.
**Criterios de Aceptación:** Cualquier endpoint no declarado explícitamente debe quedar protegido. No usar `permitAll` global.
**Archivos involucrados:** `SecurityConfig.java`
**Comando para verificar:** `./mvnw test -Dtest=SecurityConfigTest#testDefaultDeny`

### C08-E02 — Usuarios de laboratorio
**Especificación:** Crear usuarios en memoria con BCrypt y roles distintos sin contraseñas en texto plano en el código final.
**Criterios de Aceptación:** Passwords codificados; secretos externalizados (usar variables de entorno o properties).
**Archivos involucrados:** `SecurityConfig.java`, `application.yaml`
**Comando para verificar:** `./mvnw test -Dtest=SecurityConfigTest#testLabUsers`

### C08-E03 — Roles por operación
**Especificación:** Configurar autorización: LECTOR consulta, OPERADOR crea y SUPERVISOR aprueba.
**Criterios de Aceptación:** Matriz de permisos completa y consistente.
**Archivos involucrados:** `SecurityConfig.java`, `SolicitudController.java`
**Comando para verificar:** `./mvnw test -Dtest=RoleAuthorizationTest`

### C08-E04 — Resource server local
**Especificación:** Validar JWT de laboratorio, verificando issuer, audience y expiración.
**Criterios de Aceptación:** Rechaza tokens con firma, audience o expiración inválidos.
**Archivos involucrados:** `SecurityConfig.java`, `application.yaml`
**Comando para verificar:** `./mvnw test -Dtest=JwtValidationTest`

### C08-E05 — Editar solo lo propio
**Especificación:** Además del rol, verificar que el solicitante edite su propia solicitud (salvo el supervisor que puede editar cualquiera).
**Criterios de Aceptación:** No confiar solo en el ID enviado por el cliente. Usar `@PreAuthorize` o lógica en el servicio.
**Archivos involucrados:** `SolicitudService.java`, `MethodSecurityConfig.java`
**Comando para verificar:** `./mvnw test -Dtest=OwnershipSecurityTest`

### C08-E06 — CSRF y formulario
**Especificación:** Proteger un formulario Thymeleaf con CSRF y demostrar el fallo si no se envía el token.
**Criterios de Aceptación:** Token CSRF presente en el formulario; sesión y logout correctos.
**Archivos involucrados:** `SecurityConfig.java`, `templates/formulario.html`, `WebController.java`
**Comando para verificar:** `./mvnw test -Dtest=CsrfSecurityTest`

### C08-E07 — Escáner de secretos
**Especificación:** Eliminar una clave accidental del repositorio, rotar el valor simulado y agregar prevención.
**Criterios de Aceptación:** El secreto no permanece en los archivos actuales; el plan considera el historial de Git.
**Archivos involucrados:** `application.yaml`, `SecretScannerReport.md` (crear)
**Comando para verificar:** Revisión manual del reporte.

### C08-E08 — Abuse cases
**Especificación:** Ejecutar un checklist de pruebas: IDOR, mass assignment, errores verbosos, CORS y logs.
**Criterios de Aceptación:** Cada hallazgo tiene severidad, evidencia y corrección documentada.
**Archivos involucrados:** `security-review.md` (crear)
**Comando para verificar:** Revisión manual del documento.

## Tareas para el Hogar

### C08-T01 — SIGEO seguro
**Especificación:** Aplicar JWT/roles/ownership a toda la API y sesión segura a Thymeleaf.
**Entregable y Aceptación:** Aplicación y 25 security tests. Matriz de acceso demostrable; secretos fuera del repo.

### C08-T02 — Threat model formal
**Especificación:** Crear DFD simple, trust boundaries y 10 amenazas STRIDE con mitigaciones.
**Entregable y Aceptación:** `docs/threat-model.md`. Amenazas vinculadas a componentes reales.

### C08-T03 — Prueba negativa
**Especificación:** Construir colección de requests maliciosas y resultados esperados.
**Entregable y Aceptación:** `security-tests.http` o colección Postman. Incluye 401, 403, 404 anti-enumeración, 400 y rate-limit conceptual.

### C08-T04 — Política de datos
**Especificación:** Clasificar datos del sistema y definir qué puede ir en logs, payloads y backups.
**Entregable y Aceptación:** `docs/data-classification.md`. Incluye retención y minimización.

## Cómo ejecutar

Para correr los tests de la clase:
```bash
./mvnw test
```

Para levantar la aplicación con perfil H2 (por defecto):
```bash
./mvnw spring-boot:run
```

Para levantar la aplicación con PostgreSQL:
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=postgres
```
