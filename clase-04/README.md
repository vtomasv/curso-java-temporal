# Clase 04: Colecciones y Estructuras de Datos

## 🎯 Objetivos de la sesión
* **Comprender a profundidad** el Java Collections Framework (List, Set, Map, Queue) y saber cuándo utilizar cada interfaz.
* **Dominar** el uso de la Streams API para el procesamiento funcional, declarativo y eficiente de colecciones de datos.
* **Introducir** conceptos de concurrencia básica en Java y el uso de colecciones concurrentes para entornos multihilo.
* **Aplicar buenas prácticas** en la elección de estructuras de datos según el caso de uso para optimizar el rendimiento.

## ⏱️ Cronograma propuesto (4 horas)
* **Hora 1: El Ecosistema de Colecciones (Collections Framework)**
  * Repaso de `List`, `Set` y `Map`.
  * Diferencias de rendimiento (Big O) y casos de uso reales.
  * Implementaciones clave: `ArrayList` vs `LinkedList`, `HashSet` vs `TreeSet`, `HashMap` vs `TreeMap`.
* **Hora 2: Programación Funcional con Streams API**
  * Introducción a la Streams API: ¿Qué es y por qué usarla?
  * Operaciones intermedias (`filter`, `map`, `sorted`) y terminales (`collect`, `reduce`, `forEach`).
  * Uso de Lambdas y Method References.
* **Hora 3: Concurrencia Básica y Colecciones Concurrentes**
  * Problemas comunes en entornos multihilo (Race conditions, Deadlocks).
  * Introducción a colecciones concurrentes: `ConcurrentHashMap`, `CopyOnWriteArrayList`, `BlockingQueue`.
* **Hora 4: Práctica y Resolución de Problemas**
  * Resolución de ejercicios prácticos.
  * Revisión de código y discusión sobre las decisiones de diseño tomadas.

## 💻 Ejercicios prácticos

### Ejercicio 1: Guiado - Gestión de Inventario con Collections
**Descripción:** Crea un sistema básico de gestión de inventario utilizando `List` y `Map`. Debes poder agregar productos, buscar por ID de forma eficiente y listar todos los productos ordenados por precio.

**Paso a paso:**
1. Crea una clase `Producto` con los atributos `id` (entero), `nombre` (String) y `precio` (double).
2. Crea una clase `Inventario` que internamente utilice un `HashMap<Integer, Producto>` para búsquedas rápidas por ID y un `ArrayList<Producto>` para mantener una lista iterable.
3. Implementa el método `agregarProducto(Producto p)` que añada el producto tanto al mapa como a la lista.
4. Implementa el método `buscarProducto(int id)` que retorne el producto usando el mapa.
5. Implementa el método `listarProductosOrdenadosPorPrecio()` utilizando `Collections.sort()` o el método `sort()` de la lista junto con un `Comparator`.

**🤖 Asistencia de IA:**
* *Prompt para Chat:* "Soy principiante en Java. ¿Puedes explicarme paso a paso cómo implementar la clase `Inventario` usando `HashMap` y `ArrayList` para el Ejercicio 1, y cómo ordenarlos por precio usando un Comparator?"
* *Prompt para Claude Code/Codex:* "Genera la clase `Producto` y la clase `Inventario` en Java. Usa un `HashMap` y un `ArrayList` internamente. Implementa los métodos agregar, buscar por ID y listar ordenado por precio."

---

### Ejercicio 2: Semi-guiado - Análisis de Datos con Streams API
**Descripción:** Dada una lista de transacciones bancarias, utiliza la Streams API para obtener estadísticas financieras sin usar bucles `for` o `while`.

**Pistas:**
* Crea una clase `Transaccion` con `id`, `monto`, `tipo` (enum: INGRESO, EGRESO) y `fecha`.
* Genera una lista de transacciones de prueba.
* Para obtener el total de ingresos, filtra por el tipo `INGRESO` y usa `mapToDouble` seguido de `sum()`.
* Para agrupar transacciones por tipo, investiga el uso del colector `Collectors.groupingBy`.
* Para encontrar la transacción de mayor monto, utiliza el método `max()` con un comparador adecuado.

**🤖 Asistencia de IA:**
* *Prompt para Chat:* "Tengo una lista de objetos `Transaccion` en Java. ¿Qué métodos de la Streams API puedo usar para agrupar estas transacciones por su atributo `tipo` y sumar los montos de cada grupo?"
* *Prompt para Claude Code/Codex:* "Tengo la clase `Transaccion` con monto y tipo. Escribe un método usando Java Streams que reciba una `List<Transaccion>` y devuelva un `Map<Tipo, Double>` con la suma de montos agrupados por tipo."

---

### Ejercicio 3: Desafío - Procesamiento Concurrente de Pedidos
**Descripción:** Simula un sistema de procesamiento de pedidos de un restaurante de comida rápida. Múltiples hilos (camareros) agregan pedidos a una cola compartida, y otros hilos (cocineros) los toman para prepararlos simultáneamente.

**Requisitos:**
* Diseña la arquitectura utilizando colecciones concurrentes adecuadas (por ejemplo, `BlockingQueue`).
* Implementa hilos productores (camareros) que generen pedidos aleatorios.
* Implementa hilos consumidores (cocineros) que procesen los pedidos (simulando un tiempo de preparación con `Thread.sleep`).
* Asegúrate de que no haya condiciones de carrera (race conditions) al procesar los pedidos.
* El programa debe detenerse limpiamente después de procesar un número determinado de pedidos (ej. 20 pedidos).

**🤖 Asistencia de IA:**
* *Prompt para Chat:* "Estoy diseñando un sistema productor-consumidor en Java para simular un restaurante. ¿Cuáles son las diferencias entre usar `ConcurrentLinkedQueue` y `BlockingQueue` para este caso de uso? ¿Cuál me recomiendas para evitar condiciones de carrera y por qué?"
* *Prompt para Claude Code/Codex:* "Implementa un patrón productor-consumidor en Java usando `BlockingQueue`. Crea una clase `Camarero` (Productor) y `Cocinero` (Consumidor) que compartan la cola. Asegura un apagado limpio de los hilos después de procesar exactamente 20 pedidos."