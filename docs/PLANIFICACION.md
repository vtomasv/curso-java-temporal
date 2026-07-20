# Planificación Detallada: Desarrollo de Aplicaciones con Java y Temporal.io

Este documento presenta la planificación estructurada de 19 clases (4 horas cada una, totalizando 76 horas) para la asignatura de Desarrollo de Aplicaciones. La estructura respeta los contenidos originales requeridos por el programa institucional, pero actualiza el enfoque tecnológico incorporando Java moderno, frameworks web actuales (Spring Boot 3), integración de Inteligencia Artificial y orquestación de flujos de trabajo resilientes mediante Temporal.io.

La planificación está diseñada con una metodología didáctica orientada al uso intensivo de asistentes de Inteligencia Artificial (IA) por parte de los alumnos, tanto en modalidad chat como en modalidad de arnés de código (por ejemplo, Claude Code o Codex).

## Estructura General del Curso

El curso se divide en cinco grandes bloques temáticos, diseñados para construir el conocimiento de forma progresiva. Cada bloque culmina con una evaluación práctica que verifica la asimilación de los conceptos y su aplicación mediante herramientas de IA.

### Bloque 1: Fundamentos de Java Moderno y Ecosistema (Clases 1-4)
Este bloque establece las bases del lenguaje Java en sus versiones más recientes (Java 21), introduciendo la Programación Orientada a Objetos (POO), manejo de excepciones, colecciones y los principios fundamentales de la inyección de dependencias. Se introduce también el uso de herramientas de IA para la generación de código y la depuración.

### Bloque 2: Aplicaciones Web y Persistencia de Datos (Clases 5-8)
En este segmento, los alumnos transicionan desde aplicaciones de consola hacia el desarrollo de aplicaciones web multicapa. Se utiliza Spring Boot como framework principal, cubriendo la creación de APIs REST, el manejo de bases de datos relacionales con Spring Data JPA y la seguridad de aplicaciones web.

### Bloque 3: Resiliencia y Flujos de Trabajo Transaccionales (Clases 9-12)
El tercer bloque aborda el desafío de construir sistemas distribuidos confiables. Se introduce Temporal.io como motor de orquestación de flujos de trabajo (workflows), enseñando a los alumnos cómo diseñar procesos de negocio duraderos, manejar fallos, reintentos y compensaciones.

### Bloque 4: Integración de Inteligencia Artificial y Tecnologías Avanzadas (Clases 13-16)
Este bloque se centra en la integración de capacidades de IA dentro de las aplicaciones desarrolladas. Utilizando Spring AI o LangChain4j, los alumnos aprenderán a conectar sus aplicaciones con modelos de lenguaje (LLMs), implementar generación aumentada por recuperación (RAG) y estructurar salidas de datos.

### Bloque 5: Integración Final y Evaluación (Clases 17-19)
El bloque final consolida todos los conocimientos adquiridos mediante el desarrollo de un proyecto integrador completo. Se incluye la preparación para el examen, la evaluación sumativa final y una sesión dedicada a la retroalimentación y nivelación.

## Desglose Detallado por Clases

A continuación, se detalla el contenido, los objetivos y las actividades prácticas para cada una de las 19 sesiones de 4 horas.

### Clase 1: Introducción a Java Moderno y Entornos Asistidos por IA
**Objetivos:** Configurar el entorno de desarrollo, comprender los principios de Java 21 y aprender a interactuar eficazmente con asistentes de IA para programación.
**Contenidos:**
- Evolución de Java: características modernas (records, pattern matching, virtual threads).
- Configuración del entorno: JDK 21, Maven/Gradle, IDE (IntelliJ/VS Code).
- Principios de Programación Orientada a Objetos (POO) en Java.
- Metodología de trabajo con IA: ingeniería de prompts para código, revisión crítica de sugerencias y uso de herramientas como Claude Code.
**Actividad Práctica:** Desarrollo de una aplicación de consola sencilla utilizando POO, generada iterativamente con asistencia de IA.

