# Clase 07: Consultas Avanzadas y Transacciones

## Objetivos de la sesión
- Comprender y aplicar JPQL para realizar consultas personalizadas en bases de datos relacionales.
- Utilizar Criteria API y Specifications para construir consultas dinámicas de forma programática y segura (type-safe).
- Implementar paginación y ordenamiento de resultados para optimizar el rendimiento de las aplicaciones.
- Dominar el uso de la anotación `@Transactional` para gestionar transacciones y asegurar la integridad de los datos.
- Entender los conceptos de bloqueo (locking) optimista y pesimista para manejar la concurrencia.

## Cronograma propuesto
- **0:00 - 0:45**: Introducción a JPQL y consultas personalizadas con `@Query`.
- **0:45 - 1:30**: Paginación y ordenamiento (`Pageable`, `Sort`).
- **1:30 - 1:45**: Descanso.
- **1:45 - 2:30**: Criteria API y Specifications para consultas dinámicas.
- **2:30 - 3:15**: Gestión de transacciones con `@Transactional` (propagación y aislamiento).
- **3:15 - 4:00**: Concurrencia y Locking (Optimistic y Pessimistic Lock).

## Ejercicios prácticos

### Ejercicio 1: Guiado
**Tema:** Consultas con JPQL y Paginación.

**Descripción:** Crear un repositorio para una entidad `Producto` que permita buscar productos por nombre (ignorando mayúsculas/minúsculas), filtrar por rango de precios y devolver los resultados paginados.

**Pasos:**
1. Crear la entidad `Producto` con atributos `id`, `nombre`, `precio`, `categoria`.
2. Crear el `ProductoRepository` extendiendo `JpaRepository`.
3. Definir un método con `@Query` usando JPQL para buscar por nombre y rango de precio.
4. Añadir el parámetro `Pageable` al método para habilitar la paginación.
5. Crear un controlador REST para probar el endpoint pasando parámetros de página y tamaño.

**Asistencia de IA:**
- *Prompt Chat:* "Actúa como un profesor de Spring Boot. Explícame paso a paso cómo crear una consulta JPQL con `@Query` que filtre por nombre y precio, y que además soporte paginación usando `Pageable`."
- *Prompt Claude Code/Codex:* "Genera la entidad Producto y su repositorio JPA. Incluye un método con @Query en JPQL para buscar productos por nombre (LIKE) y precio entre dos valores, devolviendo un objeto Page<Producto>."

### Ejercicio 2: Semi-guiado
**Tema:** Consultas dinámicas con Criteria API (Specifications).

**Descripción:** Implementar un buscador avanzado de `Pedidos` donde los filtros (estado, fecha de inicio, fecha de fin, cliente) sean opcionales.

**Pistas:**
- Usa la interfaz `JpaSpecificationExecutor` en tu repositorio.
- Crea una clase `PedidoSpecification` con métodos estáticos que devuelvan `Specification<Pedido>`.
- Combina las especificaciones usando `Specification.where().and()` dependiendo de qué parámetros no sean nulos.

**Asistencia de IA:**
- *Prompt Chat:* "Tengo que implementar filtros dinámicos opcionales para una entidad Pedido en Spring Data JPA. ¿Me puedes dar un ejemplo de cómo usar `Specification` y `CriteriaBuilder` para lograr esto?"
- *Prompt Claude Code/Codex:* "Crea una clase PedidoSpecification con métodos para filtrar por estado y rango de fechas. Luego actualiza PedidoRepository para que extienda JpaSpecificationExecutor y muestra cómo usarlo en un servicio."

### Ejercicio 3: Desafío
**Tema:** Transacciones y Locking.

**Descripción:** Simular un sistema de compra de entradas (tickets) para un evento. Debes asegurar que no se vendan más entradas de las disponibles cuando múltiples usuarios intentan comprar al mismo tiempo.

**Requisitos:**
- Entidad `Evento` con `capacidadMaxima` y `entradasVendidas`.
- Método de compra que verifique la disponibilidad y actualice el contador.
- Usar `@Transactional` y configurar un mecanismo de Locking (Optimista con `@Version` o Pesimista con `@Lock`).
- Escribir un test que simule concurrencia (múltiples hilos) para verificar que no hay sobreventa.

**Asistencia de IA:**
- *Prompt Chat:* "Estoy diseñando un sistema de venta de entradas en Spring Boot y necesito evitar la sobreventa concurrente. ¿Cuáles son las diferencias entre Optimistic Locking y Pessimistic Locking en JPA, y cuál me recomiendas para este caso?"
- *Prompt Claude Code/Codex:* "Implementa un servicio de compra de entradas con Spring Boot. Usa @Transactional y Pessimistic Locking en el repositorio para evitar condiciones de carrera. Genera también un test de integración usando ExecutorService para simular 100 peticiones concurrentes comprando la misma entrada."