# Planificación vigente: cierre en la clase 15

La reforma afecta clases 10–15. Las clases 1–9 mantienen su material. Restan seis sesiones desde la 10 inclusive. Cada sesión reserva 240 minutos de trabajo del guion docente, ajustables al horario institucional: el docente adapta descansos y tiempos efectivos. No se añaden sesiones 16–19.

## Método de cada sesión

1. Arrancar una base independiente con las funcionalidades anteriores completas.
2. Explicar una teoría y demostrar contratos/pantallas antes de editar.
3. Ejecutar test rojo, pedir plan al CLI de IA y explicar el cambio.
4. Completar un TODO, verificar diff y test, construir y relanzar el JAR.
5. Configurar el escenario después de reset, comprobar pantalla y registrar evidencia.
6. Crear una variante pequeña con requisito, prueba y resultado documentados.

La solución docente genera copias completas o hasta E01/E02 para recuperar alumnos atrasados. El paso de una clase a la siguiente nunca copia la entrega de un alumno. Al abrir la siguiente carpeta todos parten de la misma base completa.

## Progresión de funciones

| Clase | Base disponible al comenzar | Incrementos del día | Evidencia |
|---|---|---|---|
| 10 | Pantallas, bancos y contratos preparados | Retry, recibo idempotente, reversa | Intentos limitados y saldos restaurados |
| 11 | Clases 10–10 completas | Regla de cumpleaños, Workflow de saludo diario, Schedule y continuidad | Configurar fechas, marcas diarias, timers y Schedule sin tocar las pantallas. |
| 12 | Clases 10–11 completas | Límites y contrato de aprobación, Esperar decisión y orquestar un hijo, Update confirmado e idempotente | Interactuar con un Workflow antes de ejecutar la transferencia bancaria. |
| 13 | Clases 10–12 completas | Posiciones netas, Lote exclusivo e inmutable, Liquidación Saga y compensación segura | Cerrar transferencias en un lote exclusivo y liquidar posiciones netas entre dos bancos. |
| 14 | Clases 10–13 completas | Outbox confirmado, Inbox y redelivery, Contrato y DLQ | Publicar y consumir eventos de las operaciones sin perderlos ni duplicar notificaciones. |
| 15 | Clases 10–14 completas | Cambio compatible y replay, Validar fuentes y fallback, Resumen y defensa final | Agregar consultas con fuentes, evolucionar el Workflow y verificar el proyecto completo. |

## Correspondencia con el programa anterior 10–19

| Contenido anterior | Destino vigente | Nivel de tratamiento |
|---|---|---|
| 10: Workflows/Activities, timeout y retry | 10 | Laboratorios de transferencia y reversa |
| 11: Signal, Query, Update, timer, Child, Continue-As-New | 11 y 12 | Cumpleaños/ciclo nocturno y aprobación interactiva |
| 12: integración HTTP, Saga y transacciones distribuidas | 10 y 13 | Bancos separados y liquidación de liquidez |
| 13: pruebas, time skipping, replay y versionado | 10–15, con replay en 15 | Pruebas por incremento y historia v1 reproducida con getVersion |
| 13: observabilidad y recuperación | 13 y 15 | Estados inciertos, recibos, consola y métricas agregadas |
| 14: IA, RAG y agentes durables | 15 | Asistente informativo con fuentes y fallback. Agente financiero autónomo fuera de alcance |
| 15: mensajería, ACK, redelivery, outbox/inbox, DLQ | 14 | Transporte local y RabbitMQ opcional real |
| 16: proyecto, arquitectura y rendimiento | 13–15 | Invariantes, lotes, límites de procesamiento y demo integrada |
| 17: hardening y defensa | 14–15 | Autorización, fuentes acotadas, fallos y defensa individual |
| 18: evaluación final | 15 | Suite completa, variante y explicación sin ayuda de IA |
| 19: recuperación y cierre | Cierre de 15 | Corregir brecha equivalente con evidencia, sin clase adicional |
| Heartbeats, cancelación técnica y Worker deployments | Discusión y extensión en 15 | Los labs usan Activities acotadas y cancelación de negocio. Extensión documentada, no TODO obligatorio |
| Spring AI, embeddings y vector store | Comparación en 15 | La implementación mínima usa recuperación léxica y HTTP, no se atribuyen embeddings inexistentes |

El volumen se reduce mediante contratos y UI preparados, y agrupando conceptos en un mismo caso de negocio. No se afirma que todos los temas avanzados anteriores tengan un laboratorio dedicado.

## Resultado de la clase 15

Transferencia entre dos bancos, reversa previa a liquidación, cumpleaños diarios, aprobación con vencimiento/Update, lote exclusivo con neteo y liquidación, eventos con deduplicación/DLQ y asistente informativo con replay. Clientes y liquidez bancaria tienen balances separados.

## Evaluación de cierre

Funcionalidad 30%, invariantes/resiliencia 25%, pruebas/replay 20%, explicación individual 15%, documentación 10%. Demo de ocho minutos por equipo y defensa individual: localizar contrato, estado y prueba; explicar un fallo incierto y una variante. La evaluación conserva evidencia antes/después de una recuperación.

## Agenda común

0–15 base, 15–40 teoría/demo, 40–80 E01, 80–95 pausa, 95–145 teoría/E02, 145–195 teoría/E03, 195–220 variante, 220–240 verificación/defensa. Los tiempos del aula pueden ajustarse manteniendo los criterios de aceptación.

La [planificación anterior de 19 sesiones](PLANIFICACION_ANTERIOR_19.md) se conserva solo como referencia histórica.
