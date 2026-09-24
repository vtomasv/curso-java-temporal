# Guía docente de la clase 08

La implementación funcional está en `../ejercicios/`. Los ejercicios E01–E08 se realizan sobre el mismo módulo y tienen instrucciones y resultados esperados en [README](../README.md).

## E01 y E02: acceso e identidad

`SecurityConfig` define dos cadenas ordenadas. `/api/**` es stateless y acepta JWT Bearer. Las vistas usan login por formulario, sesión y CSRF. Las rutas no declaradas se deniegan. `UserDetailsService` crea cuatro usuarios con hashes BCrypt. El perfil `lab` usa una clave pública de práctica y permite reemplazarla con `LAB_PASSWORD`.

## E03: roles

Los métodos de `SolicitudService` usan `@PreAuthorize`. LECTOR consulta; OPERADOR crea; SUPERVISOR aprueba. El convertidor de JWT transforma el claim `roles` en autoridades `ROLE_...`. Supervisor no hereda automáticamente los permisos de operador: la matriz lo indica explícitamente.

## E04: JWT

`JwtConfig` genera una clave RSA efímera y configura `NimbusJwtDecoder`. Además de firma RS256, comprueba emisor, audiencia, expiración, nbf y presencia de claims obligatorios. `LabTokenService` firma tokens para la identidad de la sesión. Los escenarios negativos de `/lab/token` sólo están disponibles en perfil lab. Este emisor es didáctico, no es un Authorization Server OAuth2.

`GET /api/jwt/validar` sólo se ejecuta después de que el filtro acepta el JWT. La página muestra su respuesta HTTP, separada de la decodificación local de claims. Un token leído en el navegador todavía puede tener una firma inválida.

## E05: propiedad y persistencia

`SolicitudRepository` guarda los recursos en H2. `isOwner` consulta el propietario persistido. Editar requiere OPERADOR propietario o SUPERVISOR. El DTO sólo recibe descripción, de modo que propietario y estado enviados por el cliente no se asignan. La creación fija el propietario autenticado y PENDIENTE; aprobar es una operación separada.

## E06: CSRF

La cadena web mantiene CSRF en login, logout, formulario y emisión de JWT. La plantilla muestra un formulario con `th:action` (token automático) y otro con `action` (sin token, rechazo 403). La cadena API puede omitir CSRF porque no acepta la cookie de sesión como autenticación.

## E07 y E08: revisión guiada

El escáner `scripts/check-secrets.sh` es una demostración limitada. La clave RSA no se almacena y cambia al reiniciar. Los reportes `SecretScannerReport.md` y `security-review.md` contienen campos para la evidencia del alumno. No se debe reescribir el historial compartido ni introducir secretos reales durante la práctica.

`LaboratorioIntegrationTest` verifica JWT firmados reales, login, roles, propiedad, CSRF, CORS, validación de entrada y persistencia. `security-tests.http` permite repetir los casos manualmente. La guía distingue rechazos esperados (400/401/403/404) de errores de la aplicación.

## Retos E09–E12

Acortar expiración, comparar sesión frente a token, alterar la firma sin cambiar claims y proponer otra matriz de mínimo privilegio. Cambiar una regla cada vez y ejecutar `./mvnw test` para discutir el resultado.

## Límites del laboratorio

H2 y la clave RSA se regeneran al iniciar. Logout no revoca tokens. No hay MFA, refresh tokens, revocación individual ni rate limiting. PostgreSQL es opcional y no forma parte de la batería automatizada. Para producción se necesitan un proveedor de identidad, HTTPS, gestión de claves, migraciones y controles operativos.
