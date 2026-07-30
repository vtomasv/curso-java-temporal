# Clase 05: Spring Boot 4: inyección de dependencias, REST y Thymeleaf

**Bloque:** Bloque 2 — Aplicaciones web y persistencia  
**Duración:** 4 horas  

## Objetivos de Aprendizaje
- Crear proyecto Spring Boot 4 con Initializr y dependencias mínimas.
- Explicar IoC/DI y ciclo de vida de beans; usar inyección por constructor.
- Diseñar controladores REST, DTOs, servicio y repositorio en memoria.
- Aplicar validación de entrada y manejo uniforme de errores HTTP.
- Construir una vista Thymeleaf básica sin mezclar lógica de negocio.

## Cronograma de la Clase

| Minutos | Actividad | Instrucción docente |
|---|---|---|
| 00–10 | Revisión de arquitectura objetivo | Mostrar diagrama de capas del SIGEO. |
| 10–35 | IoC/DI y Spring Boot | Construir bean graph y detectar dependencias ocultas. |
| 35–60 | Demo Initializr + primer endpoint | Crear desde cero y ejecutar con perfil dev. |
| 60–80 | Ejercicios E01–E03 | Endpoints pequeños y DTOs. |
| 80–95 | Receso | Verificar que todos ejecuten la app. |
| 95–120 | Validación, errores y Thymeleaf | Mostrar handler global y formulario. |
| 120–160 | Laboratorio E04–E06 | CRUD en memoria y vista web. |
| 160–185 | Desafíos E07–E08 | Actuator y pruebas web. |
| 185–195 | Cierre y tarea | Revisión de contrato API y asignación. |

## Ejercicios de Clase

### C05-E01 — Health personalizado
**Especificación:** Crear proyecto y endpoint `/api/health` que reporte versión y estado sin exponer secretos.
**Criterios de aceptación:** Arranca con `./mvnw spring-boot:run`; respuesta JSON estable.
**Archivos involucrados:** `HealthController.java`
**Comando para verificar:** `./mvnw test -Dtest=HealthControllerTest`

### C05-E02 — Repositorio intercambiable
**Especificación:** Definir interfaz y repositorio en memoria; inyectarlo por constructor en servicio.
**Criterios de aceptación:** No usar `new` en el servicio; dependencia visible.
**Archivos involucrados:** `SolicitudRepository.java`, `InMemorySolicitudRepository.java`, `SolicitudService.java`
**Comando para verificar:** `./mvnw test -Dtest=SolicitudServiceTest`

### C05-E03 — Crear solicitud
**Especificación:** POST `/api/solicitudes` con DTO de entrada y 201 + Location.
**Criterios de aceptación:** Valida obligatorios; no devuelve entidad interna directamente.
**Archivos involucrados:** `SolicitudController.java`, `CrearSolicitudDto.java`, `SolicitudResponseDto.java`
**Comando para verificar:** `./mvnw test -Dtest=SolicitudControllerTest#testCrearSolicitud`

### C05-E04 — Consulta y filtros
**Especificación:** GET con filtros opcionales por estado/prioridad y 404 por id inexistente.
**Criterios de aceptación:** Códigos correctos; filtros componibles.
**Archivos involucrados:** `SolicitudController.java`, `SolicitudService.java`, `InMemorySolicitudRepository.java`
**Comando para verificar:** `./mvnw test -Dtest=SolicitudControllerTest#testConsultarFiltros`

### C05-E05 — Problem Details uniforme
**Especificación:** Implementar handler global para validación, not found y conflicto de estado.
**Criterios de aceptación:** Incluye type/title/status/detail/instance o estructura equivalente.
**Archivos involucrados:** `GlobalExceptionHandler.java`, `SolicitudNotFoundException.java`
**Comando para verificar:** `./mvnw test -Dtest=GlobalExceptionHandlerTest`

### C05-E06 — Panel web básico
**Especificación:** Listado, detalle y formulario de creación usando Thymeleaf.
**Criterios de aceptación:** Escapa contenido; errores de validación visibles.
**Archivos involucrados:** `SolicitudWebController.java`, `listado.html`, `formulario.html`
**Comando para verificar:** `./mvnw test -Dtest=SolicitudWebControllerTest`

### C05-E07 — Operabilidad mínima
**Especificación:** Habilitar health/info y crear info de build sin exponer env completo.
**Criterios de aceptación:** Solo endpoints necesarios; health responde correctamente.
**Archivos involucrados:** `application.yaml`
**Comando para verificar:** `./mvnw test -Dtest=ActuatorTest`

### C05-E08 — Contrato MockMvc
**Especificación:** Agregar pruebas de 201, 400, 404 y conflicto 409.
**Criterios de aceptación:** Aserciones sobre status, headers y cuerpo; no solo status.
**Archivos involucrados:** `SolicitudControllerMockMvcTest.java`
**Comando para verificar:** `./mvnw test -Dtest=SolicitudControllerMockMvcTest`

## Tareas para el Hogar

### C05-T01 — API SIGEO v1
**Esfuerzo:** 60-90 min
**Especificación:** Completar CRUD en memoria con DTOs, validación, errores y OpenAPI opcional.
**Entregable y aceptación:** Aplicación y colección HTTP. 25 pruebas; cobertura de transición inválida.

### C05-T02 — Portal Thymeleaf
**Esfuerzo:** 60-90 min
**Especificación:** Añadir edición, filtros y vista de errores amigable.
**Entregable y aceptación:** Templates y capturas. Sin lógica de negocio en HTML/controller.

### C05-T03 — Prueba de arquitectura
**Esfuerzo:** 60-90 min
**Especificación:** Crear pruebas que impidan que controller acceda directamente al repositorio.
**Entregable y aceptación:** Test ArchUnit o verificación equivalente. Falla ante dependencia prohibida.

### C05-T04 — ADR de arquitectura
**Esfuerzo:** 60-90 min
**Especificación:** Documentar decisión Spring Boot frente a despliegue WAR/Jakarta EE para este curso.
**Entregable y aceptación:** `docs/adr/0001-framework.md`. Contexto, opciones, decisión y consecuencias.

## Cómo ejecutar

Para ejecutar los tests y verificar tu progreso:
```bash
cd ejercicios
./mvnw test
```

Para ejecutar la aplicación con el perfil por defecto (H2 en memoria):
```bash
cd ejercicios
./mvnw spring-boot:run
```

Para ejecutar la aplicación con PostgreSQL (requiere base de datos local):
```bash
cd ejercicios
./mvnw spring-boot:run -Dspring-boot.run.profiles=postgres
```