### Clase 2: Características Avanzadas y Manejo de Errores
**Objetivos:** Dominar el manejo de excepciones, el registro de eventos (logging) y comprender el ciclo de vida de los objetos.
**Contenidos:**
- Clases internas, anónimas y expresiones lambda.
- Manejo avanzado de excepciones: bloques try-with-resources, excepciones personalizadas.
- Sistemas de logging modernos (SLF4J, Logback) frente a `System.out.println`.
- Conceptos básicos del recolector de basura (Garbage Collector) y gestión de memoria.
**Actividad Práctica:** Refactorización de la aplicación anterior para incluir manejo robusto de errores y trazabilidad completa mediante logs, utilizando la IA para identificar posibles puntos de fallo.

### Clase 3: Depuración, Pruebas y Documentación
**Objetivos:** Aprender a identificar y corregir errores, escribir pruebas unitarias y documentar el código generado.
**Contenidos:**
- Técnicas de depuración (debugging) en entornos modernos.
- Pruebas unitarias con JUnit 5 y aserciones con AssertJ.
- Generación de documentación técnica (JavaDoc moderno).
- Uso de IA para la generación automática de casos de prueba y documentación.
**Actividad Práctica:** Creación de una suite de pruebas unitarias exhaustiva para un módulo de lógica de negocio, evaluando la cobertura de código y depurando errores inducidos intencionalmente.

### Clase 4: Colecciones y Estructuras de Datos
**Objetivos:** Utilizar eficazmente el Collections Framework y la API de Streams para el procesamiento de datos.
**Contenidos:**
- Jerarquía de colecciones en Java (List, Set, Map).
- Procesamiento funcional de datos con Streams API.
- Principios básicos del procesamiento multihilo y concurrencia.
- Comparativa conceptual: Interfaces gráficas de escritorio (Swing) vs. Interfaces web modernas.
**Actividad Práctica:** Implementación de un procesador de datos masivos que filtre, transforme y agregue información utilizando Streams, con optimizaciones sugeridas por IA.

### Clase 5: Introducción a Spring Boot e Inyección de Dependencias
**Objetivos:** Comprender el patrón de Inyección de Dependencias y crear la primera aplicación web con Spring Boot.
**Contenidos:**
- Conceptos de Inversión de Control (IoC) e Inyección de Dependencias (CDI en Jakarta EE / Spring DI).
- Arquitectura multicapa: Controladores, Servicios y Repositorios.
- Inicialización de proyectos con Spring Initializr.
- Creación de endpoints REST básicos.
**Actividad Práctica:** Desarrollo de una API REST fundamental con operaciones CRUD en memoria, delegando la creación del esqueleto del proyecto a la IA.

### Clase 6: Persistencia de Datos con Spring Data JPA
**Objetivos:** Integrar bases de datos relacionales mediante el mapeo objeto-relacional (ORM).
**Contenidos:**
- Introducción a la API de Persistencia de Java (JPA) y Hibernate.
- Configuración de bases de datos (PostgreSQL y H2 para desarrollo).
- Mapeo de entidades, relaciones y ciclos de vida.
- Repositorios de Spring Data y consultas derivadas.
**Actividad Práctica:** Conexión de la API REST a una base de datos PostgreSQL, implementando entidades complejas y consultas personalizadas con la ayuda de la IA para la redacción de JPQL.

### Clase 7: Consultas Avanzadas y Transacciones
**Objetivos:** Dominar técnicas avanzadas de consulta, paginación y la gestión de transacciones.
**Contenidos:**
- Lenguaje de consulta (JPQL) y Criteria API.
- Paginación y ordenamiento de grandes volúmenes de datos.
- Gestión de transacciones declarativas (`@Transactional`).
- Control de acceso concurrente y bloqueos (Optimistic y Pessimistic Locking).
**Actividad Práctica:** Implementación de un motor de búsqueda avanzado con filtros dinámicos y manejo seguro de transacciones concurrentes simuladas.

