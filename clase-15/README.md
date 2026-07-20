# Clase 15: Tecnologías de Soporte y Mensajería Asíncrona

## Objetivos de la sesión
* Comprender los conceptos fundamentales de la mensajería asíncrona y su aplicación en arquitecturas distribuidas.
* Implementar productores y consumidores de mensajes utilizando tecnologías como RabbitMQ o Kafka con Spring Boot.
* Aplicar validaciones robustas en modelos y controladores utilizando Bean Validation (JSR 380).
* Entender y aplicar la Programación Orientada a Aspectos (AOP) y los interceptores en Spring para separar preocupaciones transversales (cross-cutting concerns) como logging, auditoría o seguridad.

## Cronograma propuesto (4 horas)
* **Hora 1: Mensajería Asíncrona (Teoría y Configuración)**
  * Conceptos de mensajería: Colas, Tópicos, Productores, Consumidores.
  * Introducción a RabbitMQ / Kafka.
  * Configuración inicial en Spring Boot (Spring AMQP / Spring Kafka).
* **Hora 2: Implementación de Mensajería y Bean Validation**
  * Envío y recepción de mensajes asíncronos.
  * Introducción a Bean Validation (`@Valid`, `@NotNull`, `@Size`, etc.).
  * Manejo de excepciones de validación (`MethodArgumentNotValidException`).
* **Hora 3: Programación Orientada a Aspectos (AOP) e Interceptores**
  * Conceptos de AOP: Aspect, Advice, Pointcut, JoinPoint.
  * Implementación de Aspectos con `@Aspect` en Spring.
  * Diferencias entre AOP e Interceptores (`HandlerInterceptor`).
* **Hora 4: Práctica Integradora y Resolución de Dudas**
  * Desarrollo de los ejercicios prácticos.
  * Revisión de código y buenas prácticas.
  * Cierre de la sesión.

## Ejercicios prácticos

### Ejercicio 1: Guiado - Validación de Datos y Logging con Interceptores
**Descripción:** Crear una API REST simple para registrar usuarios. Se debe validar que los datos de entrada sean correctos usando Bean Validation y registrar cada petición HTTP utilizando un Interceptor de Spring.

**Pasos:**
1. Crea un proyecto Spring Boot con las dependencias `Spring Web` y `Validation`.
2. Crea un DTO `UsuarioRegistroDTO` con campos: `nombre` (no nulo, min 2 caracteres), `email` (formato válido) y `password` (min 8 caracteres).
3. Crea un `UsuarioController` con un endpoint POST `/api/usuarios` que reciba el DTO validado (`@Valid`).
4. Implementa un manejador global de excepciones (`@ControllerAdvice`) para devolver un 400 Bad Request con los errores de validación.
5. Crea una clase que implemente `HandlerInterceptor` para hacer un log (System.out o SLF4J) antes y después de cada petición al controlador.
6. Registra el interceptor en una clase de configuración que implemente `WebMvcConfigurer`.

**Asistencia de IA:**
* *Modo Chat (ChatGPT/Claude):* "Actúa como un experto en Spring Boot. Explícame paso a paso cómo configurar un `HandlerInterceptor` para registrar el tiempo de ejecución de las peticiones HTTP y cómo registrarlo en `WebMvcConfigurer`."
* *Claude Code / Cursor:* "Genera un `UsuarioRegistroDTO` con Bean Validation para nombre, email y password. Luego, crea un `@RestControllerAdvice` que capture `MethodArgumentNotValidException` y devuelva un mapa con los campos que fallaron y sus mensajes de error."

### Ejercicio 2: Semi-guiado - Auditoría con AOP (Programación Orientada a Aspectos)
**Descripción:** Implementar un sistema de auditoría que registre automáticamente la ejecución de ciertos métodos de servicio utilizando AOP, sin modificar la lógica de negocio.

**Pistas:**
* Necesitarás la dependencia `spring-boot-starter-aop`.
* Crea una anotación personalizada `@Auditable`.
* Crea una clase aspecto (`@Aspect` y `@Component`).
* Define un `@Around` o `@Before` pointcut que intercepte cualquier método anotado con `@Auditable`.
* En el aspecto, obtén el nombre del método y los argumentos usando el `JoinPoint`.

**Asistencia de IA:**
* *Modo Chat (ChatGPT/Claude):* "Tengo problemas entendiendo los Pointcuts en Spring AOP. ¿Puedes darme ejemplos de cómo interceptar métodos basados en una anotación personalizada vs basados en el paquete donde se encuentran?"
* *Claude Code / Cursor:* "Crea una anotación `@Auditable` y un Aspecto en Spring Boot que intercepte los métodos con esta anotación. El aspecto debe imprimir en consola el nombre del método ejecutado y los valores de sus parámetros."

### Ejercicio 3: Desafío - Sistema de Notificaciones Asíncronas
**Descripción:** Diseñar e implementar un sistema donde la creación de un pedido (Order) dispare un evento asíncrono para enviar una notificación (simulada) al usuario, utilizando un broker de mensajería (RabbitMQ o Kafka).

**Requisitos:**
* Configurar un broker de mensajería (puedes usar Docker Compose para levantar RabbitMQ o Kafka localmente).
* Crear un `PedidoController` que reciba una petición de creación de pedido, lo guarde (simulado) y envíe un mensaje a una cola/tópico.
* El mensaje debe contener el ID del pedido y el email del cliente.
* Crear un servicio consumidor (`@RabbitListener` o `@KafkaListener`) que escuche la cola/tópico y simule el envío de un correo electrónico (con un `Thread.sleep` para simular latencia).
* Validar los datos de entrada del pedido usando Bean Validation.
* Usar AOP para medir el tiempo que tarda el consumidor en procesar el mensaje.

**Asistencia de IA:**
* *Modo Chat (ChatGPT/Claude):* "Quiero implementar RabbitMQ en Spring Boot para un sistema de notificaciones. ¿Me puedes dar un archivo `docker-compose.yml` para levantar RabbitMQ y explicarme cómo configurar el `RabbitTemplate` y las colas en Spring?"
* *Claude Code / Cursor:* "Implementa un productor y un consumidor de RabbitMQ en Spring Boot. El productor debe enviar un objeto `NotificacionEvento` (serializado en JSON) a un exchange 'notificaciones.exchange'. El consumidor debe leer de la cola 'notificaciones.queue' y procesar el evento. Incluye la configuración necesaria."