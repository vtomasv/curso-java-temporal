# Clase 12: Orquestación Compleja y Patrón Saga

## Objetivos de la sesión
* Comprender los desafíos de las transacciones distribuidas en arquitecturas de microservicios.
* Aprender e implementar el Patrón Saga (Coreografía y Orquestación) para mantener la consistencia de los datos.
* Diseñar y ejecutar transacciones de compensación ante fallos en procesos distribuidos.
* Integrar herramientas y frameworks en Spring Boot para la orquestación de servicios.
* **Nota:** Esta sesión incluye la Evaluación Sumativa 2.

## Cronograma propuesto (4 horas)
* **00:00 - 00:45:** Teoría: Transacciones distribuidas, Teorema CAP y el Patrón Saga (Coreografía vs. Orquestación).
* **00:45 - 01:30:** Ejercicio 1 (Guiado): Implementación de una Saga basada en Coreografía con eventos (Spring Boot + Kafka/RabbitMQ).
* **01:30 - 01:45:** Descanso.
* **01:45 - 02:30:** Ejercicio 2 (Semi-guiado): Orquestación centralizada y manejo de transacciones de compensación.
* **02:30 - 03:15:** Ejercicio 3 (Desafío): Diseño de un flujo complejo de e-commerce con múltiples servicios y fallos simulados.
* **03:15 - 04:00:** Evaluación Sumativa 2 (Prueba práctica/teórica sobre microservicios, mensajería y Saga).

## Ejercicios prácticos

### Ejercicio 1: Guiado - Saga basada en Coreografía (Paso a paso)
**Descripción:** Implementaremos un flujo simple de creación de pedidos donde el servicio de `OrderService` emite un evento, y el `InventoryService` lo escucha para reservar stock. Si falla, emite un evento de fallo para que `OrderService` cancele el pedido.

**Pasos:**
1. Crear dos microservicios en Spring Boot: `order-service` e `inventory-service`.
2. Configurar Spring Cloud Stream o Kafka Template para la emisión de eventos.
3. En `order-service`, crear un endpoint POST para iniciar un pedido en estado `PENDING` y emitir el evento `OrderCreated`.
4. En `inventory-service`, escuchar `OrderCreated`. Si hay stock, emitir `InventoryReserved`. Si no, emitir `InventoryFailed`.
5. En `order-service`, escuchar la respuesta y actualizar el estado del pedido a `APPROVED` o `CANCELLED`.

**Asistencia de IA:**
* *Prompt para Chat:* "Actúa como un experto en Spring Boot. Muéstrame paso a paso cómo configurar Spring Cloud Stream con Kafka para emitir un evento `OrderCreated` desde un servicio y escucharlo en otro."
* *Prompt para Claude Code/Codex:* "Genera la configuración de `application.yml` y las clases productora y consumidora en Spring Boot para un sistema de mensajería pub/sub usando Kafka."

### Ejercicio 2: Semi-guiado - Orquestación con un Coordinador Central
**Descripción:** Cambiaremos el enfoque a Orquestación. Crearemos un `SagaOrchestrator` que coordine las llamadas REST o eventos a `PaymentService` e `InventoryService`.

**Pistas:**
* Usa un patrón de máquina de estados (State Machine) o un servicio orquestador dedicado.
* El orquestador debe llamar a `PaymentService`. Si el pago es exitoso, llama a `InventoryService`.
* Si `InventoryService` falla, el orquestador debe ejecutar explícitamente una llamada de compensación a `PaymentService` para reembolsar el dinero.

**Asistencia de IA:**
* *Prompt para Chat:* "Tengo un orquestador en Spring Boot que llama a dos servicios externos vía REST. ¿Cómo puedo implementar un mecanismo de compensación robusto si la segunda llamada falla, asegurando que la primera acción se revierta?"
* *Prompt para Claude Code/Codex:* "Crea un servicio `OrderOrchestrator` en Java que use `RestTemplate` o `WebClient` para llamar a `/pay` y `/reserve`. Si `/reserve` devuelve error 500, implementa el bloque catch que llame a `/refund`."

### Ejercicio 3: Desafío - Flujo Complejo de E-commerce con Fallos Simulados
**Descripción:** Diseña e implementa un flujo completo de compra que involucre 4 servicios: `Order`, `Payment`, `Inventory`, y `Shipping`.

**Requisitos:**
* Debes elegir entre Coreografía u Orquestación y justificar tu decisión.
* Implementa un endpoint que permita simular fallos (ej. forzar que `Shipping` falle el 50% de las veces).
* Asegúrate de que todas las transacciones de compensación (reembolso de pago, liberación de inventario) se ejecuten correctamente cuando ocurra un fallo en cualquier punto de la cadena.
* Escribe tests de integración que validen tanto el "Happy Path" como los escenarios de compensación.

**Asistencia de IA:**
* *Prompt para Chat:* "Quiero diseñar un patrón Saga para 4 microservicios (Order, Payment, Inventory, Shipping). ¿Cuáles son los pros y contras de usar Coreografía vs Orquestación para este caso específico? Ayúdame a diseñar el diagrama de secuencia para el escenario donde Shipping falla."
* *Prompt para Claude Code/Codex:* "Escribe un test de integración en Spring Boot usando Testcontainers y WireMock que simule un flujo Saga completo donde el tercer servicio falla, verificando que los endpoints de compensación de los dos primeros servicios sean llamados."