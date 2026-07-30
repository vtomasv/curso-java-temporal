# Clase 6: Persistencia relacional con Spring Data JPA y PostgreSQL

**Bloque:** Bloque 2 — Aplicaciones web y persistencia  
**Duración:** 4 horas  

## Objetivos de Aprendizaje
- Configurar PostgreSQL con Docker Compose y perfiles de desarrollo/prueba.
- Mapear entidades, objetos valor y relaciones con JPA/Hibernate.
- Usar repositorios Spring Data, consultas derivadas, JPQL y projections.
- Aplicar migraciones de esquema y datos de prueba reproducibles.
- Detectar N+1, carga perezosa, cascadas peligrosas y exposición de entidades.

## Cronograma de la Clase

| Minutos | Actividad | Instrucción docente |
|---|---|---|
| 00–10 | Revisión del modelo relacional | Comparar diagrama de clases y tablas. |
| 10–35 | JPA y ciclo de vida | Mostrar persist, flush, clear y dirty checking. |
| 35–60 | Demo Docker Compose + Flyway | Levantar DB, migrar y consultar. |
| 60–80 | Ejercicios E01–E03 | Mapeos simples y repositorios. |
| 80–95 | Receso | Revisar conectividad y puertos. |
| 95–120 | Relaciones, consultas y N+1 | Activar SQL y analizar consultas. |
| 120–160 | Laboratorio E04–E06 | Consultas, paginación y prueba real. |
| 160–185 | Desafíos E07–E08 | Optimización y migración segura. |
| 185–195 | Cierre y tarea | Ticket: explicar owning side y riesgo de cascade. |

## Ejercicios de Clase

### C06-E01 — Solicitud persistente
**Especificación:** Mapear entidad `Solicitud` con UUID, estado, prioridad, timestamps y `@Version`.
**Criterios de aceptación:** Schema y mapping alineados; no usa entidad como DTO web.
**Archivos involucrados:** `Solicitud.java`, `V1__init.sql`
**Comando para verificar:** `./mvnw test -Dtest=SolicitudEntityTest`

### C06-E02 — Datos de contacto
**Especificación:** Mapear objeto valor `Contacto` como `@Embeddable` con validación.
**Criterios de aceptación:** Columnas claras; igualdad por valor.
**Archivos involucrados:** `Contacto.java`, `Solicitud.java`
**Comando para verificar:** `./mvnw test -Dtest=ContactoEmbeddableTest`

### C06-E03 — Consultas derivadas
**Especificación:** Crear consultas por estado, prioridad y rango de fecha en `SolicitudRepository`.
**Criterios de aceptación:** Nombres correctos; índices propuestos en migración.
**Archivos involucrados:** `SolicitudRepository.java`
**Comando para verificar:** `./mvnw test -Dtest=SolicitudRepositoryTest`

### C06-E04 — Historial de aprobación
**Especificación:** Mapear relación `Solicitud` 1:N `Aprobacion` evitando serialización recursiva.
**Criterios de aceptación:** Dueño de relación correcto; orphan removal justificado.
**Archivos involucrados:** `Solicitud.java`, `Aprobacion.java`, `V2__aprobacion.sql`
**Comando para verificar:** `./mvnw test -Dtest=AprobacionRelacionTest`

### C06-E05 — Bandeja paginada
**Especificación:** Endpoint paginado y ordenado, con límites de tamaño.
**Criterios de aceptación:** No expone Page internamente sin contrato; máximo configurable.
**Archivos involucrados:** `SolicitudController.java`, `SolicitudService.java`
**Comando para verificar:** `./mvnw test -Dtest=SolicitudPaginacionTest`

### C06-E06 — Resumen por responsable
**Especificación:** Consulta agregada que devuelve proyección con conteo y última fecha.
**Criterios de aceptación:** Una consulta; tipos y nulls controlados.
**Archivos involucrados:** `ResumenResponsable.java`, `SolicitudRepository.java`
**Comando para verificar:** `./mvnw test -Dtest=ResumenProjectionTest`

### C06-E07 — Caza N+1
**Especificación:** Reproducir N+1, contar consultas y corregir con entity graph/fetch join.
**Criterios de aceptación:** Demuestra reducción con evidencia SQL.
**Archivos involucrados:** `SolicitudRepository.java`, `SolicitudService.java`
**Comando para verificar:** `./mvnw test -Dtest=NPlusOneTest`

### C06-E08 — Cambio compatible
**Especificación:** Agregar campo obligatorio en dos pasos sin romper datos existentes.
**Criterios de aceptación:** Migración aplicable sobre base poblada; rollback lógico descrito.
**Archivos involucrados:** `V3__add_campo.sql`, `V4__make_campo_required.sql`
**Comando para verificar:** `./mvnw test -Dtest=MigracionCompatibleTest`

## Tareas para el Hogar

### C06-T01 — Persistencia completa SIGEO
**Esfuerzo:** 60-90 min
**Especificación:** Reemplazar repositorio en memoria por PostgreSQL y migraciones versionadas.
**Entregable y aceptación:** Aplicación, compose y migrations. 30 pruebas, incluidos reinicio y datos persistentes.

### C06-T02 — Catálogo de consultas
**Esfuerzo:** 60-90 min
**Especificación:** Implementar 8 consultas: derivadas, JPQL, proyección, paginada y specification.
**Entregable y aceptación:** Repositorio y matriz de pruebas. Cada consulta documenta índice esperado.

### C06-T03 — Pruebas con Testcontainers
**Esfuerzo:** 60-90 min
**Especificación:** Ejecutar pruebas de repositorio contra PostgreSQL efímero.
**Entregable y aceptación:** Perfil test y CI local. No depende de DB instalada ni orden de pruebas.

### C06-T04 — Revisión de mapeo
**Esfuerzo:** 60-90 min
**Especificación:** Auditar cascadas, fetch y serialización; producir lista de riesgos y correcciones.
**Entregable y aceptación:** docs/jpa-review.md. Incluye al menos 6 hallazgos o justificaciones.

## Cómo ejecutar

1. Levantar la base de datos PostgreSQL:
   ```bash
   docker compose up -d
   ```

2. Ejecutar las pruebas (utilizan H2 o Testcontainers según el perfil):
   ```bash
   ./mvnw test
   ```

3. Ejecutar la aplicación con perfil de desarrollo (PostgreSQL):
   ```bash
   ./mvnw spring-boot:run -Dspring-boot.run.profiles=postgres
   ```
