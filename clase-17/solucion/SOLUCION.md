# Clase 17 — Resolución paso a paso

Esta guía explica una forma correcta y didáctica de resolver la clase centrada en **Proyecto integrador: implementación**. Debe leerse después de intentar los ejercicios de la carpeta `ejercicios/`, idealmente ejecutando pruebas y revisando cada cambio con apoyo de IA.

## Paso 0. Preparación

Antes de comenzar, revisa el `README.md` de la clase, ejecuta el proyecto base y verifica que el entorno compile. Si la clase usa Spring Boot, ejecuta `mvn -q test` o `mvn -q compile` para validar dependencias. Si usa Temporal, asegúrate además de poder levantar un entorno local o al menos de contar con `temporal-testing` para pruebas en memoria.

## Paso 1. Cerrar CRUD y dominio

Completa primero las piezas básicas de datos y casos de uso sin mezclar aún la orquestación.

La decisión didáctica aquí es mantener la solución incremental: primero se establece el contrato, luego la implementación y finalmente la verificación con pruebas. Esto permite que el estudiante use la IA como asistente de diseño y depuración, no como sustituto de comprensión.

## Paso 2. Implementar el workflow principal

Modela el flujo de negocio, sus compensaciones y su estado observable desde queries.

La decisión didáctica aquí es mantener la solución incremental: primero se establece el contrato, luego la implementación y finalmente la verificación con pruebas. Esto permite que el estudiante use la IA como asistente de diseño y depuración, no como sustituto de comprensión.

## Paso 3. Agregar cancelación y pruebas

Incorpora señales de cancelación y valida el comportamiento esperado con tests de Temporal.

La decisión didáctica aquí es mantener la solución incremental: primero se establece el contrato, luego la implementación y finalmente la verificación con pruebas. Esto permite que el estudiante use la IA como asistente de diseño y depuración, no como sustituto de comprensión.

## Errores frecuentes

| Error | Efecto | Cómo corregirlo |
|---|---|---|
| Intentar resolver todo en una sola clase | Diseño inmantenible | Separar controller, service, workflow y activities |
| No probar compensaciones | La resiliencia queda solo declarada | Forzar fallos y verificar el rollback |

## Cómo usar la IA en este ejercicio

Un buen uso de la IA en esta clase consiste en pedir **explicaciones justificadas**, revisiones de diseño y ayuda de depuración sobre fragmentos pequeños. Tres prompts útiles son los siguientes:

> "Estoy resolviendo la clase y quiero implementar esta parte sin perder la arquitectura. Te comparto mi código actual y el objetivo. Propón el siguiente cambio mínimo y explícame por qué es mejor que dos alternativas."

> "Este test falla con el siguiente error. Antes de darme un fix, enumera las tres causas más probables y cómo verificar cada una desde Java/Maven."

> "Revisa este código como si fueras un profesor de desarrollo de software: identifica problemas de diseño, de legibilidad y de pruebas, y proponme un plan de mejora en tres pasos."
