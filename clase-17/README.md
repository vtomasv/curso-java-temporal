# Clase 17: Proyecto Integrador: Implementación

## Objetivos de la sesión
* Integrar todos los conocimientos adquiridos durante el curso (Java, Spring Boot, JPA, Seguridad, Testing) en un proyecto completo.
* Desarrollar una aplicación backend robusta desde cero simulando un entorno de desarrollo ágil (formato Hackathon).
* Tomar decisiones de arquitectura y diseño de software frente a requerimientos abiertos.
* Preparar el proyecto para su presentación y posible despliegue en producción.

## Cronograma propuesto (4 horas)
* **0:00 - 0:30:** Presentación del Hackathon, definición de requerimientos y setup inicial.
* **0:30 - 1:30:** Diseño de la arquitectura, modelado de la base de datos y creación de entidades.
* **1:30 - 3:00:** Desarrollo intensivo (APIs REST, lógica de negocio, Spring Security).
* **3:00 - 3:30:** Testing, corrección de errores (bug fixing) y refinamiento.
* **3:30 - 4:00:** Presentación de proyectos (Demos), feedback y cierre del curso.

## Ejercicios prácticos

Al ser una sesión de Hackathon, los ejercicios representan las fases de construcción de tu Proyecto Integrador (por ejemplo, un sistema de gestión de reservas, un e-commerce o un clon de una red social).

### Ejercicio 1: Guiado - Configuración base y esqueleto del proyecto
**Descripción:** Inicia el proyecto configurando las dependencias necesarias, la conexión a la base de datos y la estructura de paquetes (controladores, servicios, repositorios, modelos).
**Pasos:**
1. Ve a Spring Initializr y genera un proyecto con Web, JPA, H2/PostgreSQL, Validation, Lombok y Security.
2. Configura el archivo `application.properties` o `application.yml` para la base de datos.
3. Crea la estructura de paquetes recomendada (`com.tuapp.controller`, `com.tuapp.service`, etc.).
4. Crea un endpoint de prueba (`/api/health`) que retorne un estado 200 OK para verificar que todo funciona.

**Asistencia de IA:**
* *Modo Chat:* "Actúa como un Tech Lead de Java. Voy a empezar mi proyecto final en Spring Boot. ¿Qué dependencias de Spring Initializr me recomiendas para un sistema de gestión de reservas y cómo debería estructurar mis paquetes?"
* *Claude Code / Codex:* "Genera la estructura de paquetes estándar para una API REST en Spring Boot y crea un `HealthController` básico que responda 'OK' en la ruta `/api/health`."

### Ejercicio 2: Semi-guiado - Implementación del Core y Seguridad
**Descripción:** Desarrolla las entidades principales, sus relaciones, la lógica de negocio y protege los endpoints utilizando Spring Security y JWT.
**Pistas:**
* Comienza por las entidades JPA y asegúrate de definir correctamente las relaciones (`@OneToMany`, `@ManyToOne`).
* Crea los repositorios y luego los servicios implementando la lógica principal (ej. crear una reserva validando disponibilidad).
* Configura Spring Security: crea un filtro para validar el token JWT y protege las rutas para que solo usuarios autenticados puedan acceder a ciertos endpoints.

**Asistencia de IA:**
* *Modo Chat:* "Tengo las entidades `Usuario` y `Reserva`. ¿Puedes ayudarme a escribir el método en el servicio que cree una reserva asegurándose de que no haya solapamiento de fechas? Dame solo la lógica, yo la adaptaré."
* *Claude Code / Codex:* "Revisa mis clases de configuración de Spring Security. Necesito implementar un filtro JWT. Genera el código para `JwtAuthenticationFilter` que extraiga el token del header 'Authorization' y valide la firma."

### Ejercicio 3: Desafío - Funcionalidades avanzadas y Despliegue
**Descripción:** Agrega valor a tu proyecto implementando características complejas que lo destaquen, y prepáralo para producción.
**Reto:**
* Implementa paginación y filtrado avanzado en tus listados (ej. buscar reservas por fecha y estado usando `Specification` o `@Query`).
* Añade manejo global de excepciones (`@ControllerAdvice`) devolviendo un formato de error estandarizado (Problem Details).
* Escribe pruebas de integración para tu flujo principal usando `@SpringBootTest` y Testcontainers.
* (Opcional) Crea un `Dockerfile` para contenerizar tu aplicación.

**Asistencia de IA:**
* *Modo Chat:* "Quiero implementar un manejo de errores global en mi API REST usando `@ControllerAdvice` que cumpla con el estándar RFC 7807 (Problem Details). ¿Cómo puedo estructurarlo en Spring Boot 3?"
* *Claude Code / Codex:* "Analiza mi controlador principal. Genera pruebas de integración usando MockMvc y Testcontainers para validar el flujo completo de creación de un recurso, incluyendo casos de error por validación de datos."