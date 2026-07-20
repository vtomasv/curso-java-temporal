# Clase 06: Persistencia de Datos con Spring Data JPA

## Objetivos de la sesión
- Comprender los conceptos fundamentales de ORM (Object-Relational Mapping) con JPA e Hibernate.
- Configurar la conexión a una base de datos PostgreSQL en una aplicación Spring Boot.
- Mapear tablas de bases de datos a clases Java utilizando anotaciones de entidades (`@Entity`, `@Table`, `@Id`, etc.).
- Crear repositorios utilizando `JpaRepository` para realizar operaciones CRUD sin escribir SQL.
- Entender el ciclo de vida de las entidades y las consultas derivadas (Derived Queries).

## Cronograma propuesto
- **Hora 1:** Introducción a JPA, Hibernate y configuración de PostgreSQL en `application.properties`.
- **Hora 2:** Mapeo de entidades (anotaciones básicas y generación de IDs) y creación del primer repositorio.
- **Hora 3:** Operaciones CRUD básicas y consultas derivadas (Derived Queries) en Spring Data JPA.
- **Hora 4:** Ejercicios prácticos, resolución de dudas y uso de herramientas de IA para generar consultas complejas.

## Ejercicios prácticos

### Ejercicio 1: Guiado
**Configuración y CRUD Básico de un Producto**
Vamos a configurar la conexión a PostgreSQL, crear una entidad `Producto` y un repositorio para realizar operaciones CRUD básicas.

**Pasos:**
1. Agrega las dependencias de Spring Data JPA y PostgreSQL Driver en tu `pom.xml` o `build.gradle`.
2. Configura las credenciales de la base de datos en `application.properties`.
3. Crea la clase `Producto` con los atributos `id`, `nombre`, `precio` y `stock`. Anótala con `@Entity`.
4. Crea la interfaz `ProductoRepository` que extienda de `JpaRepository<Producto, Long>`.
5. Crea un `CommandLineRunner` para insertar algunos productos al iniciar la aplicación y luego listarlos por consola.

**Asistencia de IA:**
- *Modo Chat:* "Soy principiante en Spring Boot. ¿Puedes mostrarme paso a paso cómo configurar `application.properties` para conectarme a una base de datos PostgreSQL local llamada 'tienda_db' con usuario 'postgres' y contraseña 'admin'?"
- *Claude Code / Codex:* "Genera una entidad JPA llamada Producto con id autogenerado, nombre (String), precio (Double) y stock (Integer). Incluye constructores, getters y setters."

### Ejercicio 2: Semi-guiado
**Consultas Derivadas (Derived Queries)**
Extiende el repositorio de productos para buscar productos por nombre y filtrar aquellos que tengan un precio menor a un valor específico.

**Pistas:**
- Spring Data JPA permite crear consultas simplemente nombrando los métodos correctamente (ej. `findBy...`).
- Necesitarás agregar métodos en `ProductoRepository`.
- Prueba los métodos creados desde tu `CommandLineRunner` o un controlador REST simple.

**Asistencia de IA:**
- *Modo Chat:* "Tengo una entidad `Producto` en Spring Data JPA. ¿Cómo debo nombrar un método en mi repositorio para buscar todos los productos cuyo precio sea menor a un valor dado y que tengan stock mayor a cero?"
- *Claude Code / Codex:* "Agrega un método en ProductoRepository usando la convención de nombres de Spring Data para encontrar productos por nombre ignorando mayúsculas y minúsculas."

### Ejercicio 3: Desafío
**Relaciones y Consultas Personalizadas con @Query**
Crea una nueva entidad `Categoria` (id, nombre) y establece una relación de muchos a uno (Many-to-One) desde `Producto` hacia `Categoria`. Luego, escribe una consulta personalizada usando JPQL para obtener todos los productos de una categoría específica ordenados por precio de forma descendente.

**Requisitos:**
- Diseñar la relación bidireccional o unidireccional según consideres mejor.
- Usar las anotaciones `@ManyToOne` y `@JoinColumn`.
- Usar la anotación `@Query` en el repositorio para la consulta personalizada.

**Asistencia de IA:**
- *Modo Chat:* "Estoy diseñando un sistema de inventario con Spring Data JPA. Necesito relacionar una entidad `Producto` con una entidad `Categoria` (muchos productos pertenecen a una categoría). ¿Me explicas cómo configurar las anotaciones `@ManyToOne` y `@OneToMany` correctamente para evitar problemas de recursión infinita al serializar a JSON?"
- *Claude Code / Codex:* "Escribe una consulta JPQL usando la anotación @Query en ProductoRepository que devuelva una lista de productos filtrados por el nombre de su categoría, ordenados de mayor a menor precio."