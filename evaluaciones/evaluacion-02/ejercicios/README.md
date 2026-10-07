# Evaluación 02: circuito durable de solicitudes SIGEO

Starter de la segunda prueba práctica. El proyecto compila, pero varias pruebas fallan porque los métodos contienen implementaciones provisionales. Los `TODO(EV02-Exx)` y las pruebas públicas indican una ruta de avance.

## Reglas de trabajo

1. No modifique las pruebas entregadas ni el `pom.xml`.
2. Mantenga los paquetes, nombres y firmas públicas.
3. Ejecute una prueba pequeña después de cada cambio.
4. No necesita Docker, PostgreSQL ni un servidor Temporal: las pruebas usan H2 y `TestWorkflowEnvironment`.
5. Agregue al menos dos pruebas propias en `PruebasPropiasTest.java`.
6. Complete `PROMPTS.md`, incluso si no utilizó IA.
7. Entregue solo código que pueda explicar y modificar durante una defensa breve.

## Preparación

```bash
java --version            # debe indicar Java 25
./mvnw -v
./mvnw -q test
```

En Windows use `mvnw.cmd` en lugar de `./mvnw`.

## Ruta sugerida por pruebas

### Paso 1 — Dominio y persistencia

Complete invariantes y transiciones; luego verifique los contratos JPA entregados.

```bash
./mvnw -q -Dtest=SolicitudDominioTest test
./mvnw -q -Dtest=SolicitudRepositoryTest test
```

### Paso 2 — Servicio transaccional

Implemente EV02-E03 y EV02-E04: mapeo DTO, creación idempotente, búsqueda y aprobación. No use `new` para construir el repositorio.

```bash
./mvnw -q -Dtest=SolicitudServiceTest test
```

### Paso 3 — Contrato REST y errores

Implemente EV02-E05 y EV02-E06 respetando DTOs, códigos HTTP y Problem Details.

```bash
./mvnw -q -Dtest=SolicitudControllerTest test
```

### Paso 4 — Autorización

En EV02-E07, la consulta requiere autenticación; crear requiere `OPERADOR`; aprobar requiere `SUPERVISOR`.

```bash
./mvnw -q -Dtest=SecurityConfigTest test
```

### Paso 5 — Activity idempotente y fallos tipados

En EV02-E08, la clave de idempotencia evita repetir el efecto. Los datos inválidos generan `ApplicationFailure` de tipo `VALIDATION` no reintentable.

```bash
./mvnw -q -Dtest=SolicitudActivitiesTest test
```

### Paso 6 — Workflow determinista y resiliente

Implemente EV02-E09 y EV02-E10: configure timeouts y reintentos explícitos; todo efecto externo debe quedar en la Activity.

```bash
./mvnw -q -Dtest=SolicitudWorkflowTest test
```

### Paso 7 — Evidencias y cierre

```bash
./mvnw -q test
git diff
git status
```

## Uso recomendado de Codex o Copilot

Trabaje un TODO y un test por vez. Ejemplo:

> Revisa solo el TODO EV02-E08 y la prueba SolicitudActivitiesTest. Explícame primero el contrato de idempotencia, propón el cambio mínimo y ejecuta únicamente esa prueba. No modifiques tests ni dependencias; muéstrame el diff y detente.

La herramienta puede sugerir, explicar y revisar. El estudiante conserva la responsabilidad de ejecutar, comprobar, registrar decisiones y poder defender el código.
