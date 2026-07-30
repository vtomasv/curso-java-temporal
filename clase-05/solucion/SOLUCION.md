# Solución de Ejercicios - Clase 05

## C05-E01 — Health personalizado

**Por qué:** Para verificar que la aplicación está viva y conocer su versión sin exponer detalles internos.

```java
package com.sigeo.clase05;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HealthController {

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of(
            "status", "UP",
            "version", "1.0.0"
        );
    }
}
```

## C05-E02 — Repositorio intercambiable

**Por qué:** Inyectar dependencias por constructor facilita las pruebas unitarias y hace explícitas las dependencias de la clase.

```java
package com.sigeo.clase05;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class InMemorySolicitudRepository implements SolicitudRepository {

    private final List<Solicitud> solicitudes = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Solicitud save(Solicitud solicitud) {
        if (solicitud.getId() == null) {
            solicitud.setId(idGenerator.getAndIncrement());
        }
        solicitudes.add(solicitud);
        return solicitud;
    }

    @Override
    public Optional<Solicitud> findById(Long id) {
        return solicitudes.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Solicitud> findAll() {
        return new ArrayList<>(solicitudes);
    }

    @Override
    public List<Solicitud> findByEstadoAndPrioridad(String estado, String prioridad) {
        return solicitudes.stream()
                .filter(s -> estado == null || s.getEstado().equals(estado))
                .filter(s -> prioridad == null || s.getPrioridad().equals(prioridad))
                .collect(Collectors.toList());
    }
}
```

```java
package com.sigeo.clase05;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SolicitudService {

    private final SolicitudRepository repository;

    public SolicitudService(SolicitudRepository repository) {
        this.repository = repository;
    }
    
    public Solicitud crearSolicitud(String titulo, String descripcion, String prioridad) {
        Solicitud solicitud = new Solicitud(null, titulo, descripcion, "CREADA", prioridad);
        return repository.save(solicitud);
    }

    public Solicitud obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new SolicitudNotFoundException(id));
    }

    public List<Solicitud> buscarSolicitudes(String estado, String prioridad) {
        return repository.findByEstadoAndPrioridad(estado, prioridad);
    }
}
```

## C05-E03 — Crear solicitud

**Por qué:** Usar DTOs separa el contrato de la API del modelo de dominio. El código 201 y el header Location son el estándar REST para creación.

```java
package com.sigeo.clase05;

import jakarta.validation.constraints.NotBlank;

public record CrearSolicitudDto(
    @NotBlank(message = "El título es obligatorio") String titulo,
    @NotBlank(message = "La descripción es obligatoria") String descripcion,
    @NotBlank(message = "La prioridad es obligatoria") String prioridad
) {}
```

```java
package com.sigeo.clase05;

public record SolicitudResponseDto(
    Long id,
    String titulo,
    String descripcion,
    String estado,
    String prioridad
) {}
```

```java
// En SolicitudController.java
    private final SolicitudService solicitudService;

    public SolicitudController(SolicitudService solicitudService) {
        this.solicitudService = solicitudService;
    }

    @PostMapping
    public ResponseEntity<SolicitudResponseDto> crearSolicitud(@Valid @RequestBody CrearSolicitudDto dto) {
        Solicitud solicitud = solicitudService.crearSolicitud(dto.titulo(), dto.descripcion(), dto.prioridad());
        SolicitudResponseDto response = new SolicitudResponseDto(
                solicitud.getId(), solicitud.getTitulo(), solicitud.getDescripcion(), 
                solicitud.getEstado(), solicitud.getPrioridad());
        
        return ResponseEntity
                .created(URI.create("/api/solicitudes/" + solicitud.getId()))
                .body(response);
    }
```

## C05-E04 — Consulta y filtros

**Por qué:** Los parámetros de consulta (`@RequestParam`) son ideales para filtros opcionales.

```java
// En SolicitudController.java
    @GetMapping("/{id}")
    public ResponseEntity<SolicitudResponseDto> obtenerSolicitud(@PathVariable Long id) {
        Solicitud solicitud = solicitudService.obtenerPorId(id);
        SolicitudResponseDto response = new SolicitudResponseDto(
                solicitud.getId(), solicitud.getTitulo(), solicitud.getDescripcion(), 
                solicitud.getEstado(), solicitud.getPrioridad());
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<SolicitudResponseDto>> listarSolicitudes(
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) String prioridad) {
        
        List<Solicitud> solicitudes = solicitudService.buscarSolicitudes(estado, prioridad);
        List<SolicitudResponseDto> response = solicitudes.stream()
                .map(s -> new SolicitudResponseDto(
                        s.getId(), s.getTitulo(), s.getDescripcion(), 
                        s.getEstado(), s.getPrioridad()))
                .toList();
                
        return ResponseEntity.ok(response);
    }
```

