# Clase 08: laboratorio web de Spring Security, JWT y RBAC

**Duración: 240 minutos. Java 25, Spring Boot 4.1.0 y Maven Wrapper 3.9.11.**

La clase incluye una aplicación funcional y ejercicios de observación, modificación y prueba. E01–E08 viven en **un único módulo**, `clase-08/ejercicios/`; no son ocho carpetas separadas. La web permite iniciar sesión, emitir JWT de laboratorio, verificar su validez en el servidor, comparar roles y practicar CSRF. No se necesita Node, Docker ni un proveedor OAuth externo para arrancar.

## Arranque desde un clon limpio

```bash
cd clase-08/ejercicios
java -version                 # Debe indicar 25
./mvnw clean verify
./mvnw spring-boot:run
```

En Windows: `mvnw.cmd clean verify` y `mvnw.cmd spring-boot:run`.
La primera ejecución descarga Maven y las dependencias desde Maven Central: necesita conexión a Internet. Abre **http://localhost:8080/login**. El servidor escucha en `127.0.0.1`.

| Usuario | Rol | Contraseña del perfil lab |
|---|---|---|
| `lector` | LECTOR | `clase08-demo` |
| `user1` | OPERADOR | `clase08-demo` |
| `user2` | OPERADOR | `clase08-demo` |
| `supervisor` | SUPERVISOR | `clase08-demo` |

Son credenciales públicas de práctica. Se codifican con BCrypt al iniciar. Para reemplazarlas: `export LAB_PASSWORD='elige-una-clave-local'` antes de arrancar (PowerShell: `$env:LAB_PASSWORD='elige-una-clave-local'`). No escribas el valor real en Git. El perfil `lab` es el predeterminado. En otros perfiles `LAB_PASSWORD` es obligatorio.

## Recorrido web de cinco minutos

1. Entra como `user1`. En `/laboratorio`, selecciona **Válido** y pulsa **Emitir JWT**.
2. Pulsa **Validar JWT**. Debes ver HTTP 200, `valid: true`, el usuario y los controles aprobados por el servidor.
3. Pulsa **Alterar firma** y vuelve a validar: HTTP 401. Selecciona cada escenario negativo y repite.
4. Emite un JWT válido. Consulta solicitudes, crea una y edita la 1 (propietario `user1`). Editar la 2 devuelve 403.
5. Cierra sesión, entra como `supervisor`, emite otro JWT y aprueba una solicitud. Consulta de nuevo para ver `APROBADO`.
6. Abre `/formulario`: enviar con token CSRF termina en éxito; enviar sin token muestra HTTP 403.

**200 significa aceptación; 401 indica autenticación ausente o JWT inválido; 403 indica una operación prohibida o fallo CSRF.** Los rechazos provocados por los ejercicios son resultados esperados, no fallos de la aplicación.

## Arquitectura que se observa

| Superficie | Autenticación | CSRF | Estado |
|---|---|---|---|
| `/login`, `/laboratorio`, `/formulario`, `/lab/token` | Formulario y cookie de sesión | Activo, incluido login/logout/emisión | Sesión en servidor |
| `/api/**` | `Authorization: Bearer <JWT>` | Desactivado sólo en esta cadena | Stateless, ignora la sesión |

La cadena API valida RS256, `iss`, `aud`, `exp`, `nbf` y los claims requeridos. Convierte el claim `roles` a autoridades `ROLE_...`. Las rutas no declaradas se deniegan. `@PreAuthorize` en el servicio aplica RBAC y compara el propietario persistido. El DTO sólo permite editar `descripcion`; `id`, `propietario` y `estado` enviados por el cliente no se asignan.

La clase genera una clave RSA efímera al iniciar. `/lab/token` es un **emisor didáctico**, no implementa un Authorization Server OAuth2. Sólo emite para la identidad de la sesión. Los escenarios inválidos están habilitados exclusivamente en `lab`. En una aplicación real se usaría un proveedor de identidad, HTTPS, claves administradas y controles operativos adicionales.

El navegador conserva el JWT sólo en memoria/textarea. Decodificar claims no verifica su autenticidad. Recargar elimina el token de la página. Logout invalida la sesión, **no revoca** JWT emitidos. Expiran en 5 minutos; reiniciar invalida todos al cambiar la clave. H2 en memoria también se reinicia.

## Cronograma (4 horas)

| Minutos | Actividad |
|---|---|
| 00–15 | Arranque, login y modelo de amenazas |
| 15–40 | Autenticación, autorización y cadenas de filtros |
| 40–85 | E01–E03: acceso, usuarios y roles |
| 85–100 | Receso |
| 100–145 | E04: firma y claims JWT |
| 145–185 | E05–E06: propiedad y CSRF |
| 185–220 | E07–E08: secretos y casos de abuso |
| 220–240 | Evidencias, preguntas y tareas |

## Ejercicios con evidencia visible

Para todos: trabaja en `clase-08/ejercicios`, mantén la aplicación en una terminal y ejecuta pruebas en otra. Predice el resultado antes de pulsar un botón. Cambia una regla cada vez y vuelve a probar. La implementación funcional es el punto de partida.

