# Laboratorios · clase 15

Trabajar desde `clase-15/ejercicios`. Cada escenario desde reset y con claves nuevas; registrar captura propia. Las imágenes de la PPT son vistas renderizadas con datos reales y pueden tener IDs distintos.

## Terminales y reinicio del JAR

Preparar JDK 25, Python 3 y el CLI de IA autenticado antes de la clase. Desde la raíz del repositorio, en terminal A:

```bash
cd clase-15/ejercicios
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

En terminal C, también dentro de `clase-15/ejercicios`, ejecutar tests y `codex`. Portal http://localhost:8080, docente / laboratorio. Consola http://localhost:8233. Windows: `mvnw.cmd` y `python` según instalación.

Después de cada cambio Java: salir del CLI, pulsar Ctrl+C **en A**, ejecutar perfil del lab, revisar diff y construir. Esperar BUILD SUCCESS y relanzar el launcher en A. `--reset` restablece únicamente los datos ficticios de H2; **no compila y no borra History ni Schedules de Temporal externo**. Cada operación manual usa una clave nueva. Los fallos bancarios se configuran después del reinicio. No resetear con Workflows ejecutándose.

Alternativa sin consola:

```bash
python3 scripts/laboratorio.py --temporal embedded --reset
```

Mantener `--temporal embedded` al relanzar si se eligió ese modo. Usa el servidor real de pruebas del SDK; no ofrece consola ni Schedule externo. El Schedule de cumpleaños usa la fecha real de Chile. La simulación de fechas se ejecuta mediante los botones de noche/ciclo. La marca del saludo es una fecha de negocio simulada, no un reloj de producción.

## C15-E01 · Cambio compatible y replay

Teoría: Agregar una Activity cambia los comandos del Workflow. getVersion mantiene la ruta de historias antiguas y registra la nueva rama en ejecuciones nuevas.

Archivo autorizado: `src/main/java/com/bancared/clase10/AsistenteWorkflowImpl.java`. Leer el test `C15E01Test`.

```bash
./mvnw -Plab-e01 test
codex
# Pegar prompt E01. Explicar plan y escribir «implementa».
# Salir del CLI. En terminal A, pulsar Ctrl+C al launcher.
./mvnw -Plab-e01 test
git diff -- src/main/java/com/bancared/clase10/AsistenteWorkflowImpl.java
./mvnw clean verify
# Esperar BUILD SUCCESS. Relanzar en A:
python3 scripts/laboratorio.py --reset
```

1. Ejecutar ./mvnw -Plab-e01 test: genera historia v1 y la reproduce con el código nuevo.
2. El archivo target/historia-v1.json contiene la historia real del servidor de pruebas.
3. Tras build/reset: Asistente y operación, clave c15-valida-01, escenario Respuesta válida y citada. Pregunta: «¿Puedo reversar una transferencia liquidada?».
4. Consultar con Temporal; esperar RESPONDIDA y fuente POL-LIQUIDACION.

**Resultado esperado:** Replay v1 verde y nueva ejecución con Activity de validación. Consulta informativa sin efectos bancarios.

## C15-E02 · Validar fuentes y fallback

Teoría: El contrato valida estructura y pertenencia de fuentes al contexto, pero no prueba por sí solo la veracidad semántica. El fallback usa texto recuperado; la IA no dispone de herramientas financieras.

Archivo autorizado: `src/main/java/com/bancared/clase10/RespuestaSegura.java`. Leer el test `C15E02Test`.

```bash
./mvnw -Plab-e02 test
codex
# Pegar prompt E02. Explicar plan y escribir «implementa».
# Salir del CLI. En terminal A, pulsar Ctrl+C al launcher.
./mvnw -Plab-e02 test
git diff -- src/main/java/com/bancared/clase10/RespuestaSegura.java
./mvnw clean verify
# Esperar BUILD SUCCESS. Relanzar en A:
python3 scripts/laboratorio.py --reset
```

1. Asistente y operación: pregunta de reversa, clave c15-fallback-01, escenario Fuente inventada.
2. Consultar y esperar FALLBACK con POL-REVERSA y proveedor FALLBACK_LOCAL.
3. Repetir con claves nuevas y escenarios Salida vacía o Proveedor no disponible.
4. Bancos conserva saldos y movimientos anteriores; ningún texto del modelo ejecuta acciones.

**Resultado esperado:** Fuentes inventadas o salidas inválidas usan fallback local citado. Proveedor simulado está identificado.

## C15-E03 · Resumen y defensa final

Teoría: Las métricas resumen hechos de negocio. El volumen bruto no es saldo ni neto. La defensa exige explicar invariantes, fallos y límites con evidencias del código.

Archivo autorizado: `src/main/java/com/bancared/clase10/ResumenOperativo.java`. Leer el test `C15E03Test`.

```bash
./mvnw -Plab-e03 test
codex
# Pegar prompt E03. Explicar plan y escribir «implementa».
# Salir del CLI. En terminal A, pulsar Ctrl+C al launcher.
./mvnw -Plab-e03 test
git diff -- src/main/java/com/bancared/clase10/ResumenOperativo.java
./mvnw clean verify
# Esperar BUILD SUCCESS. Relanzar en A:
python3 scripts/laboratorio.py --reset
```

1. Desde reset, transferir 100000 Ana a Bruno con clave nueva y esperar COMPLETADA.
2. Asistente y operación: transferencias 1, completadas 1, volumenCompletado 100000, pendientes 0.
3. Ejecutar ./mvnw -Plaboratorios clean verify y completar bitácora/rúbrica.
4. Demostrar transferencia, reversa, cumpleaños, aprobación, lote, redelivery y fallback; explicar un fallo sin ayuda de IA.

**Resultado esperado:** Métricas agregadas sin cuentas ni nombres. Suite completa verde y demo final con invariantes conservadas.

## Variante y entrega

Elegir un cambio pequeño de regla o parámetro. Escribir `TODO(C15-VARIANTE): requisito, archivo, prueba y resultado` antes de pedir cambios a IA. Mantener suma de dinero, claves, controles de acceso y separación Workflow/Activity.

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

## Proveedor de IA opcional

Por defecto se usa `MODELO_SIMULADO`: una fixture reproducible, no una llamada a un LLM. Recuperación léxica sobre tres documentos ficticios, sin embeddings ni vector store. Fallback citado ante indisponibilidad o salida inválida. La validación de fuentes no garantiza verdad semántica; el asistente no tiene herramientas para mover dinero.

Para un servicio HTTP compatible con Chat Completions, configurar en la shell `LAB_AI_URL` (URL completa del endpoint), `LAB_AI_MODEL` y `LAB_AI_KEY`; no pegarlos en prompts, bitácoras, capturas ni git. Después de detener launcher:

```bash
python3 scripts/laboratorio.py --ai remote --reset
```

El modo remoto requiere una configuración del docente y conectividad. Los escenarios de fallo seleccionados en pantalla pertenecen al proveedor simulado. Las pruebas y la demo local no requieren credenciales ni realizan llamadas facturables.

## Defensa final

Demo de 8 minutos: camino normal, fallo recuperable/compensado y nueva variante. Defensa individual: localizar código y prueba, explicar efecto incierto, idempotencia, diferencia entre reversa/compensación/liquidación y límites del asistente.

Rúbrica: funcionalidad 30%, invariantes/resiliencia 25%, pruebas/replay 20%, explicación individual 15%, documentación 10%. Recuperación: corregir una brecha con variante equivalente sin borrar la evidencia inicial. El curso termina aquí; no hay clases lectivas 16–19 en el programa reformado.
