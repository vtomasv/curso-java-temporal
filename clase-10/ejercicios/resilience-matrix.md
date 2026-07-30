# Matriz de Resiliencia (C10-E08)

Completa esta tabla ejecutando diferentes combinaciones de fallos, timeouts y reintentos.

| Escenario | Timeout Configurado | Retry Policy | Fallo Simulado | Resultado Esperado | Resultado Real |
|-----------|---------------------|--------------|----------------|--------------------|----------------|
| 1         | StartToClose=2s     | Default      | Latencia 3s    | TimeoutFailure     | TODO           |
| 2         | StartToClose=5s     | MaxAttempts=1| Excepción      | ActivityFailure    | TODO           |
| 3         | StartToClose=5s     | Default      | Excepción      | Reintento exitoso  | TODO           |
| 4         | StartToClose=5s     | DoNotRetry=400| AppFailure(400)| Falla inmediata    | TODO           |
| 5         | Heartbeat=2s        | Default      | Bloqueo 5s     | TimeoutFailure     | TODO           |
| 6         | Heartbeat=2s        | Default      | Heartbeat 1s   | Éxito              | TODO           |
| 7         | StartToClose=10s    | Backoff=2.0  | Falla 3 veces  | Éxito en intento 4 | TODO           |
| 8         | ScheduleToClose=5s  | Default      | Falla continua | TimeoutFailure     | TODO           |

**Anomalías investigadas:**
(Describe aquí cualquier comportamiento inesperado y su causa)
