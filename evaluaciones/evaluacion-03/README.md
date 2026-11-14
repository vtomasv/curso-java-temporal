# Evaluación 3 — SICOR

Esta carpeta reúne la tercera evaluación práctica del curso. El escenario, las unidades y los usuarios son completamente ficticios y se usan solo con fines educativos.

## Estructura

| Recurso | Contenido | Distribución |
|---|---|---|
| `ejercicios/` | Aplicación Spring–Temporal funcional con cliente, Worker y Task Queue; siete misiones, pruebas públicas y archivos de decisiones/evidencias que completa el estudiante. | **Entregar al alumno.** |
| `solucion/` | Solución guiada con pistas progresivas para destrabar misiones; exige registrar la consulta y adaptar la idea. | **Entregar al alumno** como ayuda autorizada. |
| `01_Prueba_Practica_SICOR_Estudiantes.docx` | Enunciado formal de aplicación. | **Entregar al alumno.** |
| Paquete docente separado | Corrector, pauta docente, matriz de revisores y copia oficial del starter. | **Confidencial docente.** No publicar en esta rama. |

## Paquete para estudiantes

Distribuir `ejercicios/`, `solucion/SOLUCION_GUIADA.md` y `01_Prueba_Practica_SICOR_Estudiantes.docx`. El corrector, la pauta docente y la matriz se entregan por separado al responsable y al comité.

Para ejecutar SICOR, el estudiante debe levantar el servidor Temporal local enseñado en clases con `temporal server start-dev --ip 127.0.0.1 --ui-port 8233`. La aplicación se conecta obligatoriamente a `127.0.0.1:7233` y falla de forma explícita si el servidor no está disponible. El motor embebido se conserva sólo dentro de `src/test` para que `./mvnw clean verify` sea reproducible; no constituye evidencia de ejecución de la aplicación. La entrega debe incluir en `BITACORA.md` Workflows observados en la Web UI o mediante la CLI, con Signal, Query, Update, Task Queue e History.

## Custodia docente

Las pruebas oficiales adicionales, el corrector, la pauta y la matriz deben mantenerse en un repositorio o canal de acceso restringido. La solución guiada sí forma parte de la ayuda autorizada al alumno. El reporte automático es evidencia auxiliar: la nota final requiere revisar las decisiones justificadas, las pruebas propias y la defensa del estudiante.
