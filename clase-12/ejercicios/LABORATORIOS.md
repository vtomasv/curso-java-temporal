# Laboratorios · clase 12

Trabajar desde `clase-12/ejercicios`. Cada escenario desde reset y con claves nuevas; registrar captura propia. Las imágenes de la PPT son vistas renderizadas con datos reales y pueden tener IDs distintos.

## Terminales y reinicio del JAR

Preparar JDK 25, Python 3 y el CLI de IA autenticado antes de la clase. Desde la raíz del repositorio, en terminal A:

```bash
cd clase-12/ejercicios
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

En terminal C, también dentro de `clase-12/ejercicios`, ejecutar tests y `codex`. Portal http://localhost:8080, docente / laboratorio. Consola http://localhost:8233. Windows: `mvnw.cmd` y `python` según instalación.

Después de cada cambio Java: salir del CLI, pulsar Ctrl+C **en A**, ejecutar perfil del lab, revisar diff y construir. Esperar BUILD SUCCESS y relanzar el launcher en A. `--reset` restablece únicamente los datos ficticios de H2; **no compila y no borra History ni Schedules de Temporal externo**. Cada operación manual usa una clave nueva. Los fallos bancarios se configuran después del reinicio. No resetear con Workflows ejecutándose.

Alternativa sin consola:

```bash
python3 scripts/laboratorio.py --temporal embedded --reset
```

Mantener `--temporal embedded` al relanzar si se eligió ese modo. Usa el servidor real de pruebas del SDK; no ofrece consola ni Schedule externo. El Schedule de cumpleaños usa la fecha real de Chile. La simulación de fechas se ejecuta mediante los botones de noche/ciclo. La marca del saludo es una fecha de negocio simulada, no un reloj de producción.

## C12-E01 · Límites y contrato de aprobación

Teoría: La validación del servidor protege el contrato aunque se omitan controles del navegador. La espera no reserva fondos.

Archivo autorizado: `src/main/java/com/bancared/clase10/PoliticaAprobacion.java`. Leer el test `C12E01Test`.

```bash
./mvnw -Plab-e01 test
codex
# Pegar prompt E01. Explicar plan y escribir «implementa».
# Salir del CLI. En terminal A, pulsar Ctrl+C al launcher.
./mvnw -Plab-e01 test
git diff -- src/main/java/com/bancared/clase10/PoliticaAprobacion.java
./mvnw clean verify
# Esperar BUILD SUCCESS. Relanzar en A:
python3 scripts/laboratorio.py --reset
```

1. Aprobaciones: monto 100000, plazo 30; pulsar Validar reglas.
2. Esperado VALIDACION_OK, sin movimientos bancarios.
3. Cambiar monto a 0 y pulsar Validar reglas: error de validación.
4. Los tests comprueban monto 1..2000000 y plazo 5..300 segundos.

**Resultado esperado:** Validación rechaza monto o plazo fuera de rango. Saldos $1.000.000 / $500.000.

## C12-E02 · Esperar decisión y orquestar un hijo

Teoría: await espera una decisión o un timer. Cancelar/rechazar/vencer finaliza sin crear la transferencia. Aprobar inicia un hijo con identidad estable.

Archivo autorizado: `src/main/java/com/bancared/clase10/AprobacionWorkflowImpl.java`. Leer el test `C12E02Test`.

```bash
./mvnw -Plab-e02 test
codex
# Pegar prompt E02. Explicar plan y escribir «implementa».
# Salir del CLI. En terminal A, pulsar Ctrl+C al launcher.
./mvnw -Plab-e02 test
git diff -- src/main/java/com/bancared/clase10/AprobacionWorkflowImpl.java
./mvnw clean verify
# Esperar BUILD SUCCESS. Relanzar en A:
python3 scripts/laboratorio.py --reset
```

1. Nueva clave c12-aprobacion-01, monto 100000, plazo 30: Solicitar aprobación.
2. Consultar Query: ESPERANDO; Bancos todavía muestra saldos iniciales.
3. Aprobar; esperar COMPLETADA y $900.000 / $600.000.
4. Caso independiente desde reset, nueva clave y plazo 5: no aprobar. Esperar VENCIDA, sin movimientos.

**Resultado esperado:** La solicitud vencida no mueve dinero. La aprobada genera aprobación padre y transferencia hija en Temporal.

## C12-E03 · Update confirmado e idempotente

Teoría: Un Update devuelve el monto confirmado. Repetir un comando antiguo no deshace un cambio posterior. La misma clave con otro payload se rechaza.

Archivo autorizado: `src/main/java/com/bancared/clase10/CambiosAprobacion.java`. Leer el test `C12E03Test`.

```bash
./mvnw -Plab-e03 test
codex
# Pegar prompt E03. Explicar plan y escribir «implementa».
# Salir del CLI. En terminal A, pulsar Ctrl+C al launcher.
./mvnw -Plab-e03 test
git diff -- src/main/java/com/bancared/clase10/CambiosAprobacion.java
./mvnw clean verify
# Esperar BUILD SUCCESS. Relanzar en A:
python3 scripts/laboratorio.py --reset
```

1. Desde reset: solicitud nueva c12-update-01, monto 100000, plazo 60.
2. Cambiar monto: solicitud c12-update-01, clave c12-cambio-01, monto 120000.
3. Enviar Update confirmado; repetir misma clave y monto. Query indica 120000.
4. Aprobar y esperar COMPLETADA: Ana $880.000, Bruno $620.000.

**Resultado esperado:** Una transferencia de $120.000 y un débito/crédito. Update fuera de ventana se rechaza.

## Variante y entrega

Elegir un cambio pequeño de regla o parámetro. Escribir `TODO(C12-VARIANTE): requisito, archivo, prueba y resultado` antes de pedir cambios a IA. Mantener suma de dinero, claves, controles de acceso y separación Workflow/Activity.

```bash
./mvnw -Plaboratorios clean verify
git diff -- src/main/java
```

Completar BITACORA.md con teoría en palabras propias, prompt, diff, pruebas y capturas. Si una pantalla no coincide: verificar que el perfil del lab pasa, que se construyó el JAR, que se relanzó, que el escenario se aplicó después del reset y que la clave es nueva.
