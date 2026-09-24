# E08: revisión de seguridad

Los resultados esperados son contratos de la implementación, no una auditoría de producción. Completar la evidencia con usuario, petición, estado HTTP y cuerpo observado, sin incluir JWT ni contraseñas.

| Caso | Severidad si fallara | Resultado esperado | Evidencia del alumno |
|---|---|---|---|
| JWT alterado, expirado, issuer/audience inválidos, nbf futuro | Alta | 401 | Completar |
| Operador edita solicitud ajena (IDOR) | Alta | 403, sin modificación | Completar |
| Cliente asigna propietario/estado | Alta | No se asignan, propietario autenticado y PENDIENTE | Completar |
| LECTOR crea o OPERADOR aprueba | Alta | 403 | Completar |
| Formulario sin CSRF | Alta | 403 | Completar |
| Sesión web usada como autenticación de API | Alta | 401 sin Bearer | Completar |
| JSON inválido o descripción vacía | Media | 400 sin stack trace en respuesta | Completar |
| Preflight de origen no permitido | Media | 403 | Completar |
| Logout con JWT anterior | Diseño | JWT sigue válido hasta expirar | Completar |

## Límites y mejoras propuestas

- El login no incluye rate limiting ni MFA. Proponer un diseño y pruebas antes de exponerlo fuera del laboratorio.
- Las claves y sesiones son locales a una instancia. No hay revocación individual de tokens ni refresh tokens.
- Los datos de H2 son efímeros. PostgreSQL es una práctica opcional, sin prueba de integración automatizada aquí.
- No registrar contraseñas ni tokens. Para producción, definir auditoría de eventos y monitorización.
- El escáner didáctico cubre pocos patrones. Complementar con una herramienta especializada en un flujo real.
