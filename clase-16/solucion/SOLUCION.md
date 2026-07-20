# Clase 16 — Resolución paso a paso

Esta guía explica una forma correcta y didáctica de resolver la clase centrada en **Proyecto integrador: diseño y arquitectura**. Debe leerse después de intentar los ejercicios de la carpeta `ejercicios/`, idealmente ejecutando pruebas y revisando cada cambio con apoyo de IA.

## Paso 0. Preparación

Antes de comenzar, revisa el `README.md` de la clase, ejecuta el proyecto base y verifica que el entorno compile. Si la clase usa Spring Boot, ejecuta `mvn -q test` o `mvn -q compile` para validar dependencias. Si usa Temporal, asegúrate además de poder levantar un entorno local o al menos de contar con `temporal-testing` para pruebas en memoria.

## Paso 1. Aterrizar el problema

Describe actores, objetivos, restricciones y eventos clave antes de pensar en clases o endpoints.

La decisión didáctica aquí es mantener la solución incremental: primero se establece el contrato, luego la implementación y finalmente la verificación con pruebas. Esto permite que el estudiante use la IA como asistente de diseño y depuración, no como sustituto de comprensión.

## Paso 2. Definir arquitectura y datos

Construye diagramas Mermaid coherentes con una solución multicapa y con flujos Temporal identificables.

La decisión didáctica aquí es mantener la solución incremental: primero se establece el contrato, luego la implementación y finalmente la verificación con pruebas. Esto permite que el estudiante use la IA como asistente de diseño y depuración, no como sustituto de comprensión.

## Paso 3. Planificar iteraciones asistidas por IA

Divide el proyecto en incrementos pequeños que puedan validarse con pruebas y revisiones.

La decisión didáctica aquí es mantener la solución incremental: primero se establece el contrato, luego la implementación y finalmente la verificación con pruebas. Esto permite que el estudiante use la IA como asistente de diseño y depuración, no como sustituto de comprensión.

## Errores frecuentes

| Error | Efecto | Cómo corregirlo |
|---|---|---|
| Empezar a programar sin diagrama | Se pierde visión de sistema | Diseñar primero contratos y componentes |
| No definir criterios de aceptación | La IA produce entregables ambiguos | Fijar alcance verificable por iteración |

## Cómo usar la IA en este ejercicio

Un buen uso de la IA en esta clase consiste en pedir **explicaciones justificadas**, revisiones de diseño y ayuda de depuración sobre fragmentos pequeños. Tres prompts útiles son los siguientes:

> "Estoy resolviendo la clase y quiero implementar esta parte sin perder la arquitectura. Te comparto mi código actual y el objetivo. Propón el siguiente cambio mínimo y explícame por qué es mejor que dos alternativas."

> "Este test falla con el siguiente error. Antes de darme un fix, enumera las tres causas más probables y cómo verificar cada una desde Java/Maven."

> "Revisa este código como si fueras un profesor de desarrollo de software: identifica problemas de diseño, de legibilidad y de pruebas, y proponme un plan de mejora en tres pasos."