| Ejercicio | Acciones en la web | Resultado y archivo principal |
|---|---|---|
| E01: denegar por defecto | Ruta pública sin JWT y API sin JWT, incluso con sesión abierta | 200 / 401. Ruta desconocida con JWT: 403. `SecurityConfig.java` |
| E02: usuarios y BCrypt | Login correcto y clave incorrecta. Repetir tras definir `LAB_PASSWORD` | Login al laboratorio / error controlado. Hash BCrypt. `SecurityConfig.java` |
| E03: RBAC | Lector consulta y crea; user1 crea y aprueba; supervisor aprueba | Lector 200/403; user1 200/403; supervisor 200. `SolicitudService.java` |
| E04: JWT | Token válido, expirado, issuer/audience incorrectos, nbf futuro y firma alterada | Válido 200; todos los negativos 401. `JwtConfig.java` |
| E05: propiedad | user1 edita 1 y 2; supervisor edita ambas | user1 200/403; supervisor 200/200. `SolicitudService.java` |
| E06: CSRF | Enviar ambos formularios en `/formulario` | Con token: redirección y éxito. Sin token: 403. `formulario.html` |
| E07: secretos | Definir `LAB_PASSWORD`, reiniciar y probar un JWT anterior | Firma anterior rechazada. Completar `SecretScannerReport.md` |
| E08: abuso | Intentar asignar propietario/estado, descripción vacía, ID ajeno y origen CORS no permitido | Campos protegidos no cambian; 400/403 según caso. `security-review.md` |

### Matriz de autorización

| Operación | LECTOR | OPERADOR | SUPERVISOR |
|---|---|---|---|
| Consultar | Sí | Sí | Sí |
| Crear | No | Sí | No |
| Editar | No | Sólo propias | Todas |
| Aprobar (`POST /api/solicitudes/{id}/approve`) | No | No | Sí |

`user1` y `user2` permiten demostrar ownership entre usuarios con el mismo rol. Consultar la lista es una decisión didáctica compartida por los tres roles; la restricción de propiedad se aplica a la edición. Para IDs ausentes, un operador recibe 403 al editar (como para un ID ajeno); el supervisor recibe 404.

### Retos adicionales

- **E09, expiración corta:** cambia `lab.jwt.ttl-seconds` a 10, emite, valida, espera y valida otra vez. Esperado: 200 seguido de 401. La tolerancia del reloj se fija en cero para hacer visible el límite del laboratorio.
- **E10, sesión frente a JWT:** inicia sesión y pulsa API sin JWT (401). Emite un JWT, guárdalo temporalmente, cierra sesión y úsalo desde un cliente HTTP antes de expirar: sigue válido. Borra el token al terminar.
- **E11, firma frente a lectura:** decodifica el payload y altera la firma. Los claims se siguen leyendo, pero el servidor responde 401.
- **E12, mínimo privilegio:** explica por qué supervisor no crea. Propón y prueba una matriz alternativa sin usar `permitAll()` global.

## Pruebas

```bash
./mvnw test -Dtest=SecurityConfigTest
./mvnw test -Dtest=RoleAuthorizationTest,OwnershipSecurityTest
./mvnw test -Dtest=JwtValidationTest,LaboratorioIntegrationTest
./mvnw test -Dtest=CsrfSecurityTest
./mvnw clean verify
bash scripts/check-secrets.sh
```

Las pruebas existentes conservan sus casos de aceptación. Se ajustó su import de MockMvc a Spring Boot 4. La batería adicional usa JWT firmados reales por el decodificador de la aplicación, además de probar login, sesión aislada de API, RBAC, persistencia, propiedad, CSRF, CORS y datos de entrada. La CI `.github/workflows/clase-08.yml` ejecuta Maven con Java 25 y el escáner didáctico.

Para peticiones manuales: `ejercicios/security-tests.http`. Sustituye `@token` por uno recién emitido y no guardes tokens reales en Git.

## PostgreSQL opcional

H2 basta para todos los ejercicios. Para practicar persistencia entre reinicios con PostgreSQL ya disponible:

```bash
export LAB_PASSWORD='elige-una-clave-local'
export DB_PASSWORD='tu-clave-local'
export DB_USER='sigeo'
export DB_URL='jdbc:postgresql://localhost:5432/sigeo'
./mvnw spring-boot:run -Dspring-boot.run.profiles=lab,postgres
```

Los datos iniciales se insertan sólo si la tabla está vacía. En este perfil de práctica se usa `ddl-auto: update`; un despliegue real necesita migraciones y secretos administrados. Las pruebas automatizadas usan H2, no requieren ni verifican PostgreSQL.

## Tareas para el hogar

- **T01, SIGEO seguro (60–90 min):** agregar un permiso nuevo con casos positivos y negativos y explicar su matriz. Entregar aplicación y pruebas.
- **T02, modelo de amenazas (60–90 min):** diagrama de flujo de datos, límites de confianza y 10 amenazas STRIDE con mitigaciones en `docs/threat-model.md`.
- **T03, pruebas negativas (60–90 min):** ampliar `security-tests.http` con evidencia de 400, 401, 403 y 404. Proponer rate limiting sin afirmar que ya está implementado.
- **T04, política de datos (60–90 min):** clasificar logs, payloads y backups, con retención y minimización, en `docs/data-classification.md`.

## Referencias

- [Resource Server JWT, Spring Security](https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/jwt.html)
- [Pruebas MockMvc y Spring Security](https://docs.spring.io/spring-security/reference/servlet/test/mockmvc/setup.html)
- [AutoConfigureMockMvc de Spring Boot 4](https://docs.spring.io/spring-boot/api/java/org/springframework/boot/webmvc/test/autoconfigure/AutoConfigureMockMvc.html)

## Presentación de la clase

[PowerPoint de la clase 08](https://docs.google.com/presentation/d/1ksP8PJrOCoJlbymWwGwmCmtJpOwf6Bke/edit?usp=drivesdk&ouid=103960235133268276671&rtpof=true&sd=true): conceptos, recorrido web, login, validación JWT y ejercicios E01–E12.