### Clase 8: Seguridad en Aplicaciones Web
**Objetivos:** Proteger las aplicaciones web implementando autenticación y autorización.
**Contenidos:**
- Principios de seguridad en aplicaciones empresariales.
- Introducción a Spring Security.
- Autenticación basada en tokens (JWT).
- Autorización basada en roles y protección de endpoints.
**Actividad Práctica:** Securización de la API REST desarrollada, requiriendo autenticación JWT para operaciones de escritura, utilizando la IA para auditar posibles vulnerabilidades.

### Clase 9: Introducción a Temporal.io y Ejecución Duradera
**Objetivos:** Comprender los conceptos fundamentales de la orquestación de flujos de trabajo con Temporal.io.
**Contenidos:**
- Desafíos de los sistemas distribuidos y la necesidad de ejecución duradera.
- Arquitectura de Temporal: Temporal Server, Workers, Workflows y Activities.
- Configuración del entorno de desarrollo local con Temporal CLI.
- Anatomía de un Workflow determinista.
**Actividad Práctica:** Creación y ejecución del primer Workflow "Hello World" en Temporal, observando su comportamiento en la interfaz web de Temporal.

### Clase 10: Actividades y Manejo de Fallos en Temporal
**Objetivos:** Implementar lógica de negocio mediante Actividades y configurar políticas de reintento.
**Contenidos:**
- Diferencia entre Workflows (deterministas) y Actividades (con efectos secundarios).
- Registro y ejecución de Actividades.
- Configuración de `ActivityOptions` y tiempos de espera (timeouts).
- Políticas de reintento automático y manejo de excepciones en Temporal.
**Actividad Práctica:** Desarrollo de un flujo que integre llamadas a servicios externos inestables, demostrando cómo Temporal maneja automáticamente los fallos y reintentos.

### Clase 11: Patrones Avanzados en Temporal: Señales y Consultas
**Objetivos:** Interactuar con Workflows en ejecución mediante eventos asíncronos y consultas de estado.
**Contenidos:**
- Envío de eventos externos a Workflows en ejecución (`@SignalMethod`).
- Consulta del estado interno de un Workflow (`@QueryMethod`).
- Espera de condiciones asíncronas (`Workflow.await`).
- Temporizadores duraderos (`Workflow.sleep`).
**Actividad Práctica:** Implementación de un proceso de aprobación de órdenes de compra, donde el Workflow espera señales externas de aprobación o rechazo antes de continuar.

### Clase 12: Orquestación Compleja y Patrón Saga
**Objetivos:** Diseñar transacciones distribuidas complejas y mecanismos de compensación.
**Contenidos:**
- El problema de las transacciones distribuidas en microservicios.
- Implementación del patrón Saga con Temporal.
- Manejo de cancelaciones y ejecución de lógicas de compensación.
- Integración de Temporal con Spring Boot (`temporal-spring-boot-starter`).
**Actividad Práctica:** Desarrollo de un sistema de reservas (vuelos, hotel, coche) que requiera revertir operaciones previas si falla un paso intermedio.

### Clase 13: Integración de IA en Aplicaciones Java (Parte 1)
**Objetivos:** Conectar la aplicación Spring Boot con modelos de lenguaje de IA.
**Contenidos:**
- Introducción a Spring AI y LangChain4j.
- Configuración de clientes de IA y gestión de claves API.
- Generación de texto y estructuración de salidas (JSON).
- Prompts dinámicos y plantillas.
**Actividad Práctica:** Integración de un asistente virtual básico en la aplicación web que responda preguntas basadas en un contexto predefinido.

### Clase 14: Integración de IA en Aplicaciones Java (Parte 2)
**Objetivos:** Implementar técnicas avanzadas de IA, como la Generación Aumentada por Recuperación (RAG).
**Contenidos:**
- Conceptos de embeddings y bases de datos vectoriales.
- Ingesta y procesamiento de documentos.
- Implementación del patrón RAG para respuestas contextualizadas.
- Interacción de la IA con herramientas y funciones locales (Function Calling).
**Actividad Práctica:** Desarrollo de un módulo de análisis de documentos donde la IA pueda extraer información específica y responder preguntas sobre archivos cargados por el usuario.

