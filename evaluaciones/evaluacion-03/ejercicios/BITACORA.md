# Bitácora técnica — Evaluación 3 SICOR

**Estudiante:** <!-- nombre y apellido --><br>
**Equipo / sistema operativo:**<br>
**JDK (`java -version`):**<br>
**Commit inicial:**<br>
**Commit final:**

La bitácora registra evidencia reproducible, no una narración minuto a minuto.
Anote comandos relevantes, resultados, incidentes y decisiones de recuperación.
No pegue contraseñas, tokens ni contenidos confidenciales.

## Verificación inicial

| Fecha/hora | Comando | Resultado | Evidencia o incidencia |
|---|---|---|---|
| | `java -version` | | |
| | `temporal --version` | | |
| | `./mvnw -version` | | |
| | `./mvnw clean verify` | | |

## Evidencia obligatoria de Temporal externo

Esta sección es parte de la evidencia obligatoria de **E01 a E07**. `clean
verify` usa un motor embebido sólo para automatizar pruebas y no reemplaza este
recorrido.
Mantenga el servidor local iniciado mientras ejecuta SICOR:

```bash
temporal server start-dev --ip 127.0.0.1 --ui-port 8233
./mvnw spring-boot:run
```

Use datos ficticios y copie sólo evidencia textual sanitizada. No incluya
contraseñas ni el cuerpo confidencial de las órdenes.

| Evidencia requerida | Workflow ID, comando u observación | Resultado verificable |
|---|---|---|
| Servidor y Web UI | Inicio del servidor; acceso a `http://localhost:8233` | |
| Worker y Task Queue | Worker observado en `SICOR-EV03` | |
| Creación e invariantes | Crear una orden válida y rechazar una inválida desde la interfaz | La válida inicia `despacho-<ordenId>` y `accesos-<ordenId>`; la inválida no genera Workflows |
| Acceso y reautenticación | Leer una orden permitida y una `MAXIMA` tras reingresar credenciales | Updates `registrarAcceso` visibles en `accesos-...`; contraseña y contenido ausentes del History; una lectura posterior al término del despacho sigue registrándose |
| Firmas por Update | Workflow ID `despacho-...`; dos usuarios distintos firman desde la interfaz | Updates visibles, estado `ESPERANDO_FIRMAS` antes del umbral y despacho sólo después de completarlo |
| Activity idempotente y orquestación | Recibos observados después del quórum | Activities ejecutadas por el Worker, claves estables y una entrega por unidad |
| Confirmación por Signal | Workflow ID `despacho-...`; `temporal workflow show --workflow-id <id>` | Signal registrado y Workflow completado |
| Cancelación por Update | Otro Workflow ID; `temporal workflow show --workflow-id <id>` | Update aceptado y Workflow cancelado de forma controlada |
| Query de progreso | Consulta desde la interfaz antes de terminar el Workflow | Conteos coherentes, sin mutar el estado |
| Confidencialidad del History | Inspección de input y eventos en Web UI o CLI | El cuerpo de la orden no aparece en el History |

**Extracto sanitizado del Workflow confirmado por Signal:**

```text
<!-- Workflow ID y nombres de eventos relevantes, sin contenido confidencial. -->
```

**Extracto sanitizado del Workflow cancelado por Update:**

```text
<!-- Workflow ID y nombres de eventos relevantes, sin contenido confidencial. -->
```

## Progreso por misión

| Misión | Opción elegida | Prueba de perfil | Resultado | Commit |
|---|---|---|---|---|
| E01 | | `./mvnw -Pev03-e01 test` | | |
| E02 | | `./mvnw -Pev03-e02 test` | | |
| E03 | | `./mvnw -Pev03-e03 test` | | |
| E04 | | `./mvnw -Pev03-e04 test` | | |
| E05 | | `./mvnw -Pev03-e05 test` | | |
| E06 | | `./mvnw -Pev03-e06 test` | | |
| E07 | | `./mvnw -Pev03-e07 test` | | |

## Entradas de trabajo

Copie esta sección para cada hallazgo significativo.

### Entrada N — EV03-E0X

**Fecha y hora:**<br>
**Hipótesis o cambio:**<br>
**Archivos modificados:**<br>
**Comando ejecutado:**

```text
<!-- comando exacto -->
```

**Resultado observado:**<br>
**Interpretación:**<br>
**Siguiente acción:**

## Pruebas propias

| Decisión | Nombre de prueba | Falla que detectaría | Comando y resultado |
|---|---|---|---|
| | | | |
| | | | |
| | | | |

## Incidentes y recuperación

| Fecha/hora | Incidente | Causa identificada | Acción de recuperación | Estado final |
|---|---|---|---|---|
| | | | | |

## Consulta de la solución guiada

Complete esta sección si abrió `../solucion/SOLUCION_GUIADA.md`. Consultarla no
reduce automáticamente el puntaje; se evalúa cómo transformó la pista en una
decisión propia.

**Sección consultada:** <!-- E0X / no consultada --><br>
**Bloqueo que tenía:**<br>
**Pista utilizada:**<br>
**Adaptación propia realizada:**<br>
**Prueba con que la verifiqué:**

## Validación final

| Fecha/hora | Comando | Resultado | Duración aproximada |
|---|---|---|---|
| | `./mvnw clean verify -Pev03-all` | | |

```text
<!-- Pegue las últimas líneas del resultado final, sin información sensible. -->
```

## Lista de control de entrega

- [ ] La aplicación inicia y la navegación base funciona.
- [ ] Levanté Temporal local antes de iniciar SICOR y accedí a la Web UI.
- [ ] Observé el Worker en la Task Queue `SICOR-EV03`.
- [ ] Observé `despacho-<ordenId>` y `accesos-<ordenId>` para una misma orden.
- [ ] Registré una lectura por Update después del resultado terminal del despacho.
- [ ] Verifiqué en el History que las firmas entraron como Updates antes de las Activities de entrega.
- [ ] Registré un Workflow confirmado por Signal y otro cancelado por Update.
- [ ] Inspeccioné el History y comprobé que no contiene el cuerpo confidencial.
- [ ] Ninguna constante de decisión permanece en `PENDIENTE`.
- [ ] Los siete perfiles pasan por separado.
- [ ] El perfil `ev03-all` pasa desde una compilación limpia.
- [ ] Entregué al menos tres pruebas propias activas y deterministas.
- [ ] No desactivé ni eliminé pruebas proporcionadas.
- [ ] No incluí `target/`, secretos, binarios ni archivos del IDE.
- [ ] `DECISIONES.md`, `PROMPTS.md` y esta bitácora están completos.
