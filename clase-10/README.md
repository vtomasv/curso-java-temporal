# Clase 10 · Transferencias confiables con Java y Temporal

Cuatro horas de teoría, demostración y cambios con IA sobre una aplicación que ya funciona. No depende de entregar la clase anterior. **BancoRed** es un intermediario bancario ficticio inspirado en una red interbancaria; no reproduce protocolos ni reglas reales de Redbanc.

Al terminar, el alumno explica Workflow vs. Activity, configura reintentos, recupera recibos persistidos y crea una reversa sin borrar la transferencia original.

## Lo que recibimos funcionando

* Portal Spring Boot con pantallas de cliente, bancos, intermediario y fallos de laboratorio.
* Cordillera (8081) y Pacífico (8082): procesos HTTP y bases H2 independientes. Portal/Worker en 8080, Temporal externo en 7233 y consola en 8233.
* Transferencia mediante Activities, claves estables, validación, transacciones SQL locales, locks y movimientos persistidos.
* Compensación técnica del débito cuando el destino rechaza definitivamente el crédito. Un efecto incierto queda `PENDIENTE_REVISION`: no se devuelve dinero sin conocer el resultado.
* Usuarios `docente`, `ana`, `bruno`, contraseña `laboratorio`. Los clientes solo operan su cuenta; solo docente configura fallos. APIs bancarias autenticadas.

Todos los datos son ficticios. Montos enteros CLP (`long`). Ana/A001 parte con $1.000.000, Bruno/B001 con $500.000; Carla/A002 tiene $300.000. La suma de Ana+Bruno es $1.500.000.

## Agenda · 240 minutos

| Minutos | Actividad | Evidencia |
|---|---|---|
| 0–15 | Arranque y diagnóstico | Tests base verdes y portal activo |
| 15–40 | Teoría + demo: Workflow, Activity, timeouts | Transferencia normal y History |
| 40–75 | E01: reintentos ante fallos transitorios | Tercer intento exitoso |
| 75–90 | Puesta en común y pausa | Explicar presupuesto de tiempo |
| 90–110 | Teoría: recibos y claves | Respuesta perdida |
| 110–145 | E02: idempotencia persistente | Un crédito aunque se pierda la respuesta |
| 145–165 | Teoría: compensación vs. reversa | Movimientos nuevos, original conservado |
| 165–210 | E03: reversa independiente | Saldos restaurados y repetición segura |
| 210–230 | Variante propia y bitácora | Cambio pequeño explicado por alumno |
| 230–240 | Cierre y base oficial clase 11 | Evidencias y preguntas de salida |

## Preparación

JDK 25, Python 3, Git y Temporal CLI. Maven se descarga con el wrapper. CLI de IA autenticado **antes** de la clase: [instalación oficial de Codex CLI](https://learn.chatgpt.com/docs/cli). Los prompts sirven también en otro CLI con acceso al repositorio.

Desde la raíz del repositorio:

```bash
git switch -c alumno/clase-10
cd clase-10/ejercicios
java -version
./mvnw clean verify
```

Windows: `mvnw.cmd` en lugar de `./mvnw`; `python` si corresponde.

En segunda terminal, mantener Temporal abierto:

```bash
temporal server start-dev --ip 127.0.0.1 --ui-port 8233
```

En la terminal del proyecto:

```bash
python3 scripts/laboratorio.py --reset
```

Abrir http://localhost:8080 (`docente / laboratorio`) y http://localhost:8233. Los tres servicios tardan segundos en iniciar. Logs: `ejercicios/.laboratorio/`. Ctrl+C detiene únicamente estos procesos.

**Tras cada cambio Java:** salir del CLI de IA y pulsar Ctrl+C en la terminal del launcher. Desde `clase-10/ejercicios`, ejecutar esta secuencia después de implementar E01:

```bash
./mvnw -Plab-e01 test
git diff -- src/main/java/com/bancared/clase10/PoliticaActivities.java
./mvnw clean verify
# Esperar BUILD SUCCESS antes de iniciar:
python3 scripts/laboratorio.py --reset
```

El launcher inicia el JAR construido en tres procesos (dos bancos y portal/Worker); no compila Java. `--reset` elimina exclusivamente los datos ficticios de `.laboratorio/data`, pero no borra History de Temporal externo. Mantener Temporal activo y usar una clave nueva en cada repetición, por ejemplo `e01-ok-02`. No resetear bancos con Workflows en ejecución.

Después del arranque, configurar **Laboratorio → Banco Pacífico → 503 antes de aplicar → fallos 2 → latencia 0 → Aplicar escenario**. Los fallos se restablecen al reiniciar el banco, por eso se configuran después. En **Cuentacorrentista**, transferir `100000` de Ana/Cordillera a Bruno/Pacífico con la clave nueva. La pantalla de la diapositiva 12 es **Intermediario**: dos créditos HTTP 503 y el tercer intento `APLICADO`, estado `COMPLETADA`. En **Bancos**: Ana $900.000 y Bruno $600.000. Los pasos completos están en [LABORATORIOS.md](ejercicios/LABORATORIOS.md).

Alternativa sin consola:

```bash
python3 scripts/laboratorio.py --temporal embedded --reset
```

Usa el servidor real de **pruebas** del SDK Java, sin consola web ni History persistente. Permite labs/tests; no reemplaza la demostración de consola con servidor externo.

## Laboratorios

[LABORATORIOS.md](ejercicios/LABORATORIOS.md): pasos, comandos y resultados exactos. [PROMPTS.md](ejercicios/PROMPTS.md): texto para pegar en el CLI. Tres archivos, tres `TODO(C10-Exx)`.

Los tests del incremento son deliberadamente rojos antes de implementarlo:

```bash
./mvnw -Plab-e01 test
./mvnw -Plab-e02 test
./mvnw -Plab-e03 test
```

La base `./mvnw clean verify` pasa desde el inicio. E02 requiere E01; E03 requiere E01+E02. Si alguien se atrasa, el docente entrega una base oficial hasta el lab anterior sin sobrescribir su carpeta. Al finalizar:

```bash
./mvnw -Plaboratorios clean verify
git diff -- src/main/java
```

Completar [BITACORA.md](ejercicios/BITACORA.md): teoría, prompt, diff, test, captura propia y explicación. Documentar la variante con `TODO(C10-VARIANTE)` antes de implementarla.

La [guía docente](solucion/SOLUCION.md) genera una solución completa para entrar a la clase 11 sin depender de entregas anteriores. Las clases 11–15 aún no se implementan en este cambio.

Referencias: [Activity failures/timeouts](https://docs.temporal.io/encyclopedia/detecting-activity-failures), [retry policies](https://docs.temporal.io/encyclopedia/retry-policies), [Java SDK](https://docs.temporal.io/develop/java).
