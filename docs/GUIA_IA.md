# Guía de trabajo con Inteligencia Artificial

En este curso la IA no es una herramienta prohibida: es parte del método. Un ingeniero de software moderno trabaja con asistentes de IA a diario, y saber usarlos bien es una competencia evaluada. Esta guía define **cómo** usarlos para aprender de verdad.

## 1. Dos modalidades de trabajo

### Modalidad chat (ChatGPT, Claude, Gemini)
Conversas con el modelo, copias y pegas código y errores. Es ideal para **entender conceptos**, revisar diseño, analizar errores y generar fragmentos pequeños.

### Modalidad agente (Claude Code, Codex CLI)
El agente opera directamente sobre tu repositorio: lee archivos, edita código, ejecuta tests. Es ideal para **tareas de implementación acotadas** con criterios de aceptación claros. Este repositorio incluye `AGENTS.md` y `CLAUDE.md` para que el agente conozca el contexto del curso automáticamente.

## 2. El ciclo de trabajo recomendado

Para cada ejercicio sigue este ciclo, que replica el flujo profesional:

1. **Lee el enunciado completo** y los criterios de aceptación (tests incluidos).
2. **Escribe tu plan en 3-5 líneas** antes de tocar la IA: qué clases crearás, qué hará cada una.
3. **Contrasta tu plan con la IA**: "Este es mi plan para X, ¿qué riesgos o alternativas ves?".
4. **Implementa por incrementos pequeños**, tú o el agente, ejecutando `mvn test` tras cada paso.
5. **Pide explicación de todo lo que no entiendas**: "explícame línea por línea este bloque".
6. **Revisa el diff antes de cada commit**: `git diff` es tu control de calidad final.
7. **Cierra con una autoevaluación**: ¿podrías reescribir esto sin IA? Si no, vuelve al paso 5.

## 3. Anatomía de un buen prompt técnico

Un prompt eficaz para desarrollo tiene cinco partes:

```text
[ROL/CONTEXTO] Estoy en un proyecto Java 21 con Spring Boot 3.3 y Maven.
[OBJETIVO] Necesito implementar un endpoint REST GET /productos con paginación.
[RESTRICCIONES] Usa Spring Data JPA, la entidad Producto ya existe, no agregues dependencias nuevas.
[MATERIAL] (pega aquí el código relevante o el error completo, incluido el stack trace)
[SALIDA ESPERADA] Dame solo la clase Controller y explica las decisiones en 3 puntos.
```

Errores comunes que debes evitar:

| Anti-patrón | Por qué falla | Corrección |
|---|---|---|
| "no funciona, arréglalo" | Sin contexto ni error | Pega el stack trace completo y el código |
| Pedir todo el ejercicio de una vez | Código gigante que no entiendes | Divide en pasos y valida cada uno |
| Aceptar la primera respuesta | La IA se equivoca con frecuencia | Verifica con tests y pide alternativas |
| No indicar versiones | La IA mezcla APIs antiguas (javax vs jakarta) | Indica siempre Java 21 / Spring Boot 3 / Temporal SDK |

## 4. Prompts tipo por actividad

| Actividad | Prompt sugerido |
|---|---|
| Entender concepto | "Explícame [concepto] como si tuviera experiencia en [lo que ya sé], con un ejemplo en Java 21 y un anti-ejemplo." |
| Diseñar | "Te doy los requisitos: [...]. Proponme 2 diseños alternativos con sus trade-offs. No escribas código todavía." |
| Depurar | "Este test falla con este stack trace: [...]. Antes de proponer un fix, dime las 3 causas más probables y cómo verificar cada una." |
| Revisar código | "Actúa como revisor senior: encuentra problemas de seguridad, rendimiento y legibilidad en este código: [...]" |
| Generar tests | "Genera tests JUnit 5 para esta clase cubriendo casos borde: entrada nula, lista vacía, valores límite: [...]" |
| Estudiar para quiz | "Hazme 5 preguntas de opción múltiple sobre [tema de la clase] y corrige mis respuestas explicando." |

## 5. Uso de agentes (Claude Code / Codex) en los ejercicios

Cuando el ejercicio esté marcado como **apto para agente**, el flujo recomendado es:

```bash
cd clase-NN/ejercicios
claude   # o codex
```

Y una instrucción inicial del estilo:

> Lee el README.md de esta carpeta. Trabaja el Ejercicio 2: implementa lo pedido en pasos pequeños, ejecutando `mvn test` después de cada cambio. Detente y explícame cada paso antes de continuar con el siguiente.

Reglas obligatorias con agentes:

1. Trabaja siempre en una rama: `git checkout -b clase-NN-ejercicio-M`.
2. Exige al agente ejecutar los tests después de cada cambio.
3. Revisa el diff completo (`git diff`) antes de aceptar y hacer commit.
4. Si el agente hace algo que no entiendes, detenlo y pide explicación.

## 6. Qué se evalúa respecto al uso de IA

En las evaluaciones y defensa del proyecto se valorará que puedas: explicar cualquier línea de tu código, justificar decisiones de diseño frente a alternativas, reproducir en pizarra la estructura general de la solución, y mostrar tu historial de prompts cuando se solicite. **Entregar código que no puedas defender se considera falta grave**, independientemente de si lo escribió una IA.
