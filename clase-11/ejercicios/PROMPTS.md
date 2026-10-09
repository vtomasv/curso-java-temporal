# Prompts para CLI de IA · clase 11

Iniciar `codex` en `clase-11/ejercicios`. Los mismos contratos sirven para otro CLI con acceso al proyecto. Salir del CLI antes de ejecutar la secuencia de build/reinicio en la shell. No omitir revisión del diff.

## E01 · plan para pegar

```text
Actúa como tutor de la clase 11 de BancoRed. Lee README.md, LABORATORIOS.md y el test C11E01Test. Trabajamos SOLO TODO(C11-E01) en src/main/java/com/bancared/clase10/PoliticaCumple.java.
No consultes ../solucion/, no cambies tests ni dependencias y no rehagas pantallas. La base oficial contiene los incrementos anteriores completos.
Relaciona esta teoría con el código: El cumpleaños compara mes y día. El contrato fija el 28/02 para nacidos el 29/02 en años no bisiestos.
Primero explica el flujo actual y presenta un plan de 3–5 pasos, los invariantes y las pruebas que deben cambiar de rojo a verde. Espera mi instrucción «implementa» antes de editar.
```

Después de explicar el plan:

```text
Implementa únicamente TODO(C11-E01). Ejecuta ./mvnw -Plab-e01 test y explica el resultado. Revisa git diff del archivo. No alteres pruebas ni escondas errores. Mantén E/S en Activities, claves estables y dinero long. Termina indicando qué verificaré en las pantallas y todos los comandos de build/reinicio de LABORATORIOS.md.
```

## E02 · plan para pegar

```text
Actúa como tutor de la clase 11 de BancoRed. Lee README.md, LABORATORIOS.md y el test C11E02Test. Trabajamos SOLO TODO(C11-E02) en src/main/java/com/bancared/clase10/CumpleWorkflowImpl.java.
No consultes ../solucion/, no cambies tests ni dependencias y no rehagas pantallas. La base oficial contiene los incrementos anteriores completos.
Relaciona esta teoría con el código: El Workflow decide el orden. Las Activities consultan bancos y reemplazan las marcas de forma atómica; el reloj real está fuera del Workflow.
Primero explica el flujo actual y presenta un plan de 3–5 pasos, los invariantes y las pruebas que deben cambiar de rojo a verde. Espera mi instrucción «implementa» antes de editar.
```

Después de explicar el plan:

```text
Implementa únicamente TODO(C11-E02). Ejecuta ./mvnw -Plab-e02 test y explica el resultado. Revisa git diff del archivo. No alteres pruebas ni escondas errores. Mantén E/S en Activities, claves estables y dinero long. Termina indicando qué verificaré en las pantallas y todos los comandos de build/reinicio de LABORATORIOS.md.
```

## E03 · plan para pegar

```text
Actúa como tutor de la clase 11 de BancoRed. Lee README.md, LABORATORIOS.md y el test C11E03Test. Trabajamos SOLO TODO(C11-E03) en src/main/java/com/bancared/clase10/AgendaCumple.java.
No consultes ../solucion/, no cambies tests ni dependencias y no rehagas pantallas. La base oficial contiene los incrementos anteriores completos.
Relaciona esta teoría con el código: Schedule dispara ejecuciones diarias en una zona horaria. La política SKIP evita solapamiento. El ciclo alternativo usa timer y Continue-As-New cada 30 noches.
Primero explica el flujo actual y presenta un plan de 3–5 pasos, los invariantes y las pruebas que deben cambiar de rojo a verde. Espera mi instrucción «implementa» antes de editar.
```

Después de explicar el plan:

```text
Implementa únicamente TODO(C11-E03). Ejecuta ./mvnw -Plab-e03 test y explica el resultado. Revisa git diff del archivo. No alteres pruebas ni escondas errores. Mantén E/S en Activities, claves estables y dinero long. Termina indicando qué verificaré en las pantallas y todos los comandos de build/reinicio de LABORATORIOS.md.
```
