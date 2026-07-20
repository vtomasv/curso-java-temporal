# Rúbricas de evaluación

Este documento define los criterios de evaluación de las instancias sumativas del curso, alineados con el Procedimiento Interno de Evaluación (PIE) y la matriz de evaluación de la unidad de aprendizaje.

## Instancias y ponderaciones

| Instancia | Momento | Ponderación | Modalidad |
|---|---|---|---|
| Evaluación sumativa 1 | Clase 8 | 20% | Práctica (API REST segura con persistencia) |
| Evaluación sumativa 2 | Clase 12 | 20% | Práctica (workflow resiliente con Temporal) |
| Tercera instancia | Clase 17 | 30% | Proyecto integrador individual |
| Examen de unidad | Clase 18 | 30% | Individual (teórico-práctico + defensa) |

La evaluación formativa es permanente: quiz de 15 minutos al inicio de cada clase sobre la materia anterior, y revisión de avances de los laboratorios.

## Rúbrica de trabajos prácticos (sumativas 1 y 2)

| Criterio | Peso | Insuficiente (1-3) | Suficiente (4-5) | Bueno (6) | Excelente (7) |
|---|---|---|---|---|---|
| Funcionalidad | 30% | No compila o la mayoría de tests falla | Compila y pasan más del 60% de los tests | Pasan todos los tests obligatorios | Pasan todos los tests, incluidos los opcionales |
| Diseño y POO | 25% | Código monolítico, sin capas ni encapsulación | Capas identificables con acoplamientos puntuales | Capas claras, responsabilidades bien asignadas | Uso justificado de patrones y abstracciones |
| Calidad de código | 20% | Sin convenciones, nombres crípticos, duplicación | Legible con inconsistencias menores | Limpio, consistente, con logging adecuado | Además, documentado (JavaDoc) y sin code smells |
| Manejo de errores y resiliencia | 15% | Excepciones ignoradas o genéricas | Manejo básico de errores | Errores tipificados, validación de entradas | Estrategia completa (retries, timeouts, compensación) |
| Defensa y comprensión | 10% | No puede explicar su código | Explica el flujo general | Explica cada componente y decisión | Justifica alternativas y trade-offs |

## Rúbrica del proyecto integrador (tercera instancia)

| Criterio | Peso | Descripción del nivel máximo |
|---|---|---|
| Arquitectura multicapa | 20% | Separación clara de capas web, servicio, persistencia y workflows, con diagrama coherente con el código |
| Resiliencia con Temporal | 25% | Al menos un workflow con activities, retries configurados, una señal o query, y compensación (Saga) demostrable ante fallos simulados |
| Persistencia y datos | 15% | Modelo relacional normalizado, migraciones, consultas eficientes y transacciones correctas |
| Seguridad | 10% | Autenticación y autorización operativas; secretos fuera del código |
| Integración de IA | 15% | Funcionalidad de IA con valor real para el caso de uso, con manejo de errores del proveedor |
| Pruebas | 10% | Tests unitarios y de integración significativos; workflow probado con `TestWorkflowEnvironment` |
| Uso profesional de IA | 5% | Historial de prompts/commits que evidencia trabajo iterativo y comprensión |

## Evaluación del uso de Inteligencia Artificial

El uso de IA está permitido y fomentado en todas las instancias prácticas. Lo que se evalúa es la **comprensión demostrable**: en cualquier defensa, el profesor puede pedir explicar una línea, modificar un comportamiento en vivo o justificar una decisión frente a una alternativa. La incapacidad reiterada de explicar el propio código invalida el criterio de defensa y puede invalidar la entrega completa según el PIE.

## Quiz formativo tipo (15 minutos)

Cinco preguntas por clase: tres conceptuales de opción múltiple, una de lectura de código ("¿qué imprime?") y una breve de aplicación ("escribe la anotación necesaria para..."). Los quizzes no llevan nota sumativa, pero su tendencia se usa como evidencia de progreso y para orientar la retroalimentación de la clase 19.