## C05-E05 — Problem Details uniforme

**Por qué:** `ProblemDetail` (RFC 7807) estandariza cómo las APIs reportan errores, facilitando la integración con clientes.

```java
package com.sigeo.clase05;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SolicitudNotFoundException.class)
    public ProblemDetail handleNotFound(SolicitudNotFoundException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problem.setTitle("No encontrado");
        return problem;
    }

    @ExceptionHandler(EstadoInvalidoException.class)
    public ProblemDetail handleConflict(EstadoInvalidoException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problem.setTitle("Conflicto de estado");
        return problem;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "La solicitud contiene datos inválidos");
        problem.setTitle("Error de validación");
        
        Map<String, String> errores = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        error -> error.getDefaultMessage() != null ? error.getDefaultMessage() : "Error de validación",
                        (msg1, msg2) -> msg1 + ", " + msg2
                ));
                
        problem.setProperty("errores", errores);
        return problem;
    }
}
```

## C05-E06 — Panel web básico

**Por qué:** Thymeleaf permite renderizar HTML en el servidor, ideal para paneles de administración simples sin necesidad de un frontend SPA.

```java
package com.sigeo.clase05;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/web/solicitudes")
public class SolicitudWebController {

    private final SolicitudService solicitudService;

    public SolicitudWebController(SolicitudService solicitudService) {
        this.solicitudService = solicitudService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("solicitudes", solicitudService.buscarSolicitudes(null, null));
        return "listado";
    }

    @GetMapping("/nueva")
    public String mostrarFormulario(Model model) {
        model.addAttribute("solicitud", new CrearSolicitudDto("", "", ""));
        return "formulario";
    }

    @PostMapping("/nueva")
    public String guardar(@Valid @ModelAttribute("solicitud") CrearSolicitudDto dto, BindingResult result) {
        if (result.hasErrors()) {
            return "formulario";
        }
        solicitudService.crearSolicitud(dto.titulo(), dto.descripcion(), dto.prioridad());
        return "redirect:/web/solicitudes";
    }
}
```

**listado.html:**
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Listado de Solicitudes</title>
</head>
<body>
    <h1>Solicitudes SIGEO</h1>
    <a th:href="@{/web/solicitudes/nueva}">Nueva Solicitud</a>
    
    <table border="1">
        <thead>
            <tr>
                <th>ID</th>
                <th>Título</th>
                <th>Estado</th>
                <th>Prioridad</th>
            </tr>
        </thead>
        <tbody>
            <tr th:each="solicitud : ${solicitudes}">
                <td th:text="${solicitud.id}">1</td>
                <td th:text="${solicitud.titulo}">Título</td>
                <td th:text="${solicitud.estado}">CREADA</td>
                <td th:text="${solicitud.prioridad}">ALTA</td>
            </tr>
        </tbody>
    </table>
</body>
</html>
```

**formulario.html:**
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Nueva Solicitud</title>
</head>
<body>
    <h1>Crear Nueva Solicitud</h1>
    
    <form th:action="@{/web/solicitudes/nueva}" th:object="${solicitud}" method="post">
        <div>
            <label>Título:</label>
            <input type="text" th:field="*{titulo}" />
            <span th:if="${#fields.hasErrors('titulo')}" th:errors="*{titulo}" style="color: red;">Error</span>
        </div>
        <div>
            <label>Descripción:</label>
            <textarea th:field="*{descripcion}"></textarea>
            <span th:if="${#fields.hasErrors('descripcion')}" th:errors="*{descripcion}" style="color: red;">Error</span>
        </div>
        <div>
            <label>Prioridad:</label>
            <select th:field="*{prioridad}">
                <option value="BAJA">Baja</option>
                <option value="MEDIA">Media</option>
                <option value="ALTA">Alta</option>
            </select>
            <span th:if="${#fields.hasErrors('prioridad')}" th:errors="*{prioridad}" style="color: red;">Error</span>
        </div>
        <button type="submit">Guardar</button>
    </form>
    
    <a th:href="@{/web/solicitudes}">Volver</a>
</body>
</html>
```

## C05-E07 — Operabilidad mínima

**Por qué:** Actuator expone endpoints para monitorear y gestionar la aplicación en producción.

```yaml
# En application.yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info
  endpoint:
    health:
      show-details: always
  info:
    env:
      enabled: true
```

## C05-E08 — Contrato MockMvc

**Por qué:** `MockMvc` permite probar los controladores web sin levantar un servidor HTTP real, verificando el enrutamiento, validación y serialización JSON.

*(La solución de este ejercicio ya está implementada en el archivo `SolicitudControllerMockMvcTest.java` del código starter)*
