# Prompts para CLI de IA · clase 13

Iniciar `codex` en `clase-13/ejercicios`. Los mismos contratos sirven para otro CLI con acceso al proyecto. Salir del CLI antes de ejecutar la secuencia de build/reinicio en la shell. No omitir revisión del diff.

## E01 · plan para pegar

```text
Actúa como tutor de la clase 13 de BancoRed. Lee README.md, LABORATORIOS.md y el test C13E01Test. Trabajamos SOLO TODO(C13-E01) en src/main/java/com/bancared/clase10/CalculoNeto.java.
No consultes ../solucion/, no cambies tests ni dependencias y no rehagas pantallas. La base oficial contiene los incrementos anteriores completos.
Relaciona esta teoría con el código: Una transferencia de A a B aumenta la posición de B y reduce la de A. Dos direcciones se netean. Una reversa completada cancela el aporte del original.
Primero explica el flujo actual y presenta un plan de 3–5 pasos, los invariantes y las pruebas que deben cambiar de rojo a verde. Espera mi instrucción «implementa» antes de editar.
```

Después de explicar el plan:

```text
Implementa únicamente TODO(C13-E01). Ejecuta ./mvnw -Plab-e01 test y explica el resultado. Revisa git diff del archivo. No alteres pruebas ni escondas errores. Mantén E/S en Activities, claves estables y dinero long. Termina indicando qué verificaré en las pantallas y todos los comandos de build/reinicio de LABORATORIOS.md.
```

## E02 · plan para pegar

```text
Actúa como tutor de la clase 13 de BancoRed. Lee README.md, LABORATORIOS.md y el test C13E02Test. Trabajamos SOLO TODO(C13-E02) en src/main/java/com/bancared/clase10/SeleccionLote.java.
No consultes ../solucion/, no cambies tests ni dependencias y no rehagas pantallas. La base oficial contiene los incrementos anteriores completos.
Relaciona esta teoría con el código: Un cierre toma un snapshot bajo lock. Cada transferencia pertenece a un solo lote abierto; se excluyen efectos inciertos y reversas en curso.
Primero explica el flujo actual y presenta un plan de 3–5 pasos, los invariantes y las pruebas que deben cambiar de rojo a verde. Espera mi instrucción «implementa» antes de editar.
```

Después de explicar el plan:

```text
Implementa únicamente TODO(C13-E02). Ejecuta ./mvnw -Plab-e02 test y explica el resultado. Revisa git diff del archivo. No alteres pruebas ni escondas errores. Mantén E/S en Activities, claves estables y dinero long. Termina indicando qué verificaré en las pantallas y todos los comandos de build/reinicio de LABORATORIOS.md.
```

## E03 · plan para pegar

```text
Actúa como tutor de la clase 13 de BancoRed. Lee README.md, LABORATORIOS.md y el test C13E03Test. Trabajamos SOLO TODO(C13-E03) en src/main/java/com/bancared/clase10/LiquidacionWorkflowImpl.java.
No consultes ../solucion/, no cambies tests ni dependencias y no rehagas pantallas. La base oficial contiene los incrementos anteriores completos.
Relaciona esta teoría con el código: La Saga mueve liquidez del banco deudor al acreedor. Una compensación crea un asiento nuevo. Un timeout requiere confirmar recibo antes de devolver liquidez.
Primero explica el flujo actual y presenta un plan de 3–5 pasos, los invariantes y las pruebas que deben cambiar de rojo a verde. Espera mi instrucción «implementa» antes de editar.
```

Después de explicar el plan:

```text
Implementa únicamente TODO(C13-E03). Ejecuta ./mvnw -Plab-e03 test y explica el resultado. Revisa git diff del archivo. No alteres pruebas ni escondas errores. Mantén E/S en Activities, claves estables y dinero long. Termina indicando qué verificaré en las pantallas y todos los comandos de build/reinicio de LABORATORIOS.md.
```
