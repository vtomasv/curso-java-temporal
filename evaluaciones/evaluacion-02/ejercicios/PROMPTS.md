# Bitácora de apoyo con IA — Evaluación 02

Registro de la sesión del 15 de octubre de 2026 e instrucciones para continuar.

## Herramienta y alcance

- Herramienta utilizada: Codex, como apoyo para diagnosticar, implementar y verificar cambios por pasos.
- TODO trabajados: EV02-E04 (creación idempotente), EV02-E05 (controlador), EV02-E06 (errores HTTP), EV02-E07 (seguridad), EV02-E08 (Activities), EV02-E09 y EV02-E10 (Workflow), EV02-E11 (pruebas propias).
- Se revisaron el dominio y el mapeo DTO, que ya contenían cambios al inicio de la sesión. No se consultó `solucion/`.
- La aprobación en `SolicitudService.aprobar()` sigue pendiente dentro de EV02-E04.

## Prompt relevante

Solicitudes realizadas durante la sesión:

- «Me ayudas resolviendo esta incidencia en el test SolicitudServiceTest.unaClaveRepetidaDevuelveElResultadoPrevioSinGuardarOtraVez:54 » SolicitudYaExiste Solicitud ya existe: SOL-ANT».
- «Puedes ayudarme a corregir lo detectado en SolicitudControllerTest, vamos paso a paso».
- «Implementa la validación y la indempotencia.».
- «Resolvamos lo de `SolicitudWorkflowTest`».
- «Puedes desarrollar dos pruebas propias, para completar todo».

### Prompt recomendado para continuar: aprobación del servicio

El siguiente prompt es una instrucción propuesta; todavía no se ha ejecutado:

```text
Trabaja únicamente en el pendiente EV02-E04 de SolicitudService.aprobar(String id).
Lee AGENTS.md, README.md, el servicio, el dominio y las pruebas relacionadas.
No consultes solucion/, no agregues dependencias y conserva las firmas públicas.
No modifiques las pruebas entregadas; agrega los casos nuevos como pruebas propias.

Avanza en pasos pequeños y explica cada cambio antes de hacerlo:

1. Agrega una prueba propia para aprobar una solicitud PENDIENTE mediante el
   servicio real, con el repositorio simulado. Debe comprobar el estado de la
   entidad y el DTO devuelto. Ejecútala para observar el fallo actual del TODO.
2. Implementa el cambio mínimo: buscar por id, lanzar SolicitudNoEncontradaException
   si no existe, invocar solicitud.aprobar() y mapear esa misma entidad al DTO.
   Conserva @Transactional y la autorización SUPERVISOR. La entidad recuperada
   por JPA queda administrada dentro de la transacción: no necesita un save adicional.
3. Ejecuta la prueba nueva y ./mvnw -q test.
4. Agrega pruebas propias para un id inexistente y para intentar aprobar dos veces.
   Comprueba las excepciones de dominio correspondientes y ejecuta los tests.
5. Revisa el diff y actualiza esta bitácora con los resultados reales.

No introduzcas E/S, reloj del sistema, UUID aleatorios ni threads en Workflows.
No des por implementada una operación solo porque el test del controlador pasa:
ese test sustituye SolicitudService por un mock.
```

## Verificación propia

Las siguientes verificaciones fueron ejecutadas por Codex durante la sesión;
no representan una ejecución independiente del estudiante.

