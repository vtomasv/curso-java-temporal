# Solución de Ejercicios - Clase 04

Este documento explica paso a paso cómo resolver cada uno de los ejercicios de la clase 04.

## C04-E01 — Deduplicación de solicitudes

**Por qué:** Para deduplicar conservando el orden de llegada, la estructura de datos ideal es `LinkedHashSet`. Para que un `Set` sepa cómo identificar duplicados, debemos implementar `equals` y `hashCode` en la clase `Solicitud`.

**Código:**

En `Solicitud.java`:
```java
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Solicitud solicitud = (Solicitud) o;
        return Objects.equals(id, solicitud.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
```

En `Deduplicador.java`:
```java
    public List<Solicitud> deduplicar(List<Solicitud> solicitudes) {
        // LinkedHashSet mantiene el orden de inserción y elimina duplicados
        Set<Solicitud> set = new LinkedHashSet<>(solicitudes);
        return new ArrayList<>(set);
    }
```

## C04-E02 — Índice por responsable

**Por qué:** Es una buena práctica devolver colecciones inmodificables para evitar que el código cliente altere el estado interno de nuestra clase. Usamos `Map.copyOf` en el constructor y `Collections.unmodifiableList` o `List.copyOf` al retornar. `getOrDefault` evita el `NullPointerException`.

**Código:**

En `IndiceResponsables.java`:
```java
    public IndiceResponsables(Map<Responsable, List<Solicitud>> asignaciones) {
        // Map.copyOf crea una copia inmodificable profunda (de las claves y valores, no de los elementos internos de la lista)
        // Para ser completamente seguros, deberíamos copiar también las listas internas.
        Map<Responsable, List<Solicitud>> copiaSegura = new HashMap<>();
        asignaciones.forEach((k, v) -> copiaSegura.put(k, List.copyOf(v)));
        this.indice = Map.copyOf(copiaSegura);
    }

    public List<Solicitud> obtenerSolicitudes(Responsable responsable) {
        return indice.getOrDefault(responsable, List.of());
    }
```

## C04-E03 — Tablero de métricas

**Por qué:** La API de Streams permite procesar colecciones de forma declarativa. Usamos `filter` para quedarnos con las prioridades 1 y 2, y `groupingBy` junto con `collectingAndThen` o `teeing` para calcular múltiples métricas a la vez. Una forma más sencilla es agrupar y luego transformar.

**Código:**

En `TableroMetricas.java`:
```java
    public Map<String, MetricasDTO> calcularMetricasPorEstado(List<Solicitud> solicitudes) {
        return solicitudes.stream()
                .filter(s -> s.prioridad() == 1 || s.prioridad() == 2)
                .collect(Collectors.groupingBy(
                        Solicitud::estado,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                lista -> {
                                    long count = lista.size();
                                    double avg = lista.stream()
                                            .mapToInt(Solicitud::horasEstimadas)
                                            .average()
                                            .orElse(0.0);
                                    return new MetricasDTO(count, avg);
                                }
                        )
                ));
    }
```

## C04-E04 — Exportación atómica

**Por qué:** Escribir directamente en el archivo de destino puede dejar un archivo corrupto o parcial si el proceso falla a la mitad. Escribir en un archivo temporal y luego moverlo atómicamente garantiza que el archivo final esté completo o no exista.

**Código:**

En `Exportador.java`:
```java
    public void exportarAtomicamente(List<String> lineas, Path destino) {
        try {
            Path tempFile = Files.createTempFile("export_", ".tmp");
            Files.write(tempFile, lineas, StandardCharsets.UTF_8);
            Files.move(tempFile, destino, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Error al exportar archivo", e);
        }
    }
```

## C04-E05 — Cliente de catálogo

**Por qué:** `HttpClient` introducido en Java 11 es la forma moderna de hacer peticiones HTTP. Es importante configurar timeouts tanto en la conexión como en la petición para evitar bloqueos indefinidos.

**Código:**

En `ClienteCatalogo.java`:
```java
    public String consultar(String id) {
        try (HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(2))
                .build()) {
            
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/" + id))
                    .timeout(Duration.ofSeconds(2))
                    .GET()
                    .build();
            
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() >= 400) {
                throw new RuntimeException("Error HTTP: " + response.statusCode());
            }
            
            return response.body();
        } catch (IOException | InterruptedException e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            throw new RuntimeException("Error al consultar catálogo", e);
        }
    }
```

## C04-E06 — Procesamiento paralelo seguro

**Por qué:** Los Virtual Threads (Java 21+) son ideales para tareas bloqueantes (como I/O o `Thread.sleep`). Usamos un `ExecutorService` con `newVirtualThreadPerTaskExecutor`. Para sumar de forma segura sin race conditions, recolectamos los `Future` y luego sumamos sus resultados.

**Código:**

En `ProcesadorParalelo.java`:
```java
    public int procesarYSumarHoras(List<Solicitud> solicitudes) {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Future<Integer>> futures = solicitudes.stream()
                    .map(s -> executor.submit(() -> {
                        // Simular procesamiento
                        Thread.sleep(50);
                        return s.horasEstimadas();
                    }))
                    .toList();
            
            int total = 0;
            for (Future<Integer> future : futures) {
                total += future.get();
            }
            return total;
        } catch (Exception e) {
            throw new RuntimeException("Error en procesamiento paralelo", e);
        }
    }
```

## C04-E07 — Matriz parametrizada

**Por qué:** Las pruebas parametrizadas permiten probar múltiples combinaciones de entrada con un solo método de prueba, mejorando la cobertura y legibilidad.

**Código:**

En `ReglasNegocio.java`:
```java
    public boolean cumpleSLA(int prioridad, int horasTranscurridas, String estado) {
        if ("CERRADO".equals(estado)) {
            return true;
        }
        return switch (prioridad) {
            case 1 -> horasTranscurridas <= 24;
            case 2 -> horasTranscurridas <= 48;
            case 3 -> horasTranscurridas <= 72;
            default -> false;
        };
    }
```

En `ReglasNegocioTest.java`:
```java
    @ParameterizedTest(name = "Prioridad {0}, {1} horas, estado {2} -> cumple SLA: {3}")
    @CsvSource({
        "1, 20, NUEVO, true",
        "1, 24, EN_PROGRESO, true",
        "1, 25, NUEVO, false",
        "2, 40, NUEVO, true",
        "2, 48, EN_PROGRESO, true",
        "2, 49, NUEVO, false",
        "3, 70, NUEVO, true",
        "3, 72, EN_PROGRESO, true",
        "3, 73, NUEVO, false",
        "1, 100, CERRADO, true",
        "2, 100, CERRADO, true",
        "3, 100, CERRADO, true"
    })
    void debeEvaluarSLA(int prioridad, int horas, String estado, boolean resultadoEsperado) {
        ReglasNegocio reglas = new ReglasNegocio();
        assertThat(reglas.cumpleSLA(prioridad, horas, estado)).isEqualTo(resultadoEsperado);
    }
```

## C04-E08 — README ejecutable

**Por qué:** La documentación viva asegura que los ejemplos de código y comandos en la documentación realmente funcionen y estén actualizados.

**Código:**

En `Documentacion.java`:
```java
/**
 * Clase de ejemplo para demostrar la documentación viva.
 * 
 * Esta clase procesa elementos de texto.
 * 
 * Para reproducir un fallo común (NullPointerException), pase un valor nulo:
 * {@code new Documentacion().procesar(null);}
 * 
 * Ejemplo de curl futuro para interactuar con la API:
 * {@code curl -X POST http://localhost:8080/api/solicitudes -d '{"id":"1"}'}
 */
```
