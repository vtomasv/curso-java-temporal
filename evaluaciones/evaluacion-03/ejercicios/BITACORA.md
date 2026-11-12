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
| | `./mvnw -version` | | |
| | `./mvnw clean verify` | | |

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
- [ ] Ninguna constante de decisión permanece en `PENDIENTE`.
- [ ] Los siete perfiles pasan por separado.
- [ ] El perfil `ev03-all` pasa desde una compilación limpia.
- [ ] Entregué al menos tres pruebas propias activas y deterministas.
- [ ] No desactivé ni eliminé pruebas proporcionadas.
- [ ] No incluí `target/`, secretos, binarios ni archivos del IDE.
- [ ] `DECISIONES.md`, `PROMPTS.md` y esta bitácora están completos.