### Clase 15: Tecnologías de Soporte y Mensajería Asíncrona
**Objetivos:** Comprender la comunicación asíncrona entre servicios y la validación de datos.
**Contenidos:**
- Conceptos de mensajería (JMS, RabbitMQ o Kafka).
- Producción y consumo de mensajes asíncronos.
- Validación avanzada de beans (Bean Validation API).
- Uso de interceptores y programación orientada a aspectos (AOP).
**Actividad Práctica:** Implementación de un sistema de notificaciones asíncronas que valide la estructura de los mensajes antes de procesarlos.

### Clase 16: Proyecto Integrador: Diseño y Arquitectura
**Objetivos:** Diseñar la arquitectura de una aplicación empresarial completa que integre todos los conceptos del curso.
**Contenidos:**
- Definición de requisitos del proyecto integrador.
- Diseño de la arquitectura del sistema, esquemas de base de datos y flujos de Temporal.
- Planificación del desarrollo asistido por IA.
- Configuración inicial del repositorio y entornos.
**Actividad Práctica:** Sesión de diseño arquitectónico y modelado de datos, utilizando herramientas de diagramación (Mermaid) generadas mediante prompts de IA.

### Clase 17: Proyecto Integrador: Implementación
**Objetivos:** Desarrollar e implementar el proyecto integrador combinando Spring Boot, Temporal y capacidades de IA.
**Contenidos:**
- Desarrollo iterativo de las capas de la aplicación.
- Implementación de flujos de trabajo transaccionales.
- Integración de características inteligentes.
- Pruebas de integración y validación de resiliencia.
**Actividad Práctica:** Sesión intensiva de codificación (Hackathon) donde los alumnos implementan sus proyectos con el apoyo continuo del profesor y las herramientas de IA.

### Clase 18: Examen Final y Presentación de Proyectos
**Objetivos:** Evaluar los conocimientos adquiridos mediante la presentación y defensa del proyecto integrador.
**Contenidos:**
- Examen teórico-práctico de la unidad (30% de la calificación).
- Presentación de los proyectos integradores ante el grupo.
- Demostración de resiliencia (simulación de fallos) y características de IA.
**Actividad Práctica:** Evaluación formal y defensa técnica de las decisiones de diseño e implementación adoptadas.

### Clase 19: Examen de Repetición y Retroalimentación
**Objetivos:** Proveer una instancia de recuperación y retroalimentación profunda sobre el desempeño en el curso.
**Contenidos:**
- Aplicación del examen de repetición para alumnos que lo requieran.
- Revisión detallada de los errores comunes encontrados en los proyectos.
- Retroalimentación individualizada sobre el uso de herramientas de IA.
- Cierre del curso y recomendaciones para el desarrollo profesional continuo.
**Actividad Práctica:** Resolución guiada del examen y análisis de código para identificar áreas de mejora.

## Consideraciones Metodológicas para el Uso de IA
Durante todo el curso, se fomentará el uso de la Inteligencia Artificial bajo las siguientes directrices:
- **Comprensión sobre Copiado:** Los alumnos deben ser capaces de explicar línea por línea el código generado por la IA antes de integrarlo en sus proyectos.
- **Ingeniería de Prompts:** Se enseñará a formular peticiones precisas, proporcionando contexto, restricciones y ejemplos de salida deseada.
- **Depuración Asistida:** Se utilizará la IA como un "compañero de depuración", analizando trazas de error y sugiriendo estrategias de resolución, en lugar de pedir únicamente la solución final.
- **Revisión Crítica:** Se inculcará la práctica de revisar críticamente el código generado, identificando posibles problemas de seguridad, rendimiento o deuda técnica introducidos por la IA.
