# Prompts para CLI de IA · clase 15

Iniciar `codex` en `clase-15/ejercicios`. Los mismos contratos sirven para otro CLI con acceso al proyecto. Salir del CLI antes de ejecutar la secuencia de build/reinicio en la shell. No omitir revisión del diff.

## E01 · plan para pegar

```text
Actúa como tutor de la clase 15 de BancoRed. Lee README.md, LABORATORIOS.md y el test C15E01Test. Trabajamos SOLO TODO(C15-E01) en src/main/java/com/bancared/clase10/AsistenteWorkflowImpl.java.
No consultes ../solucion/, no cambies tests ni dependencias y no rehagas pantallas. La base oficial contiene los incrementos anteriores completos.
Relaciona esta teoría con el código: Agregar una Activity cambia los comandos del Workflow. getVersion mantiene la ruta de historias antiguas y registra la nueva rama en ejecuciones nuevas.
Primero explica el flujo actual y presenta un plan de 3–5 pasos, los invariantes y las pruebas que deben cambiar de rojo a verde. Espera mi instrucción «implementa» antes de editar.
```

Después de explicar el plan:

```text
Implementa únicamente TODO(C15-E01). Ejecuta ./mvnw -Plab-e01 test y explica el resultado. Revisa git diff del archivo. No alteres pruebas ni escondas errores. Mantén E/S en Activities, claves estables y dinero long. Termina indicando qué verificaré en las pantallas y todos los comandos de build/reinicio de LABORATORIOS.md.
```

## E02 · plan para pegar

```text
Actúa como tutor de la clase 15 de BancoRed. Lee README.md, LABORATORIOS.md y el test C15E02Test. Trabajamos SOLO TODO(C15-E02) en src/main/java/com/bancared/clase10/RespuestaSegura.java.
No consultes ../solucion/, no cambies tests ni dependencias y no rehagas pantallas. La base oficial contiene los incrementos anteriores completos.
Relaciona esta teoría con el código: El contrato valida estructura y pertenencia de fuentes al contexto, pero no prueba por sí solo la veracidad semántica. El fallback usa texto recuperado; la IA no dispone de herramientas financieras.
Primero explica el flujo actual y presenta un plan de 3–5 pasos, los invariantes y las pruebas que deben cambiar de rojo a verde. Espera mi instrucción «implementa» antes de editar.
```

Después de explicar el plan:

```text
Implementa únicamente TODO(C15-E02). Ejecuta ./mvnw -Plab-e02 test y explica el resultado. Revisa git diff del archivo. No alteres pruebas ni escondas errores. Mantén E/S en Activities, claves estables y dinero long. Termina indicando qué verificaré en las pantallas y todos los comandos de build/reinicio de LABORATORIOS.md.
```

## E03 · plan para pegar

```text
Actúa como tutor de la clase 15 de BancoRed. Lee README.md, LABORATORIOS.md y el test C15E03Test. Trabajamos SOLO TODO(C15-E03) en src/main/java/com/bancared/clase10/ResumenOperativo.java.
No consultes ../solucion/, no cambies tests ni dependencias y no rehagas pantallas. La base oficial contiene los incrementos anteriores completos.
Relaciona esta teoría con el código: Las métricas resumen hechos de negocio. El volumen bruto no es saldo ni neto. La defensa exige explicar invariantes, fallos y límites con evidencias del código.
Primero explica el flujo actual y presenta un plan de 3–5 pasos, los invariantes y las pruebas que deben cambiar de rojo a verde. Espera mi instrucción «implementa» antes de editar.
```

Después de explicar el plan:

```text
Implementa únicamente TODO(C15-E03). Ejecuta ./mvnw -Plab-e03 test y explica el resultado. Revisa git diff del archivo. No alteres pruebas ni escondas errores. Mantén E/S en Activities, claves estables y dinero long. Termina indicando qué verificaré en las pantallas y todos los comandos de build/reinicio de LABORATORIOS.md.
```
