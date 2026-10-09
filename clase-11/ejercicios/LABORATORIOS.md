# Laboratorios · clase 11

Trabajar desde `clase-11/ejercicios`. Cada escenario desde reset y con claves nuevas; registrar captura propia. Las imágenes de la PPT son vistas renderizadas con datos reales y pueden tener IDs distintos.

## Terminales y reinicio del JAR

Preparar JDK 25, Python 3 y el CLI de IA autenticado antes de la clase. Desde la raíz del repositorio, en terminal A:

```bash
cd clase-11/ejercicios
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

En terminal C, también dentro de `clase-11/ejercicios`, ejecutar tests y `codex`. Portal http://localhost:8080, docente / laboratorio. Consola http://localhost:8233. Windows: `mvnw.cmd` y `python` según instalación.

Después de cada cambio Java: salir del CLI, pulsar Ctrl+C **en A**, ejecutar perfil del lab, revisar diff y construir. Esperar BUILD SUCCESS y relanzar el launcher en A. `--reset` restablece únicamente los datos ficticios de H2; **no compila y no borra History ni Schedules de Temporal externo**. Cada operación manual usa una clave nueva. Los fallos bancarios se configuran después del reinicio. No resetear con Workflows ejecutándose.

Alternativa sin consola:

```bash
python3 scripts/laboratorio.py --temporal embedded --reset
```

Mantener `--temporal embedded` al relanzar si se eligió ese modo. Usa el servidor real de pruebas del SDK; no ofrece consola ni Schedule externo. El Schedule de cumpleaños usa la fecha real de Chile. La simulación de fechas se ejecuta mediante los botones de noche/ciclo. La marca del saludo es una fecha de negocio simulada, no un reloj de producción.

## C11-E01 · Regla de cumpleaños

Teoría: El cumpleaños compara mes y día. El contrato fija el 28/02 para nacidos el 29/02 en años no bisiestos.

Archivo autorizado: `src/main/java/com/bancared/clase10/PoliticaCumple.java`. Leer el test `C11E01Test`.

```bash
./mvnw -Plab-e01 test
codex
# Pegar prompt E01. Explicar plan y escribir «implementa».
# Salir del CLI. En terminal A, pulsar Ctrl+C al launcher.
./mvnw -Plab-e01 test
git diff -- src/main/java/com/bancared/clase10/PoliticaCumple.java
./mvnw clean verify
# Esperar BUILD SUCCESS. Relanzar en A:
python3 scripts/laboratorio.py --reset
```

1. Cumpleaños: fecha de proceso 2026-10-08.
2. Pulsar Evaluar regla sin marcar.
3. Comprobar Coincidencias: A001, sin modificar saldos.
4. Probar 2026-10-09: ninguna coincidencia con los datos iniciales.

**Resultado esperado:** La regla coincide con Ana el 08/10. Tests cubren año distinto y 29/02.

## C11-E02 · Workflow de saludo diario

Teoría: El Workflow decide el orden. Las Activities consultan bancos y reemplazan las marcas de forma atómica; el reloj real está fuera del Workflow.

Archivo autorizado: `src/main/java/com/bancared/clase10/CumpleWorkflowImpl.java`. Leer el test `C11E02Test`.

```bash
./mvnw -Plab-e02 test
codex
# Pegar prompt E02. Explicar plan y escribir «implementa».
# Salir del CLI. En terminal A, pulsar Ctrl+C al launcher.
./mvnw -Plab-e02 test
git diff -- src/main/java/com/bancared/clase10/CumpleWorkflowImpl.java
./mvnw clean verify
# Esperar BUILD SUCCESS. Relanzar en A:
python3 scripts/laboratorio.py --reset
```

1. Tras build y reset: Cumpleaños, clave c11-noche-01, fecha 2026-10-08, Una noche.
2. Ejecutar proceso; esperar fecha vigente 2026-10-08 y marca A001.
3. Cuentacorrentista: Ana ve el saludo, sin movimientos de dinero.
4. Nueva clave c11-noche-02, fecha 2026-10-09: ejecutar y comprobar que A001 deja de aparecer.

**Resultado esperado:** El saludo está marcado para el día procesado y se retira al ejecutar la noche siguiente.

## C11-E03 · Schedule y continuidad

Teoría: Schedule dispara ejecuciones diarias en una zona horaria. La política SKIP evita solapamiento. El ciclo alternativo usa timer y Continue-As-New cada 30 noches.

Archivo autorizado: `src/main/java/com/bancared/clase10/AgendaCumple.java`. Leer el test `C11E03Test`.

```bash
./mvnw -Plab-e03 test
codex
# Pegar prompt E03. Explicar plan y escribir «implementa».
# Salir del CLI. En terminal A, pulsar Ctrl+C al launcher.
./mvnw -Plab-e03 test
git diff -- src/main/java/com/bancared/clase10/AgendaCumple.java
./mvnw clean verify
# Esperar BUILD SUCCESS. Relanzar en A:
python3 scripts/laboratorio.py --reset
```

1. Con Temporal externo: Crear Schedule; observar cumple-diario-BANCA-CLASE11 en la consola.
2. Disparar ahora ejecuta con la fecha real de Chile, no con la fecha simulada del formulario.
3. Pausar y Reanudar permiten controlar próximas ejecuciones.
4. Simulación rápida: nacimiento de Bruno 1988-10-09; fecha 2026-10-08, modo Dos noches, clave nueva. Al terminar queda solo B001.

**Resultado esperado:** Calendar diario 00:05 America/Santiago. Simulación de dos noches retira A001 y marca B001.

## Variante y entrega

Elegir un cambio pequeño de regla o parámetro. Escribir `TODO(C11-VARIANTE): requisito, archivo, prueba y resultado` antes de pedir cambios a IA. Mantener suma de dinero, claves, controles de acceso y separación Workflow/Activity.

```bash
./mvnw -Plaboratorios clean verify
git diff -- src/main/java
```

Completar BITACORA.md con teoría en palabras propias, prompt, diff, pruebas y capturas. Si una pantalla no coincide: verificar que el perfil del lab pasa, que se construyó el JAR, que se relanzó, que el escenario se aplicó después del reset y que la clave es nueva.
