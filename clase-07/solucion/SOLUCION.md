# Solucionario Clase 07

Este documento contiene las soluciones paso a paso para los ejercicios de la Clase 07.

## C07-E01 — Rollback total

**Problema:** El método `aprobarSolicitud` no revierte los cambios en la base de datos si ocurre una excepción después de guardar la solicitud y la aprobación.

**Solución:** Agregar la anotación `@Transactional` al método o a la clase. Por defecto, `@Transactional` hace rollback para excepciones no comprobadas (`RuntimeException`).

```java
// En SolicitudService.java
import org.springframework.transaction.annotation.Transactional;

@Service
public class SolicitudService {
    // ...

    @Transactional
    public void aprobarSolicitud(Long solicitudId, String aprobador, boolean simularFallo) {
        Solicitud solicitud = solicitudRepository.findById(solicitudId)
                .orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada"));

        solicitud.setEstado("APROBADA");
        solicitudRepository.save(solicitud);

        Aprobacion aprobacion = new Aprobacion(solicitudId, aprobador, "Aprobado automáticamente");
        aprobacionRepository.save(aprobacion);

        if (simularFallo) {
            throw new RuntimeException("Fallo simulado durante la aprobación");
        }
    }
}
```

## C07-E02 — Self-invocation

**Problema:** El método `procesarSolicitud` llama a `actualizarEstado` (que tiene `@Transactional`) desde dentro de la misma clase. Spring usa proxies para manejar las transacciones, por lo que las llamadas internas no pasan por el proxy y la transacción no se inicia.

**Solución:** Mover el método `@Transactional` a otro servicio, o inyectar el propio servicio (Self-Injection), o usar `TransactionTemplate`. La forma más limpia es reestructurar el código.

```java
// En SelfInvocationService.java
@Service
public class SelfInvocationService {

    private final SolicitudRepository solicitudRepository;
    private final SelfInvocationService self; // Self-injection

    // Usar @Lazy para evitar dependencia circular
    public SelfInvocationService(SolicitudRepository solicitudRepository, @org.springframework.context.annotation.Lazy SelfInvocationService self) {
        this.solicitudRepository = solicitudRepository;
        this.self = self;
    }

    public void procesarSolicitud(Long id, boolean simularFallo) {
        // Llamar a través del proxy
        self.actualizarEstado(id, simularFallo);
    }

    @Transactional
    public void actualizarEstado(Long id, boolean simularFallo) {
        // ...
    }
}
```

## C07-E03 — Conflicto optimista

**Problema:** Dos transacciones concurrentes pueden sobrescribir los cambios de la otra (Lost Update).

**Solución:** Agregar un campo `@Version` a la entidad `Solicitud` e implementar la actualización en el servicio.

```java
// En Solicitud.java
import jakarta.persistence.Version;

@Entity
public class Solicitud {
    // ...
    
    @Version
    private Long version;

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}

// En SolicitudService.java
@Transactional
public Solicitud actualizarSolicitud(Long id, String nuevaDescripcion) {
    Solicitud solicitud = solicitudRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada"));
            
    solicitud.setDescripcion(nuevaDescripcion);
    return solicitudRepository.save(solicitud);
}
```

## C07-E04 — Servicio de aprobación

**Problema:** Implementar la lógica de negocio para registrar una aprobación con validaciones.

**Solución:**

```java
// En AprobacionService.java
public Aprobacion registrarAprobacion(Long solicitudId, String aprobador, String comentarios) {
    Solicitud solicitud = solicitudRepository.findById(solicitudId)
            .orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada"));
            
    if ("APROBADA".equals(solicitud.getEstado())) {
        throw new IllegalStateException("La solicitud ya está aprobada");
    }
    
    Aprobacion aprobacion = new Aprobacion(solicitudId, aprobador, comentarios);
    Aprobacion guardada = aprobacionRepository.save(aprobacion);
    
    solicitud.setEstado("APROBADA");
    solicitudRepository.save(solicitud);
    
    return guardada;
}
```

## C07-E05 — Controller aislado

**Problema:** El endpoint PUT no está implementado y no maneja la excepción de concurrencia.

**Solución:** Implementar el endpoint y usar `@ExceptionHandler` para capturar `ObjectOptimisticLockingFailureException` y devolver un 409 Conflict.

```java
// En SolicitudController.java
import org.springframework.orm.ObjectOptimisticLockingFailureException;

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudController {
    // ...

    @PutMapping("/{id}")
    public ResponseEntity<Solicitud> actualizar(@PathVariable Long id, @RequestBody Solicitud request) {
        Solicitud actualizada = solicitudService.actualizarSolicitud(id, request.getDescripcion());
        return ResponseEntity.ok(actualizada);
    }
    
    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<String> handleOptimisticLockingFailure(ObjectOptimisticLockingFailureException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("El recurso fue modificado por otro usuario. Por favor, recargue y vuelva a intentarlo.");
    }
}
```

## C07-E06 — Flujo HTTP→DB

**Problema:** El test de integración falla porque el endpoint PUT no estaba implementado.

**Solución:** Una vez implementado el endpoint en C07-E05, el test pasará automáticamente. Asegurarse de que Testcontainers esté configurado correctamente en el entorno local (Docker en ejecución).

## C07-E07 — Petición lenta

**Problema:** Identificar por qué una petición es lenta (simulado).

**Solución:** (Contenido para `debug-trace.md`)
1. Configurar `logging.level.org.hibernate.SQL=DEBUG` en `application.yaml`.
2. Observar el problema de N+1 queries al cargar colecciones perezosas.
3. Solución: Usar `JOIN FETCH` en JPQL o `@EntityGraph` en el repositorio.

## C07-E08 — OpenAPI + ADR

**Problema:** Documentar la API y la decisión de arquitectura.

**Solución:** (Contenido para `001-optimistic-locking.md`)
```markdown
# ADR 001: Uso de Optimistic Locking para concurrencia

## Contexto
Múltiples usuarios pueden intentar aprobar o modificar la misma solicitud simultáneamente.

## Decisión
Utilizaremos Optimistic Locking (mediante la anotación `@Version` de JPA) en lugar de Pessimistic Locking.

## Consecuencias
- **Positivas:** Mejor rendimiento y escalabilidad, ya que no bloqueamos filas en la base de datos durante las lecturas.
- **Negativas:** Los clientes deben estar preparados para manejar respuestas HTTP 409 Conflict y reintentar la operación.
```
