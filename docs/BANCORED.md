# BancoRed: arquitectura e invariantes

## Procesos y datos

Cordillera (8081) y Pacífico (8082) conservan sus cuentas y recibos en H2 independientes. Portal/Worker (8080) guarda estados, marcas, lotes, outbox/inbox y consultas. Temporal conserva History y timers. No hay transacción SQL que abarque los tres procesos.

Datos iniciales: Ana A001 $1.000.000, Bruno B001 $500.000, liquidez bancaria $2.000.000 por banco. Montos enteros CLP. La autorización del portal distingue docente, Ana y Bruno; bancos y credenciales locales son un entorno ficticio de laboratorio.

## Garantías del ejemplo

* Efecto económico local: recibo y saldo en una transacción, clave y payload estables, lock y restricciones únicas. Activities pueden ejecutarse más de una vez.
* Incertidumbre: un timeout no demuestra que el banco no aplicó. Consultar recibo; si no se conoce el resultado, conservar PENDIENTE_REVISION.
* Reversa: nuevos asientos, original conservado, fondos del destinatario suficientes, sin lote reservado ni liquidación. No garantiza devolución si el cliente ya gastó los fondos.
* Cumpleaños: consultar todos los bancos antes de reemplazar marcas; fecha de negocio única y actualización atómica. 29/02 se celebra el 28/02 en años no bisiestos.
* Aprobación: Signal decide, Query observa, Update confirma cambio. Primera decisión aceptada cierra la ventana. Esperar no reserva fondos; puede fallar por fondos insuficientes al aprobar.
* Lote: snapshot con reserva exclusiva. Transferencia liquidada o efecto/reversa incierto se excluye. Repetir ID devuelve el mismo snapshot. Un lote compensado/rechazado conserva su snapshot histórico y libera la reserva de sus miembros para un lote nuevo. El lote anterior permanece cerrado y no vuelve a mover liquidez.
* Liquidación: posiciones netas suman cero, asientos nuevos en liquidez bancaria, saldos de clientes sin cambios. Rechazo conocido compensa. Incertidumbre mantiene reserva y revisión.
* Eventos: outbox atómico con estado de negocio, confirmación antes de PUBLICADO, inbox antes de ACK. El mismo ID con distinto payload se rechaza. DLQ conserva contrato inválido. El consumidor nunca mueve dinero.
* IA: recuperación léxica sobre tres políticas ficticias, E/S en Activity, salida acotada y fuentes pertenecientes al contexto. Fallback citado. Validar fuentes no demuestra verdad semántica. Ninguna herramienta financiera está expuesta al modelo.

## Límites y operación

SQL_LOCAL y MODELO_SIMULADO son modos reproducibles, identificados en pantalla. RabbitMQ y el modelo remoto son adaptadores reales opcionales. El modo embebido usa el servidor de pruebas del SDK y no tiene consola ni Schedule externo. Para Schedule se usa Temporal externo.

Reset borra H2 ficticio, sin borrar History/Schedules externos. No resetear con operaciones activas: un Workflow antiguo podría dirigirse a datos nuevos. Usar claves nuevas y pausar Schedule antes de cambiar de clase. Cada clase tiene Task Queue distinta.

Los lotes y consumers procesan cantidades limitadas. El ciclo nocturno usa Continue-As-New. Para trabajos largos futuros: Activity con heartbeat/progreso persistido, cancelación cooperativa y tests de recuperación. La cancelación actual de aprobación es un estado de negocio antes de transferir, no una terminación abrupta del Workflow.

## Seguridad y defensa final

Probar que Ana no accede a `/api/lab/**`, no puede enviar acciones de Bruno y solo recibe sus datos. Mantener CSRF. No introducir claves de IA en capturas, prompts, logs ni git. Tratar texto recuperado/modelo como datos no confiables. El ejemplo no implementa un protocolo de seguridad interbancaria de producción.

En clase 15 discutir límites de polling HTTP, cardinalidad del History, bloqueo SQL, presupuesto de timeouts, latencia de modelo y coste por consulta. Para medir: usar la misma secuencia de operaciones, registrar latencia observada y cantidad de mensajes, sin inventar TPS o SLO de producción.