| Área | Fallo observado | Cambio y comprobación |
| --- | --- | --- |
| Servicio | Una clave repetida lanzaba `SolicitudYaExisteException`. | Devolver el DTO de la solicitud previa sin guardar otra vez; mapear el resultado de `save` para solicitudes nuevas. Pasan las 3 pruebas de `SolicitudServiceTest`. |
| API y seguridad | Faltaba `HttpSecurity`; luego aparecieron bloqueos CSRF y respuestas 401 con `@WithMockUser`. | Habilitar seguridad web, definir autenticación y roles, configurar CSRF solo para `/api/**` y corregir la dependencia de pruebas. Pasan las 3 pruebas de `SolicitudControllerTest` y las 3 de `SecurityConfigTest`. |
| Contrato HTTP | El controlador devolvía respuestas provisionales y los manejadores devolvían 500. | Delegar al servicio, devolver 201 con `Location` y definir `ProblemDetail` para 400, 404 y 409. Los tests entregados comprueban 201, 400 y 404; no comprueban directamente el caso 409. |
| Activity | `registrar()` lanzaba `UnsupportedOperationException`. | Validar datos con fallo `VALIDATION` no reintentable y usar `computeIfAbsent` por clave. Pasan 2 pruebas entregadas y 12 casos adicionales. La idempotencia es en memoria, por instancia. |
| Workflow | Devolvía `TODO` sin invocar Activities. | Registrar y notificar mediante Activities, con 5 s por intento, 20 s totales por Activity, máximo 3 intentos, intervalo inicial de 100 ms y `VALIDATION` excluido de reintentos. Pasan 3 pruebas entregadas y 2 adicionales. |
| Pruebas propias | Había dos métodos vacíos con `@Disabled`. | Reemplazarlos por pruebas de conservación del estado previo ante una clave repetida y propagación de fallos al guardar. Ambas pasan. |

Última ejecución completa antes de actualizar esta bitácora:

```bash
./mvnw -q test
```

Resultado: **35 pruebas, 0 fallos, 0 errores y 0 omitidas**.

Esta ejecución utilizó Java 26.0.1; el curso y el `pom.xml` tienen Java 25 como
objetivo. Antes de entregar, verificar el JDK activo y repetir la suite con Java 25.

### Comandos para retomar el trabajo

Ejecutar desde la carpeta `evaluaciones/evaluacion-02/ejercicios`.
Todos los comandos de pruebas deben incluir el objetivo `test`:

```bash
java --version
./mvnw -v
./mvnw -q -Dtest=SolicitudServiceTest,PruebasPropiasTest test
./mvnw -q -Dtest=SolicitudControllerTest,SecurityConfigTest test
./mvnw -q -Dtest=SolicitudActivitiesTest,SolicitudActivitiesCasosLimiteTest test
./mvnw -q -Dtest=SolicitudWorkflowTest,SolicitudWorkflowCasosLimiteTest test
./mvnw -q test
git diff
git status --short
```

Si se crean nuevas clases de pruebas, incluirlas también en la verificación dirigida.
No se necesita Docker ni un servidor Temporal externo para estas pruebas.

### Pendientes antes del cierre

1. Implementar y probar `SolicitudService.aprobar()` siguiendo el prompt anterior.
2. Revisar los comentarios TODO restantes: `buscarPorId()` ya está implementado,
   pero conserva un comentario provisional. No borrar un TODO sin comprobar su contrato.
3. Revisar si `SolicitudYaExisteException`, agregada previamente, sigue siendo necesaria:
   una clave idempotente repetida debe devolver el resultado previo.
4. Ejecutar la suite con Java 25, revisar el diff y registrar cualquier resultado nuevo.
5. Revisar el código y completar personalmente la reflexión de la sección siguiente.

## Decisión del estudiante

Decisión explícita registrada en el chat: el estudiante autorizó reemplazar
`org.springframework.security:spring-security-test` por
`org.springframework.boot:spring-boot-starter-security-test`, manteniendo alcance
`test`, después de que Codex explicara la integración necesaria para `@WithMockUser`
en Spring Boot 4. Esta fue una excepción autorizada a la regla del README de no
modificar `pom.xml`; no autoriza otros cambios de dependencias.

Reflexión personal pendiente de completar por el estudiante, sin atribuirle
explicaciones o verificaciones que todavía no ha realizado:

- ¿Qué sugerencia acepté, corregí o rechacé y por qué?
- ¿Qué prueba ejecuté personalmente y qué resultado observé?
- ¿Puedo explicar por qué una clave repetida no debe repetir el efecto y por qué
  los fallos `VALIDATION` no se reintentan?
- ¿Puedo explicar qué hace una Activity y por qué el Workflow solo la orquesta?
