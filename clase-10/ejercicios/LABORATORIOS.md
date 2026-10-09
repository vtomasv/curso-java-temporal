# Laboratorios C10 · código preparado e incrementos comprobados

Trabajar en `clase-10/ejercicios`. Portal: `docente / laboratorio`. Comenzar cada escenario con `--reset` y clave nueva. Transferencia normal de $100.000: `COMPLETADA`, Ana $900.000 y Bruno $600.000; suma $1.500.000.

## Rutina con IA

1. Ejecutar test del lab y leer el fallo esperado: la funcionalidad está pendiente.
2. Iniciar `codex` desde esta carpeta. Pegar prompt de `PROMPTS.md` (también sirve en otro CLI).
3. Pedir explicación y plan corto; relacionar cada cambio con la teoría antes de editar.
4. Pedir implementar solo el TODO autorizado; no cambiar tests ni consultar `../solucion/`.
5. Ejecutar test del lab y regresión. Revisar `git diff -- src/main/java`; explicar el diff.
6. Salir del CLI de IA y pulsar Ctrl+C en la terminal que ejecuta `laboratorio.py`. Construir el JAR con `./mvnw clean verify`; esperar `BUILD SUCCESS`. Ejecutar `python3 scripts/laboratorio.py --reset` y mantener esa terminal abierta. Un JAR iniciado no incorpora el código recién editado y el launcher no compila por sí solo. Si se utiliza el modo embebido, conservar `--temporal embedded` en el comando.
7. Completar bitácora. Para corregir, compartir el error concreto con IA y conservar el alcance.

## E01 · recuperarse de errores transitorios

Archivo: `src/main/java/com/bancared/clase10/PoliticaActivities.java`.

```bash
./mvnw -Plab-e01 test
codex
# Pegar prompt E01; explicar plan; escribir «implementa».
# Salir del CLI al terminar. En la terminal del launcher, pulsar Ctrl+C.
# Continuar desde clase-10/ejercicios:
./mvnw -Plab-e01 test
git diff -- src/main/java/com/bancared/clase10/PoliticaActivities.java
./mvnw clean verify
# Solo después de BUILD SUCCESS:
python3 scripts/laboratorio.py --reset
# Mantener esta terminal abierta; esperar que http://localhost:8080 responda.
```

Mantener StartToClose=5 s por intento y ScheduleToClose=15 s para toda la Activity. Máximo 3 intentos, intervalo inicial 200 ms, backoff 2 y máximo 1 s. Timeout HTTP 3 s por llamada bancaria. `VALIDACION`, `RECHAZO_BANCO`, `FONDOS_INSUFICIENTES` no se reintentan. Temporal exige al menos uno de StartToClose o ScheduleToClose; aquí usamos ambos.

El lanzador inicia el JAR actualizado en tres procesos: Cordillera, Pacífico y portal/Worker. `--reset` restablece sus datos ficticios, pero **no borra History del servidor Temporal externo**. Mantener Temporal activo en su propia terminal. Para repetir el escenario usar una clave que nunca se haya ejecutado allí, por ejemplo `e01-ok-02`; en una nueva repetición usar `e01-ok-03`. El ID de la captura puede diferir del propio.

1. Abrir http://localhost:8080 e ingresar como `docente`, contraseña `laboratorio`. En **Bancos**, comprobar Ana/A001 $1.000.000 y Bruno/B001 $500.000 antes de transferir.
2. **Laboratorio** → Banco **Banco Pacífico** → Modo **503 antes de aplicar** (valor interno `TRANSITORIO`) → **Fallos antes de recuperar: 2** → **Latencia en milisegundos: 0** → **Aplicar escenario**. Hacerlo después del reinicio: los fallos vuelven a `NORMAL` cuando se reinicia el banco.
3. **Cuentacorrentista** → origen Ana/A001 de Cordillera, destino Bruno/B001 de Pacífico → monto `100000` → clave nueva `e01-ok-02` → **Transferir**. Esperar que finalice.
4. **Intermediario**: `CREDITO` falla con HTTP 503 en intentos 1 y 2; intento 3 confirma `APLICADO`. Estado `COMPLETADA`. Esta es la pantalla de la diapositiva 12.
5. **Bancos**: Ana $900.000, Bruno $600.000; un débito y un crédito. La suma se conserva en $1.500.000.
6. Consola Temporal: buscar `transferencia-tx-e01-ok-02` (o el ID correspondiente a la clave elegida). History no necesariamente agrega eventos individuales por cada retry intermedio; detalle de Activity/registro del intermediario muestran contador real.
7. Caso adicional desde cero: detener launcher, ejecutar nuevamente `python3 scripts/laboratorio.py --reset`, elegir una clave nueva `e01-rechazo-02` y configurar Pacífico como **Rechazo definitivo** (`PERMANENTE`). Un intento de crédito, `COMPENSADA`, saldos iniciales; Cordillera conserva débito y crédito compensador.

