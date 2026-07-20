# Clase 05: Introducción a Spring Boot e Inyección de Dependencias

## Objetivos de la sesión
- Comprender los conceptos de Inversión de Control (IoC) e Inyección de Dependencias (DI).
- Inicializar y configurar un proyecto con Spring Boot 3.
- Entender la estructura básica de una aplicación Spring Boot.
- Crear una API REST básica utilizando controladores, servicios y repositorios simulados.
- Familiarizarse con las anotaciones principales de Spring (`@Component`, `@Service`, `@RestController`, `@Autowired`).

## Cronograma propuesto (4 horas)
- **Hora 1:** Teoría de IoC y DI. Introducción a Spring Boot 3 y Spring Initializr.
- **Hora 2:** Creación del primer proyecto. Exploración de la estructura y configuración (`application.properties`).
- **Hora 3:** Desarrollo de una API REST básica (Controlador y Servicio). Ejercicio guiado.
- **Hora 4:** Ejercicios prácticos (Semi-guiado y Desafío), resolución de dudas y cierre.

## Ejercicios prácticos

### Ejercicio 1: Guiado - Mi Primera API REST con Spring Boot
**Objetivo:** Crear un proyecto Spring Boot desde cero y exponer un endpoint GET simple.

**Pasos:**
1. Ve a [Spring Initializr](https://start.spring.io/).
2. Configura el proyecto: Maven, Java 17+, Spring Boot 3.x.
3. Añade la dependencia: **Spring Web**.
4. Genera, descarga y abre el proyecto en tu IDE.
5. Crea un paquete `controllers` y dentro una clase `HelloController`.
6. Anota la clase con `@RestController`.
7. Crea un método que retorne un `String` y anótalo con `@GetMapping("/hello")`.
8. Ejecuta la aplicación y prueba el endpoint en tu navegador o Postman (`http://localhost:8080/hello`).

**Asistencia de IA:**
- *Modo Chat:* "Actúa como un tutor de Spring Boot. Explícame paso a paso cómo crear un controlador REST básico que devuelva un saludo, y qué hace exactamente la anotación @RestController."
- *Claude Code / Codex:* "Genera una clase `HelloController` en el paquete `com.ejemplo.demo.controllers` con un endpoint GET en la ruta `/api/saludo` que retorne '¡Hola, Spring Boot 3!'."

### Ejercicio 2: Semi-guiado - Inyección de Dependencias en Acción
**Objetivo:** Separar la lógica de negocio del controlador utilizando un Servicio e inyectarlo.

**Pistas:**
- Crea una clase `GreetingService` en un paquete `services`.
- Anota el servicio con `@Service`.
- Mueve la lógica del saludo a un método dentro de este servicio.
- En tu `HelloController`, declara una variable de tipo `GreetingService`.
- Utiliza inyección por constructor para inyectar el servicio en el controlador (recomendado sobre `@Autowired` en campos).
- Llama al método del servicio desde tu endpoint.

**Asistencia de IA:**
- *Modo Chat:* "Tengo un controlador REST en Spring Boot pero quiero mover la lógica a un servicio. ¿Cómo creo un servicio y lo inyecto en mi controlador usando inyección por constructor?"
- *Claude Code / Codex:* "Refactoriza el `HelloController` para que utilice un `GreetingService` inyectado por constructor. Crea la clase `GreetingService` con un método `getGreeting()`."

### Ejercicio 3: Desafío - API REST de Gestión de Tareas (CRUD Básico en Memoria)
**Objetivo:** Crear una API REST completa para gestionar una lista de tareas (To-Do) utilizando una lista en memoria.

**Requisitos:**
- Crea un modelo `Task` con `id`, `title`, `description` y `completed`.
- Crea un `TaskService` que maneje una `List<Task>` interna.
- Implementa métodos en el servicio para: obtener todas las tareas, obtener una por ID, crear una nueva tarea, actualizar una existente y eliminarla.
- Crea un `TaskController` que exponga los endpoints correspondientes (`GET`, `POST`, `PUT`, `DELETE`).
- Asegúrate de usar los códigos de estado HTTP correctos (ej. 201 Created, 404 Not Found).

**Asistencia de IA:**
- *Modo Chat:* "Quiero construir un CRUD en memoria para una entidad 'Task' en Spring Boot. ¿Puedes darme la estructura de las clases (Modelo, Servicio, Controlador) y explicarme qué anotaciones HTTP usar para cada operación?"
- *Claude Code / Codex:* "Implementa un CRUD completo en memoria para la entidad `Task` (id, title, completed). Crea el modelo, el servicio con una lista interna y el controlador REST con los endpoints GET, POST, PUT y DELETE. Maneja el caso de tarea no encontrada devolviendo un 404."