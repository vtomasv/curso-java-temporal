# Clase 15: Middleware de mensajes, colas y procesamiento asíncrono

**Bloque:** Bloque 4 — IA y tecnologías avanzadas  
**Duración:** 4 horas  

## Propósito
Diseñar integración asíncrona mediante broker, comprender sus garantías y combinar mensajería con Temporal sin duplicar responsabilidades.

## Resultados de aprendizaje
- Explicar productor, consumidor, exchange/topic, queue, ack, redelivery y dead-letter queue.
- Implementar publicación/consumo con RabbitMQ y Spring AMQP (o equivalente institucional).
- Hacer consumidores idempotentes y controlar reintentos/DLQ.
- Aplicar outbox/inbox para consistencia con base de datos.
- Distinguir cuándo usar cola, evento, Temporal Workflow o combinación.

## Cronograma de la clase

| Minutos | Actividad | Instrucción docente |
|---|---|---|
| 00–15 | Debrief visita profesional | Recoger hallazgos en tablero: arquitectura, seguridad y operación. |
| 15–40 | Fundamentos de mensajería | Dibujar rutas, garantías y fallos. |
| 40–65 | Demo broker | Publicar, consumir, fallar y redeliver. |
| 65–85 | Ejercicios E01–E03 | Publisher, consumer y validación. |
| 85–100 | Receso | Preparar DLQ/outbox. |
| 100–125 | Idempotencia, DLQ y outbox | Mostrar duplicado y poison message. |
| 125–165 | Laboratorio E04–E06 | Flujo robusto e integración Temporal. |
| 165–185 | Desafíos E07–E08 | Schema evolution y observabilidad. |
| 185–195 | Cierre y tarea | Matriz de decisión cola/Temporal. |

## Ejercicios de Clase

### C15-E01 — RabbitMQ local
**Especificación:** Levantar broker con Docker Compose y verificar management UI/health.  
**Entregable y aceptación:** Archivo `docker-compose.yml` y comandos. Credenciales de laboratorio externalizadas; volumen/puertos documentados.  
**Archivos involucrados:** `docker-compose.yml`  
**Comando para verificar:** `docker compose up -d` y acceder a `http://localhost:15672`

### C15-E02 — Notificación asíncrona
**Especificación:** Publicar `NotificationRequested` y consumirlo con ack manual o configurado.  
**Entregable y aceptación:** Apps/beans y evidencia. Mensaje tipado; correlationId; no pérdida en caso normal.  
**Archivos involucrados:** `NotificationRequested.java`, `NotificationPublisher.java`, `NotificationConsumer.java`, `NotificationPublisherTest.java`  
**Comando para verificar:** `./mvnw test -Dtest=NotificationPublisherTest`

### C15-E03 — Mensaje inválido
**Especificación:** Validar payload y enviar inválidos a ruta definida.  
**Entregable y aceptación:** Bean Validation y tests. No entra en retry infinito por error de esquema.  
**Archivos involucrados:** `NotificationRequested.java`, `NotificationConsumer.java`, `NotificationConsumerTest.java`  
**Comando para verificar:** `./mvnw test -Dtest=NotificationConsumerTest`

### C15-E04 — Duplicado de notificación
**Especificación:** Persistir messageId procesado y evitar segunda notificación.  
**Entregable y aceptación:** Inbox y test duplicado. Mismo mensaje produce un efecto.  
**Archivos involucrados:** `InboxMessage.java`, `InboxRepository.java`, `IdempotentConsumer.java`, `IdempotentConsumerTest.java`  
**Comando para verificar:** `./mvnw test -Dtest=IdempotentConsumerTest`

### C15-E05 — Poison message
**Especificación:** Configurar retry limitado y DLQ; reprocess manual controlado.  
**Entregable y aceptación:** Configuración y runbook. Mensaje problemático no bloquea cola principal.  
**Archivos involucrados:** `RabbitMQConfig.java`, `PoisonMessageTest.java`  
**Comando para verificar:** `./mvnw test -Dtest=PoisonMessageTest`

### C15-E06 — Publicación confiable
**Especificación:** Guardar cambio y evento outbox en una transacción; publicador envía y marca.  
**Entregable y aceptación:** Tablas, job/activity y tests. No existe ventana commit-sin-evento; publicación idempotente.  
**Archivos involucrados:** `OutboxEvent.java`, `OutboxRepository.java`, `OutboxPublisher.java`, `OutboxPublisherTest.java`  
**Comando para verificar:** `./mvnw test -Dtest=OutboxPublisherTest`

### C15-E07 — Mensaje inicia o señala Workflow
**Especificación:** Consumidor usa WorkflowClient para start/update/signal con ID de negocio.  
**Entregable y aceptación:** Bridge y tests. Redelivery no duplica Workflow ni comando.  
**Archivos involucrados:** `TemporalBridgeConsumer.java`, `TemporalBridgeConsumerTest.java`  
**Comando para verificar:** `./mvnw test -Dtest=TemporalBridgeConsumerTest`

### C15-E08 — Evento v1→v2
**Especificación:** Agregar campo compatible y consumidor tolerante a versiones.  
**Entregable y aceptación:** Contratos y tests. Consumidor antiguo no se rompe; cambios incompatibles versionados.  
**Archivos involucrados:** `NotificationRequestedV2.java`, `VersionTolerantConsumer.java`, `VersionTolerantConsumerTest.java`  
**Comando para verificar:** `./mvnw test -Dtest=VersionTolerantConsumerTest`

## Tareas para el Hogar

### C15-T01 — Pipeline de notificaciones
**Esfuerzo:** 60-90 min  
**Especificación:** Outbox→RabbitMQ→consumer idempotente→auditoría con DLQ.  
**Entregable y aceptación:** Sistema y 25 pruebas. Reinicio/redelivery no duplica; métricas básicas.

### C15-T02 — Integración Workflow-broker
**Esfuerzo:** 60-90 min  
**Especificación:** Al completar saga, publicar evento; otro consumidor actualiza Workflow relacionado.  
**Entregable y aceptación:** Implementación y diagrama. Responsabilidades claras; no hay ciclo infinito.

### C15-T03 — Chaos de mensajería
**Esfuerzo:** 60-90 min  
**Especificación:** Simular broker caído, consumidor caído, duplicado, poison y mensaje fuera de orden.  
**Entregable y aceptación:** Informe y pruebas/scripts. Estado recuperable y procedimientos documentados.

### C15-T04 — Informe visita profesional
**Esfuerzo:** 60-90 min  
**Especificación:** Relacionar 5 observaciones de la visita con decisiones del proyecto.  
**Entregable y aceptación:** `docs/visita-reflexion.md`. Distingue observación, interpretación y acción aplicable.

## Cómo ejecutar

Para ejecutar los tests del proyecto:
```bash
./mvnw test
```

Para levantar la infraestructura local (RabbitMQ y PostgreSQL):
```bash
docker compose up -d
```

Para iniciar el servidor de desarrollo de Temporal:
```bash
temporal server start-dev
```