La imagen de la PPT usa una ejecución con Temporal embebido: allí aparece «sin consola web». En modo externo aparecerá el enlace a la consola. La comprobación de E01 es el estado, los tres intentos y los saldos, aunque el ID o esa etiqueta difieran.

Si aparece un solo crédito exitoso, revisar que se aplicó el escenario **después** de reiniciar. Si no hay tercer intento, comprobar el perfil E01 y que se reconstruyó el JAR antes de lanzar. Si la clave ya existe en History, usar otra nueva.

Con latencia de 5 s puede haber una llamada HTTP en curso: un timeout no prueba que el banco no aplicó el crédito. Resultado incierto queda para revisión.

## E02 · respuesta perdida sin crédito duplicado

Requiere E01. Archivo: `src/main/java/com/bancared/clase10/Idempotencia.java`.

```bash
./mvnw -Plab-e02 test
codex
# Prompt E02; explicar plan; «implementa».
# Salir del CLI y pulsar Ctrl+C en la terminal del launcher.
./mvnw -Plab-e02 test
git diff -- src/main/java/com/bancared/clase10/Idempotencia.java
./mvnw clean verify
python3 scripts/laboratorio.py --reset
```

Recuperar recibo persistido para misma clave y mismos `transferId`, cuenta, monto y dirección. Payload distinto: `CLAVE_REUTILIZADA`. No reaplicar dinero ni usar Set en memoria. Locks, transacción y restricciones SQL ya están preparados; garantizan un efecto económico por transferencia/cuenta/dirección incluso si se intenta otra clave.

1. Reiniciar app/datos. Laboratorio → Pacífico → `RESPUESTA_PERDIDA`, fallos=1.
2. Transferir 100000 con clave `e02-ok`.
3. Primer crédito se aplica pero devuelve 503. Segundo intento recupera **el mismo recibo**. Estado `COMPLETADA`.
4. Ana $900.000, Bruno $600.000. Pacífico tiene **un** crédito: `tx-e02-ok:credito`.
5. Repetir botón con misma clave/payload conserva transferencia, saldos y movimientos. Deduplicar inicio HTTP del Workflow no reemplaza idempotencia de la Activity bancaria.
6. Tests comprueban reinicio del repositorio, concurrencia, payload distinto y respuesta perdida.

## E03 · reversa sin borrar historia

Requiere E01+E02. Archivo: `src/main/java/com/bancared/clase10/ReversaWorkflowImpl.java`.

```bash
./mvnw -Plab-e03 test
codex
# Prompt E03; explicar plan; «implementa».
# Salir del CLI y pulsar Ctrl+C en la terminal del launcher.
./mvnw -Plab-e03 test
git diff -- src/main/java/com/bancared/clase10/ReversaWorkflowImpl.java
./mvnw -Plaboratorios clean verify
python3 scripts/laboratorio.py --reset
```

Workflow `reversa-tx-<clave>`: obtener original mediante Activity, admitir solo `COMPLETADA` y sin liquidar, debitar destinatario y acreditar origen. Claves estables distintas del original. Conservar estado original y registrar reversa por separado. Ante fallo, consultar recibo: efecto confirmado permite continuar; rechazo definitivo con ausencia confirmada permite rechazar/compensar; consulta fallida o efecto incierto queda `PENDIENTE_REVISION`. No usar SQL/HTTP, IDs aleatorios, reloj del sistema ni sleep dentro del Workflow.

1. Reiniciar app/datos, Pacífico `NORMAL`. Transferir 100000, clave `e03-ok`.
2. Esperar `COMPLETADA`: Ana $900.000, Bruno $600.000.
3. Solicitar reversa. Esperar `REVERSA COMPLETADA`: Ana $1.000.000, Bruno $500.000.
4. Cada banco conserva dos movimientos: original y reversa. Original sigue `COMPLETADA`.
5. Consola: dos Workflows, `transferencia-tx-e03-ok` y `reversa-tx-e03-ok`.
6. Repetir reversa no agrega movimientos. Tests incluyen destinatario sin fondos y original no elegible.

Compensación técnica corrige un proceso fallido. Reversa de negocio deshace el efecto de una transferencia que sí terminó. Ambas agregan movimientos; no borran asientos.

## Variante propia y salida

Variante pequeña: parámetros de retry dentro del presupuesto, o validación de descripción. Documentar `TODO(C10-VARIANTE): requisito, archivo, prueba, resultado`. Cambio acotado con IA; agregar prueba si modifica una regla. Mantener suma, claves y movimientos.

Entrega: base y labs verdes, tres capturas propias, diff explicado y bitácora. La clase 11 empieza en base oficial completa independiente.
