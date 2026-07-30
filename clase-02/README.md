# Clase 02: Programación orientada a objetos moderna y diseño mantenible

**Bloque:** Bloque 1 — Fundamentos de Java moderno
**Duración:** 4 horas

## Objetivos de aprendizaje
- Diseñar clases con invariantes, constructores y métodos de comportamiento, evitando modelos anémicos.
- Distinguir identidad, igualdad, mutabilidad e inmutabilidad.
- Aplicar composición, interfaces y polimorfismo antes de recurrir a herencia.
- Usar enums, records, sealed classes y pattern matching cuando agreguen claridad.
- Refactorizar código guiándose por pruebas y responsabilidades.

## Cronograma de la clase

| Minutos | Actividad | Instrucción docente |
|---|---|---|
| 00–10 | Recuperación y quiz de conceptos | Revisar tarea de setup y tres errores comunes. |
| 10–35 | Exposición POO e invariantes | Modelar en pizarra una solicitud con estados válidos. |
| 35–60 | Live coding de objetos valor y entidad | Mostrar constructor protegido y métodos de transición. |
| 60–80 | Ejercicios E01–E03 | Pair programming con rotación de conductor. |
| 80–95 | Receso | Revisar commits parciales. |
| 95–120 | Polimorfismo, composición y Java moderno | Demostrar sealed interface y pattern matching. |
| 120–160 | Laboratorio E04–E06 | Exigir pruebas antes de cada refactor. |
| 160–185 | Desafíos E07–E08 | Asignar code review cruzado con checklist. |
| 185–195 | Cierre y tarea | Exit ticket: justificar una decisión composición/herencia. |

## Ejercicios de clase

### C02-E01 — Invariante de Recurso
**Especificación:** Crear `Recurso` con código, nombre y estado; impedir código vacío y cantidades negativas.
**Criterios de aceptación:** No expone setters generales; errores con mensajes útiles.
**Archivos involucrados:** `Recurso.java`, `RecursoTest.java`
**Comando para verificar:** `./mvnw test -Dtest=RecursoTest`

### C02-E02 — CorreoInstitucional
**Especificación:** Modelar correo inmutable, normalizar dominio y comparar por valor.
**Criterios de aceptación:** equals/hashCode correctos; no acepta formato inválido.
**Archivos involucrados:** `CorreoInstitucional.java`, `CorreoInstitucionalTest.java`
**Comando para verificar:** `./mvnw test -Dtest=CorreoInstitucionalTest`

### C02-E03 — Prioridad y SLA
**Especificación:** Enum que conozca horas de atención y factor de escalamiento.
**Criterios de aceptación:** Sin switch externo duplicado para obtener SLA.
**Archivos involucrados:** `Prioridad.java`, `PrioridadTest.java`
**Comando para verificar:** `./mvnw test -Dtest=PrioridadTest`

### C02-E04 — Canales de notificación
**Especificación:** Interfaz `Notificador` con implementaciones consola, correo simulado y SMS simulado; servicio depende de interfaz.
**Criterios de aceptación:** DIP: servicio no instancia implementaciones concretas.
**Archivos involucrados:** `Notificador.java`, `NotificadorConsola.java`, `NotificadorCorreo.java`, `NotificadorSMS.java`, `ServicioNotificacion.java`, `ServicioNotificacionTest.java`
**Comando para verificar:** `./mvnw test -Dtest=ServicioNotificacionTest`

### C02-E05 — Eliminar herencia frágil
**Especificación:** Transformar jerarquía Recurso→Vehículo→Ambulancia con flags en composición de capacidades.
**Criterios de aceptación:** Se elimina comportamiento condicional por tipo cuando es posible.
**Archivos involucrados:** `Vehiculo.java`, `Capacidad.java`, `VehiculoTest.java`
**Comando para verificar:** `./mvnw test -Dtest=VehiculoTest`

### C02-E06 — Estados sellados
**Especificación:** Modelar estados Borrador, EnRevisión, Aprobada, Rechazada con sealed interface y pattern matching.
**Criterios de aceptación:** Compilador obliga a tratar todos los estados.
**Archivos involucrados:** `EstadoSolicitud.java`, `ProcesadorEstado.java`, `ProcesadorEstadoTest.java`
**Comando para verificar:** `./mvnw test -Dtest=ProcesadorEstadoTest`

### C02-E07 — Validador contextual
**Especificación:** Implementar clase interna privada que valide una transición usando estado del agregado.
**Criterios de aceptación:** No expone la clase; acceso justificado al contexto externo.
**Archivos involucrados:** `AgregadoSolicitud.java`, `AgregadoSolicitudTest.java`
**Comando para verificar:** `./mvnw test -Dtest=AgregadoSolicitudTest`

### C02-E08 — Cazador de olores POO
**Especificación:** Revisar una clase de 200 líneas e identificar al menos 8 olores, priorizar 3 y aplicar 2 refactors seguros.
**Criterios de aceptación:** Cada refactor respaldado por pruebas.
**Archivos involucrados:** `ClaseDios.java`, `ClaseDiosTest.java`
**Comando para verificar:** `./mvnw test -Dtest=ClaseDiosTest`

## Tareas para el hogar

### C02-T01 — Modelo SIGEO v1
**Esfuerzo:** 60-90 min
**Especificación:** Modelar Solicitud, Solicitante, Recurso, Aprobación y Estado con relaciones y reglas de transición.
**Entregable y aceptación:** Módulo domain con 20 pruebas. Sin setters indiscriminados; cobertura de estados inválidos.

### C02-T02 — Catálogo de patrones POO
**Esfuerzo:** 60-90 min
**Especificación:** Crear ejemplos mínimos de estrategia, fábrica y adaptador dentro del dominio.
**Entregable y aceptación:** Tres paquetes con README comparativo. Cada patrón resuelve un problema real, no ceremonial.

### C02-T03 — Refactor de código legado
**Esfuerzo:** 60-90 min
**Especificación:** Recibir una solución procedural y convertirla a un diseño orientado a objetos en 4 commits.
**Entregable y aceptación:** Rama refactor/c02. Pruebas preservan comportamiento; explicación del diseño.

### C02-T04 — Defensa oral grabada
**Esfuerzo:** 45-60 min
**Especificación:** Grabar 5 minutos explicando invariantes, igualdad y por qué eligió composición o herencia.
**Entregable y aceptación:** Enlace o guion en docs. Debe referirse a su propio código y a una prueba concreta.

## Cómo ejecutar
Para ejecutar todos los tests de la clase:
```bash
cd ejercicios
./mvnw test
```
Para ejecutar un test específico:
```bash
cd ejercicios
./mvnw test -Dtest=NombreDelTest
```
