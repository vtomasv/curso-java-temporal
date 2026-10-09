# Laboratorios · clase 13

Trabajar desde `clase-13/ejercicios`. Cada escenario desde reset y con claves nuevas; registrar captura propia. Las imágenes de la PPT son vistas renderizadas con datos reales y pueden tener IDs distintos.

## Terminales y reinicio del JAR

Preparar JDK 25, Python 3 y el CLI de IA autenticado antes de la clase. Desde la raíz del repositorio, en terminal A:

```bash
cd clase-13/ejercicios
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

En terminal C, también dentro de `clase-13/ejercicios`, ejecutar tests y `codex`. Portal http://localhost:8080, docente / laboratorio. Consola http://localhost:8233. Windows: `mvnw.cmd` y `python` según instalación.

Después de cada cambio Java: salir del CLI, pulsar Ctrl+C **en A**, ejecutar perfil del lab, revisar diff y construir. Esperar BUILD SUCCESS y relanzar el launcher en A. `--reset` restablece únicamente los datos ficticios de H2; **no compila y no borra History ni Schedules de Temporal externo**. Cada operación manual usa una clave nueva. Los fallos bancarios se configuran después del reinicio. No resetear con Workflows ejecutándose.

Alternativa sin consola:

```bash
python3 scripts/laboratorio.py --temporal embedded --reset
```

Mantener `--temporal embedded` al relanzar si se eligió ese modo. Usa el servidor real de pruebas del SDK; no ofrece consola ni Schedule externo. El Schedule de cumpleaños usa la fecha real de Chile. La simulación de fechas se ejecuta mediante los botones de noche/ciclo. La marca del saludo es una fecha de negocio simulada, no un reloj de producción.

## C13-E01 · Posiciones netas

Teoría: Una transferencia de A a B aumenta la posición de B y reduce la de A. Dos direcciones se netean. Una reversa completada cancela el aporte del original.

Archivo autorizado: `src/main/java/com/bancared/clase10/CalculoNeto.java`. Leer el test `C13E01Test`.

```bash
./mvnw -Plab-e01 test
codex
# Pegar prompt E01. Explicar plan y escribir «implementa».
# Salir del CLI. En terminal A, pulsar Ctrl+C al launcher.
./mvnw -Plab-e01 test
git diff -- src/main/java/com/bancared/clase10/CalculoNeto.java
./mvnw clean verify
# Esperar BUILD SUCCESS. Relanzar en A:
python3 scripts/laboratorio.py --reset
```

1. Desde reset, Laboratorio: ambos bancos Disponible.
2. Transferir 100000 de Ana a Bruno con clave c13-ida-01; esperar COMPLETADA.
3. Transferir 60000 de Bruno a Ana con clave c13-vuelta-01; esperar COMPLETADA.
4. Compensación: Calcular netos sin reservar. Cordillera -40000, Pacífico +40000.

**Resultado esperado:** Saldos clientes $960.000 / $540.000. Posiciones -$40.000 / +$40.000, suma cero.

## C13-E02 · Lote exclusivo e inmutable

Teoría: Un cierre toma un snapshot bajo lock. Cada transferencia pertenece a un solo lote abierto; se excluyen efectos inciertos y reversas en curso.

Archivo autorizado: `src/main/java/com/bancared/clase10/SeleccionLote.java`. Leer el test `C13E02Test`.

```bash
./mvnw -Plab-e02 test
codex
# Pegar prompt E02. Explicar plan y escribir «implementa».
# Salir del CLI. En terminal A, pulsar Ctrl+C al launcher.
./mvnw -Plab-e02 test
git diff -- src/main/java/com/bancared/clase10/SeleccionLote.java
./mvnw clean verify
# Esperar BUILD SUCCESS. Relanzar en A:
python3 scripts/laboratorio.py --reset
```

1. Repetir preparación de dos transferencias con claves nuevas tras reset.
2. Compensación: clave c13-lote-01, Preparar lote.
3. Comprobar dos miembros y netos -40000 / +40000. Repetir prepara el mismo snapshot.
4. Preparar c13-lote-02: cero miembros. La reversa de una transferencia reservada queda bloqueada.

**Resultado esperado:** Dos miembros exclusivos en el primer lote. Segundo lote vacío, sin duplicar posiciones.

## C13-E03 · Liquidación Saga y compensación segura

Teoría: La Saga mueve liquidez del banco deudor al acreedor. Una compensación crea un asiento nuevo. Un timeout requiere confirmar recibo antes de devolver liquidez.

Archivo autorizado: `src/main/java/com/bancared/clase10/LiquidacionWorkflowImpl.java`. Leer el test `C13E03Test`.

```bash
./mvnw -Plab-e03 test
codex
# Pegar prompt E03. Explicar plan y escribir «implementa».
# Salir del CLI. En terminal A, pulsar Ctrl+C al launcher.
./mvnw -Plab-e03 test
git diff -- src/main/java/com/bancared/clase10/LiquidacionWorkflowImpl.java
./mvnw clean verify
# Esperar BUILD SUCCESS. Relanzar en A:
python3 scripts/laboratorio.py --reset
```

1. Preparar el lote de dos transferencias como en E02 y pulsar Liquidar lote.
2. Esperar LIQUIDADO: liquidez Cordillera 1960000, Pacífico 2040000.
3. Bancos: clientes conservan $960.000 / $540.000. Repetir no agrega asientos.
4. Caso de rechazo desde reset: realizar transferencias, preparar lote nuevo y recién entonces configurar Pacífico Rechazo definitivo. Liquidar: COMPENSADO; liquidez $2.000.000 por banco.

**Resultado esperado:** Liquidación mueve $40.000 de liquidez, no vuelve a mover $160.000 de clientes. Rechazo conocido devuelve liquidez; incertidumbre conserva reserva.

## Variante y entrega

Elegir un cambio pequeño de regla o parámetro. Escribir `TODO(C13-VARIANTE): requisito, archivo, prueba y resultado` antes de pedir cambios a IA. Mantener suma de dinero, claves, controles de acceso y separación Workflow/Activity.

```bash
./mvnw -Plaboratorios clean verify
git diff -- src/main/java
```

Completar BITACORA.md con teoría en palabras propias, prompt, diff, pruebas y capturas. Si una pantalla no coincide: verificar que el perfil del lab pasa, que se construyó el JAR, que se relanzó, que el escenario se aplicó después del reset y que la clave es nueva.
