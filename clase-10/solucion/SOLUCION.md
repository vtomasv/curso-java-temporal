# Guía docente · clase 10

Uso autorizado docente. Alumnos trabajan en TODO de ejercicios sin consultar esta referencia.

## Base completa, reproducible e independiente

Desde raíz del repositorio, destino **nuevo** fuera de ejercicios:

```bash
python3 clase-10/solucion/preparar.py /tmp/bancared-clase10-resuelta
cd /tmp/bancared-clase10-resuelta
./mvnw -Plaboratorios clean verify
python3 scripts/laboratorio.py --reset
```

Usar carpeta temporal equivalente en Windows. Iniciar Temporal según README. Generador sustituye únicamente los tres archivos de los TODO; no toca copia del alumno ni sobrescribe destino existente.

Puntos de recuperación para alguien que se atrasa:

```bash
python3 clase-10/solucion/preparar.py /tmp/bancared-base-e02 --hasta e01
python3 clase-10/solucion/preparar.py /tmp/bancared-base-e03 --hasta e02
```

Conservar trabajo anterior y registrar el avance. El `--hasta e03` completo será la **base oficial de entrada a clase 11**; las nuevas funcionalidades se preparan al crear esa clase.

## Demostración

1. Base alumno: transferencia normal, un intento y reversa deshabilitada. Explicar plan antes de proyectar solución.
2. E01: dos 503 previos al crédito, tercer intento termina. Rechazo definitivo no reintenta y compensa débito.
3. E02: respuesta perdida después del crédito. Segundo intento recupera mismo recibo; distinguir intento de Activity y movimiento económico.
4. E03: reversa independiente, cuatro movimientos total, saldos restaurados. Original COMPLETADA y reversa COMPLETADA coexisten.
5. Cierre: resultado incierto queda PENDIENTE_REVISION. Un timeout no prueba ausencia de efecto; clase 11 ampliará compensaciones/Saga.

Fallos simulados residen en proceso y se reinician al reiniciar bancos. Recibos/saldos persisten SQL sin `--reset`. No hay todavía compensación multilateral, liquidación, cumpleaños, conciliación ni Signals; corresponden a clases siguientes.

## Aceptación

* Base: saldo no negativo, validación, aislamiento bancario, concurrencia sin doble efecto, transferencia normal, rechazo compensado, incertidumbre preservada.
* E01: política exacta, tres intentos recuperables, rechazo no reintentado.
* E02: recibo repetido, payload distinto rechazado, reinicio, concurrencia y respuesta perdida.
* E03: reversa, repetición sin nuevos movimientos, destinatario sin fondos, original no elegible.
* UI: solo docente configura fallos; cliente solo opera y ve su cuenta; POST protegido CSRF.

Procedencia de capturas en `material/capturas/README.md`. Repetir demo en equipo docente antes de clase; usar claves nuevas o reiniciar dev server para comenzar desde cero.
