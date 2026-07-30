# Solución Clase 6: Persistencia relacional con Spring Data JPA y PostgreSQL

## C06-E01 — Solicitud persistente

**Por qué:** Necesitamos mapear la clase Java a una tabla relacional. Usamos `@Entity` para marcarla como persistente, `@Id` y `@GeneratedValue` para el identificador único, y `@Version` para manejar concurrencia optimista (evitar que dos usuarios sobreescriban cambios simultáneamente).

```java
@Entity
@Table(name = "solicitud")
public class Solicitud {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String descripcion;
    private String estado;
    private Integer prioridad;

    @CreationTimestamp
    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @UpdateTimestamp
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @Version
    private Long version;
    
    // ...
}
```

```sql
-- V1__init.sql
CREATE TABLE solicitud (
    id UUID PRIMARY KEY,
    descripcion VARCHAR(255) NOT NULL,
    estado VARCHAR(50) NOT NULL,
    prioridad INTEGER NOT NULL,
    email VARCHAR(255),
    telefono VARCHAR(50),
    fecha_creacion TIMESTAMP NOT NULL,
    fecha_actualizacion TIMESTAMP NOT NULL,
    version BIGINT NOT NULL
);
```

## C06-E02 — Datos de contacto

**Por qué:** Un `Contacto` no tiene identidad propia (no necesita ID), es un "Value Object". Usamos `@Embeddable` para que sus campos se guarden en la misma tabla que `Solicitud`, mejorando el rendimiento al evitar joins.

```java
@Embeddable
public class Contacto {
    // ... campos ...

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contacto contacto = (Contacto) o;
        return Objects.equals(email, contacto.email) && 
               Objects.equals(telefono, contacto.telefono);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, telefono);
    }
}
```

En `Solicitud.java`:
```java
    @Embedded
    private Contacto contacto;
```

## C06-E03 — Consultas derivadas

**Por qué:** Spring Data JPA puede generar consultas SQL automáticamente basándose en el nombre del método, lo que reduce el código boilerplate para consultas simples.

```java
public interface SolicitudRepository extends JpaRepository<Solicitud, UUID> {
    List<Solicitud> findByEstadoAndPrioridadGreaterThanEqual(String estado, Integer prioridad);
    List<Solicitud> findByFechaCreacionBetween(LocalDateTime inicio, LocalDateTime fin);
}
```

## C06-E04 — Historial de aprobación

**Por qué:** Es una relación 1:N bidireccional. `Aprobacion` es el dueño de la relación (tiene la FK). Usamos `cascade = CascadeType.ALL` y `orphanRemoval = true` en `Solicitud` para que el ciclo de vida de las aprobaciones dependa de la solicitud.

En `Aprobacion.java`:
```java
@Entity
@Table(name = "aprobacion")
public class Aprobacion {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solicitud_id", nullable = false)
    private Solicitud solicitud;
    // ...
}
```

En `Solicitud.java`:
```java
    @OneToMany(mappedBy = "solicitud", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Aprobacion> aprobaciones = new ArrayList<>();

    public void addAprobacion(Aprobacion aprobacion) {
        aprobaciones.add(aprobacion);
        aprobacion.setSolicitud(this);
    }
```

```sql
-- V2__aprobacion.sql
CREATE TABLE aprobacion (
    id UUID PRIMARY KEY,
    responsable VARCHAR(255) NOT NULL,
    comentario TEXT,
    fecha TIMESTAMP NOT NULL,
    solicitud_id UUID NOT NULL,
    CONSTRAINT fk_aprobacion_solicitud FOREIGN KEY (solicitud_id) REFERENCES solicitud(id)
);
```

## C06-E05 — Bandeja paginada

**Por qué:** Devolver miles de registros a la vez consume mucha memoria. La paginación resuelve esto. No debemos exponer `Page<T>` de Spring Data en la API porque acopla el contrato web a la tecnología de persistencia.

En `PageDTO.java`:
```java
public record PageDTO<T>(
    List<T> content, int pageNumber, int pageSize, 
    long totalElements, int totalPages, boolean isLast
) {
    public static <T> PageDTO<T> from(Page<T> page) {
        return new PageDTO<>(
            page.getContent(), page.getNumber(), page.getSize(),
            page.getTotalElements(), page.getTotalPages(), page.isLast()
        );
    }
}
```

En `SolicitudController.java`:
```java
    @GetMapping
    public PageDTO<Solicitud> listarPaginado(...) {
        // ...
        Page<Solicitud> solicitudes = solicitudService.listarPorEstadoPaginado(estado, pageRequest);
        return PageDTO.from(solicitudes);
    }
```

## C06-E06 — Resumen por responsable

**Por qué:** Para reportes, no necesitamos cargar entidades completas. Las proyecciones (interfaces) permiten a Spring Data JPA optimizar la consulta SQL para traer solo las columnas necesarias.

```java
public interface ResumenResponsable {
    String getResponsable();
    Long getCantidadAprobaciones();
    LocalDateTime getUltimaAprobacion();
}
```

En `SolicitudRepository.java`:
```java
    @Query("SELECT a.responsable AS responsable, COUNT(a) AS cantidadAprobaciones, MAX(a.fecha) AS ultimaAprobacion " +
           "FROM Aprobacion a GROUP BY a.responsable")
    List<ResumenResponsable> obtenerResumenPorResponsable();
```

## C06-E07 — Caza N+1

**Por qué:** Si iteramos sobre una lista de solicitudes y accedemos a sus aprobaciones (que son Lazy por defecto), Hibernate ejecutará 1 consulta para las solicitudes y N consultas adicionales para las aprobaciones. `JOIN FETCH` o `@EntityGraph` resuelven esto cargando todo en 1 sola consulta.

En `SolicitudRepository.java`:
```java
    // Opción 1: JOIN FETCH
    @Query("SELECT s FROM Solicitud s LEFT JOIN FETCH s.aprobaciones")
    List<Solicitud> findAllWithAprobaciones();

    // Opción 2: EntityGraph
    // @EntityGraph(attributePaths = {"aprobaciones"})
    // @Query("SELECT s FROM Solicitud s")
    // List<Solicitud> findAllWithAprobaciones();
```

## C06-E08 — Cambio compatible

**Por qué:** En producción, no podemos simplemente agregar una columna `NOT NULL` si la tabla ya tiene datos, porque la migración fallará. Debemos hacerlo en pasos: agregar columna nullable, actualizar datos existentes, y luego hacerla `NOT NULL`.

```sql
-- V3__add_campo.sql
ALTER TABLE solicitud ADD COLUMN departamento VARCHAR(100);
UPDATE solicitud SET departamento = 'GENERAL' WHERE departamento IS NULL;
```

```sql
-- V4__make_campo_required.sql
ALTER TABLE solicitud ALTER COLUMN departamento SET NOT NULL;
```

En `Solicitud.java`:
```java
    @Column(nullable = false)
    private String departamento = "GENERAL";
```
