# Laboratorios · clase 14

Trabajar desde `clase-14/ejercicios`. Cada escenario desde reset y con claves nuevas; registrar captura propia. Las imágenes de la PPT son vistas renderizadas con datos reales y pueden tener IDs distintos.

## Terminales y reinicio del JAR

Preparar JDK 25, Python 3 y el CLI de IA autenticado antes de la clase. Desde la raíz del repositorio, en terminal A:

```bash
cd clase-14/ejercicios
./mvnw clean verify
```

En terminal B iniciar Temporal **antes** del launcher y mantenerlo abierto:

```bash
temporal server start-dev --ip 127.0.0.1 --ui-port 8233
```

En terminal A:

```bash
python3 scripts/laboratorio.py --reset
```

En terminal C, también dentro de `clase-14/ejercicios`, ejecutar tests y `codex`. Portal http://localhost:8080, docente / laboratorio. Consola http://localhost:8233. Windows: `mvnw.cmd` y `python` según instalación.

Después de cada cambio Java: salir del CLI, pulsar Ctrl+C **en A**, ejecutar perfil del lab, revisar diff y construir. Esperar BUILD SUCCESS y relanzar el launcher en A. `--reset` restablece únicamente los datos ficticios de H2; **no compila y no borra History ni Schedules de Temporal externo**. Cada operación manual usa una clave nueva. Los fallos bancarios se configuran después del reinicio. No resetear con Workflows ejecutándose.

Alternativa sin consola:

```bash
python3 scripts/laboratorio.py --temporal embedded --reset
```

Mantener `--temporal embedded` al relanzar si se eligió ese modo. Usa el servidor real de pruebas del SDK; no ofrece consola ni Schedule externo. El Schedule de cumpleaños usa la fecha real de Chile. La simulación de fechas se ejecuta mediante los botones de noche/ciclo. La marca del saludo es una fecha de negocio simulada, no un reloj de producción.

## C14-E01 · Outbox confirmado

Teoría: El estado y el evento se escriben en una transacción local. Publicar no equivale a confirmar; si falta confirmación, el evento permanece pendiente y puede repetirse.

Archivo autorizado: `src/main/java/com/bancared/clase10/OutboxStore.java`. Leer el test `C14E01Test`.

```bash
./mvnw -Plab-e01 test
codex
# Pegar prompt E01. Explicar plan y escribir «implementa».
# Salir del CLI. En terminal A, pulsar Ctrl+C al launcher.
./mvnw -Plab-e01 test
git diff -- src/main/java/com/bancared/clase10/OutboxStore.java
./mvnw clean verify
# Esperar BUILD SUCCESS. Relanzar en A:
python3 scripts/laboratorio.py --reset
```

1. Tras reset, transferir 100000 Ana a Bruno, clave c14-tx-01; esperar COMPLETADA.
2. Notificaciones: outbox muestra transfer:tx-c14-tx-01 PENDIENTE.
3. Acción Publicar outbox pendiente, clave c14-publicar-01. Ejecutar y esperar PUBLICADO.
4. El transporte SQL_LOCAL persiste el mensaje. Todavía no hay notificación en inbox.

**Resultado esperado:** Evento PUBLICADO solo después de confirmar transporte. La cola tiene un mensaje pendiente.

## C14-E02 · Inbox y redelivery

Teoría: El inbox registra el evento y la notificación en una transacción. Repetir mismo ID/payload recupera el resultado. Otro payload con igual ID se rechaza.

Archivo autorizado: `src/main/java/com/bancared/clase10/InboxStore.java`. Leer el test `C14E02Test`.

```bash
./mvnw -Plab-e02 test
codex
# Pegar prompt E02. Explicar plan y escribir «implementa».
# Salir del CLI. En terminal A, pulsar Ctrl+C al launcher.
./mvnw -Plab-e02 test
git diff -- src/main/java/com/bancared/clase10/InboxStore.java
./mvnw clean verify
# Esperar BUILD SUCCESS. Relanzar en A:
python3 scripts/laboratorio.py --reset
```

1. Preparar y publicar una transferencia como E01 con claves nuevas tras reset.
2. Acción Consumir hasta 25 mensajes; ejecutar con clave nueva. Inbox tiene una notificación.
3. Acción Reenviar evento ya recibido; luego Consumir con otra clave nueva.
4. Comprobar una notificación, dos entregas ACK y los mismos saldos de clientes.

**Resultado esperado:** Una notificación aunque el evento se entregue dos veces. Inbox sobrevive al reinicio sin reset.

## C14-E03 · Contrato y DLQ

Teoría: Un mensaje con esquema inválido no se arregla reintentándolo. Se aparta en DLQ y se conserva evidencia; un consumidor nunca decide mover dinero.

Archivo autorizado: `src/main/java/com/bancared/clase10/PoliticaMensaje.java`. Leer el test `C14E03Test`.

```bash
./mvnw -Plab-e03 test
codex
# Pegar prompt E03. Explicar plan y escribir «implementa».
# Salir del CLI. En terminal A, pulsar Ctrl+C al launcher.
./mvnw -Plab-e03 test
git diff -- src/main/java/com/bancared/clase10/PoliticaMensaje.java
./mvnw clean verify
# Esperar BUILD SUCCESS. Relanzar en A:
python3 scripts/laboratorio.py --reset
```

1. Desde reset, recrear E02 con claves nuevas: transferir 100000 Ana a Bruno, publicar, consumir, reenviar y volver a consumir. Comprobar una notificación y dos ACK. Después, Notificaciones: acción Encolar mensaje con versión 99, otra clave nueva.
2. Acción Consumir hasta 25 mensajes, otra clave nueva.
3. Cola muestra DLQ; inbox conserva solamente notificaciones válidas.
4. Consumir otra vez no reintenta el mensaje inválido. RabbitMQ opcional muestra la cola bancared.dlq.

**Resultado esperado:** Mensaje versión 99 en DLQ. Cero notificaciones para ese mensaje y cero movimientos bancarios nuevos.

## Variante y entrega

Elegir un cambio pequeño de regla o parámetro. Escribir `TODO(C14-VARIANTE): requisito, archivo, prueba y resultado` antes de pedir cambios a IA. Mantener suma de dinero, claves, controles de acceso y separación Workflow/Activity.

```bash
./mvnw -Plaboratorios clean verify
git diff -- src/main/java
```

Completar BITACORA.md con teoría en palabras propias, prompt, diff, pruebas y capturas. Si una pantalla no coincide: verificar que el perfil del lab pasa, que se construyó el JAR, que se relanzó, que el escenario se aplicó después del reset y que la clave es nueva.

## Transporte local y RabbitMQ opcional

Por defecto `SQL_LOCAL` permite ejecutar todos los laboratorios sin Docker. Es una cola SQL persistente identificada en pantalla; no se presenta como RabbitMQ. El adaptador AMQP usa confirmación de publicación, colas durables y ack posterior al inbox. Para el broker real, desde ejercicios:

```bash
docker compose up -d
# Esperar estado healthy; luego, tras detener launcher:
python3 scripts/laboratorio.py --broker rabbit --reset
```

RabbitMQ: http://localhost:15672, curso / laboratorio, vhost bancared. Al recompilar mantener `--broker rabbit`. Las colas del broker persisten independientemente del reset de H2: usar eventos y claves nuevos; no mezclar escenarios con datos previos. Para repetición limpia del broker ficticio del curso: `docker compose down -v` y `docker compose up -d` (elimina sus mensajes). Mantener la consola de Temporal en su terminal.
