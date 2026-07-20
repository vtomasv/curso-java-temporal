# Clase 16: Proyecto Integrador: Diseño y Arquitectura

## Objetivos de la sesión
- Comprender los principios de diseño arquitectónico para aplicaciones distribuidas y escalables.
- Diseñar flujos de trabajo resilientes utilizando Temporal.io.
- Utilizar herramientas de Inteligencia Artificial para la planificación, diseño y validación de arquitecturas de software.
- Estructurar el proyecto integrador final aplicando las mejores prácticas de Spring Boot y microservicios.

## Cronograma propuesto (4 horas)
- **Hora 1:** Introducción al diseño arquitectónico y patrones para el proyecto integrador.
- **Hora 2:** Fundamentos y diseño de flujos de trabajo con Temporal.io.
- **Hora 3:** Planificación de arquitectura asistida por IA (generación de diagramas, validación de decisiones).
- **Hora 4:** Taller práctico: Definición de la arquitectura del proyecto integrador y resolución de dudas.

## Ejercicios prácticos

### Ejercicio 1: Guiado - Diseño de la Arquitectura Base
**Descripción:** En este ejercicio, diseñaremos paso a paso la arquitectura base del proyecto integrador, definiendo los microservicios necesarios, sus responsabilidades y cómo se comunicarán entre sí.

**Pasos:**
1. Identificar los dominios principales del proyecto integrador (ej. Usuarios, Pedidos, Pagos).
2. Definir los microservicios correspondientes a cada dominio.
3. Establecer los mecanismos de comunicación (REST, mensajería asíncrona).
4. Documentar la arquitectura utilizando un diagrama de componentes.

**Asistencia de IA:**
- *Modo Chat:* "Actúa como un arquitecto de software. Tengo un proyecto integrador que es un sistema de e-commerce. Ayúdame a identificar los microservicios principales y sus responsabilidades paso a paso."
- *Claude Code / Codex:* "Genera un diagrama de arquitectura en formato Mermaid para un sistema de e-commerce con microservicios de Usuarios, Catálogo, Pedidos y Pagos, comunicados a través de un API Gateway."

### Ejercicio 2: Semi-guiado - Modelado de un Flujo con Temporal
**Descripción:** Diseña un flujo de trabajo resiliente para el proceso de "Checkout" utilizando los conceptos de Temporal.io (Workflows y Activities).

**Pistas:**
- Recuerda que los Workflows deben ser deterministas.
- Las interacciones con sistemas externos (ej. pasarela de pago, envío de emails) deben ser Activities.
- Define qué sucede si el pago falla (compensación).

**Asistencia de IA:**
- *Modo Chat:* "Estoy diseñando un Workflow de Temporal en Java para un proceso de checkout. ¿Cuáles deberían ser las Activities si necesito cobrar una tarjeta, actualizar el inventario y enviar un email de confirmación? ¿Cómo manejo las fallas en el cobro?"
- *Claude Code / Codex:* "Crea la interfaz de un Workflow de Temporal llamado `CheckoutWorkflow` y la interfaz de sus Activities correspondientes (`PaymentActivity`, `InventoryActivity`, `NotificationActivity`) en Java."

### Ejercicio 3: Desafío - Planificación Completa Asistida por IA
**Descripción:** Utiliza herramientas de IA para generar un documento de diseño técnico (RFC o Architecture Decision Record - ADR) para una nueva funcionalidad compleja del proyecto integrador, por ejemplo, un sistema de recomendaciones en tiempo real. El documento debe incluir contexto, alternativas consideradas, decisión tomada y consecuencias.

**Requisitos:**
- El diseño debe justificar la elección de base de datos, patrones de comunicación y manejo de errores.
- Debes iterar con la IA para refinar el diseño y descubrir posibles cuellos de botella.

**Asistencia de IA:**
- *Modo Chat:* "Quiero escribir un Architecture Decision Record (ADR) para implementar un sistema de recomendaciones en tiempo real en mi proyecto de Spring Boot. Propón 3 alternativas de arquitectura (ej. usando Redis, Kafka, o una base de datos de grafos), analiza sus pros y contras, y ayúdame a redactar el documento final."
- *Claude Code / Codex:* "Genera una plantilla Markdown para un Architecture Decision Record (ADR) y complétala con una propuesta para usar Kafka como bus de eventos en nuestro sistema de recomendaciones."