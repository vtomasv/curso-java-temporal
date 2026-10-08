# Prompts del CLI de IA

Iniciar `codex` en `clase-10/ejercicios`; pegar el texto del lab. Explicar el plan al docente/compañero y luego escribir «implementa». Los mismos textos sirven en otro CLI.

## E01

```text
Lee AGENTS.md, LABORATORIOS.md y PoliticaActivities.java. Estamos en C10-E01.
No leas ../solucion ni edites tests u otros archivos. Primero explica
StartToClose vs. ScheduleToClose y propón un plan de 3 pasos. Espera
«implementa» antes de editar. Mantén 5 s y 15 s; máximo 3 intentos,
intervalo inicial 200 ms, backoff 2 y máximo 1 s. VALIDACION, RECHAZO_BANCO
 y FONDOS_INSUFICIENTES no se reintentan. Mantén persistencia() intacto.
Después ejecuta ./mvnw -Plab-e01 test y ./mvnw test. Muestra diff y explica
por qué dos fallos transitorios terminan en el intento 3.
```

## E02

```text
Lee AGENTS.md, LABORATORIOS.md, Idempotencia.java y BankStore.java.
E01 está completo. No leas ../solucion ni edites tests. Primero explica
por qué una respuesta perdida puede duplicar crédito; propón un plan
solo para TODO C10-E02. Espera «implementa» para editar. Devuelve recibo
persistido para misma clave y payload (transferId, cuentaId, monto,
direccion); con payload distinto lanza ErrorNegocio CLAVE_REUTILIZADA.
No apliques dinero otra vez ni uses memoria volátil. Conserva locks,
transacciones y restricciones. Ejecuta ./mvnw -Plab-e02 test y ./mvnw test;
muestra diff, y explica concurrencia y reinicio del almacenamiento.
```

## E03

```text
Lee AGENTS.md, LABORATORIOS.md, ReversaWorkflowImpl.java,
TransferenciaWorkflowImpl.java, BankActivities.java y Modelos.java.
E01/E02 completos. No leas ../solucion ni edites tests o contratos.
Explica reversa vs. compensación; propón flujo con estados de error.
Espera «implementa». Completa solo TODO C10-E03 y activa disponible().
Workflow independiente: obtener original por Activity, exigir COMPLETADA
sin liquidar, debitar destinatario y acreditar origen. Claves estables
distintas; preservar movimientos/estado original; registrar reversa por
separado. Si ya está reversada, devolver resultado. Ante fallo, recuperar
recibo. Solo rechazo definitivo con ausencia confirmada permite compensar
 o rechazar. Consulta fallida/efecto incierto: PENDIENTE_REVISION sin
 devolver dinero automáticamente. Si crédito al origen se rechaza,
compensar débito de reversa al destinatario con otro comando estable.
Workflow determinista; toda E/S en Activities. Ejecuta ./mvnw -Plab-e03 test
 y ./mvnw -Plaboratorios verify. Muestra diff, estados y evidencia.
```

## Corrección

```text
Comando y error real: [pegar salida]. Explica causa y diferencia respecto
al resultado del lab. Corrige solo archivo autorizado; no cambies asserts
ni desactives tests. Repite test afectado y regresión base.
```

## Variante propia

```text
Variante: [regla concreta y resultado esperado]. Documenta primero
TODO(C10-VARIANTE) con requisito, archivo y prueba. Explica cómo conserva
monto total, claves y movimientos. Propón cambio pequeño y prueba de
comportamiento; espera mi instrucción para implementarlo.
```

Referencia CLI interactivo: https://learn.chatgpt.com/docs/cli (verificada 2026-10-08).
