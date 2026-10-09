# Prompts para CLI de IA · clase 12

Iniciar `codex` en `clase-12/ejercicios`. Los mismos contratos sirven para otro CLI con acceso al proyecto. Salir del CLI antes de ejecutar la secuencia de build/reinicio en la shell. No omitir revisión del diff.

## E01 · plan para pegar

```text
Actúa como tutor de la clase 12 de BancoRed. Lee README.md, LABORATORIOS.md y el test C12E01Test. Trabajamos SOLO TODO(C12-E01) en src/main/java/com/bancared/clase10/PoliticaAprobacion.java.
No consultes ../solucion/, no cambies tests ni dependencias y no rehagas pantallas. La base oficial contiene los incrementos anteriores completos.
Relaciona esta teoría con el código: La validación del servidor protege el contrato aunque se omitan controles del navegador. La espera no reserva fondos.
Primero explica el flujo actual y presenta un plan de 3–5 pasos, los invariantes y las pruebas que deben cambiar de rojo a verde. Espera mi instrucción «implementa» antes de editar.
```

Después de explicar el plan:

```text
Implementa únicamente TODO(C12-E01). Ejecuta ./mvnw -Plab-e01 test y explica el resultado. Revisa git diff del archivo. No alteres pruebas ni escondas errores. Mantén E/S en Activities, claves estables y dinero long. Termina indicando qué verificaré en las pantallas y todos los comandos de build/reinicio de LABORATORIOS.md.
```

## E02 · plan para pegar

```text
Actúa como tutor de la clase 12 de BancoRed. Lee README.md, LABORATORIOS.md y el test C12E02Test. Trabajamos SOLO TODO(C12-E02) en src/main/java/com/bancared/clase10/AprobacionWorkflowImpl.java.
No consultes ../solucion/, no cambies tests ni dependencias y no rehagas pantallas. La base oficial contiene los incrementos anteriores completos.
Relaciona esta teoría con el código: await espera una decisión o un timer. Cancelar/rechazar/vencer finaliza sin crear la transferencia. Aprobar inicia un hijo con identidad estable.
Primero explica el flujo actual y presenta un plan de 3–5 pasos, los invariantes y las pruebas que deben cambiar de rojo a verde. Espera mi instrucción «implementa» antes de editar.
```

Después de explicar el plan:

```text
Implementa únicamente TODO(C12-E02). Ejecuta ./mvnw -Plab-e02 test y explica el resultado. Revisa git diff del archivo. No alteres pruebas ni escondas errores. Mantén E/S en Activities, claves estables y dinero long. Termina indicando qué verificaré en las pantallas y todos los comandos de build/reinicio de LABORATORIOS.md.
```

## E03 · plan para pegar

```text
Actúa como tutor de la clase 12 de BancoRed. Lee README.md, LABORATORIOS.md y el test C12E03Test. Trabajamos SOLO TODO(C12-E03) en src/main/java/com/bancared/clase10/CambiosAprobacion.java.
No consultes ../solucion/, no cambies tests ni dependencias y no rehagas pantallas. La base oficial contiene los incrementos anteriores completos.
Relaciona esta teoría con el código: Un Update devuelve el monto confirmado. Repetir un comando antiguo no deshace un cambio posterior. La misma clave con otro payload se rechaza.
Primero explica el flujo actual y presenta un plan de 3–5 pasos, los invariantes y las pruebas que deben cambiar de rojo a verde. Espera mi instrucción «implementa» antes de editar.
```

Después de explicar el plan:

```text
Implementa únicamente TODO(C12-E03). Ejecuta ./mvnw -Plab-e03 test y explica el resultado. Revisa git diff del archivo. No alteres pruebas ni escondas errores. Mantén E/S en Activities, claves estables y dinero long. Termina indicando qué verificaré en las pantallas y todos los comandos de build/reinicio de LABORATORIOS.md.
```
