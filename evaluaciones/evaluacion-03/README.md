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

La línea base usa Temporal embebido y puede conectarse opcionalmente al servidor local enseñado en clases mediante `SICOR_TEMPORAL_MODE=external`. No se considera suficiente una entrega que sólo declare interfaces de Workflow: `./mvnw clean verify` ejecuta la integración Spring → WorkflowClient → Worker → Workflow/Activity → JPA.

## Custodia docente

Las pruebas oficiales adicionales, el corrector, la pauta y la matriz deben mantenerse en un repositorio o canal de acceso restringido. La solución guiada sí forma parte de la ayuda autorizada al alumno. El reporte automático es evidencia auxiliar: la nota final requiere revisar las decisiones justificadas, las pruebas propias y la defensa del estudiante.
