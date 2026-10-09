# Prompts para CLI de IA · clase 14

Iniciar `codex` en `clase-14/ejercicios`. Los mismos contratos sirven para otro CLI con acceso al proyecto. Salir del CLI antes de ejecutar la secuencia de build/reinicio en la shell. No omitir revisión del diff.

## E01 · plan para pegar

```text
Actúa como tutor de la clase 14 de BancoRed. Lee README.md, LABORATORIOS.md y el test C14E01Test. Trabajamos SOLO TODO(C14-E01) en src/main/java/com/bancared/clase10/OutboxStore.java.
No consultes ../solucion/, no cambies tests ni dependencias y no rehagas pantallas. La base oficial contiene los incrementos anteriores completos.
Relaciona esta teoría con el código: El estado y el evento se escriben en una transacción local. Publicar no equivale a confirmar; si falta confirmación, el evento permanece pendiente y puede repetirse.
Primero explica el flujo actual y presenta un plan de 3–5 pasos, los invariantes y las pruebas que deben cambiar de rojo a verde. Espera mi instrucción «implementa» antes de editar.
```

Después de explicar el plan:

```text
Implementa únicamente TODO(C14-E01). Ejecuta ./mvnw -Plab-e01 test y explica el resultado. Revisa git diff del archivo. No alteres pruebas ni escondas errores. Mantén E/S en Activities, claves estables y dinero long. Termina indicando qué verificaré en las pantallas y todos los comandos de build/reinicio de LABORATORIOS.md.
```

## E02 · plan para pegar

```text
Actúa como tutor de la clase 14 de BancoRed. Lee README.md, LABORATORIOS.md y el test C14E02Test. Trabajamos SOLO TODO(C14-E02) en src/main/java/com/bancared/clase10/InboxStore.java.
No consultes ../solucion/, no cambies tests ni dependencias y no rehagas pantallas. La base oficial contiene los incrementos anteriores completos.
Relaciona esta teoría con el código: El inbox registra el evento y la notificación en una transacción. Repetir mismo ID/payload recupera el resultado. Otro payload con igual ID se rechaza.
Primero explica el flujo actual y presenta un plan de 3–5 pasos, los invariantes y las pruebas que deben cambiar de rojo a verde. Espera mi instrucción «implementa» antes de editar.
```

Después de explicar el plan:

```text
Implementa únicamente TODO(C14-E02). Ejecuta ./mvnw -Plab-e02 test y explica el resultado. Revisa git diff del archivo. No alteres pruebas ni escondas errores. Mantén E/S en Activities, claves estables y dinero long. Termina indicando qué verificaré en las pantallas y todos los comandos de build/reinicio de LABORATORIOS.md.
```

## E03 · plan para pegar

```text
Actúa como tutor de la clase 14 de BancoRed. Lee README.md, LABORATORIOS.md y el test C14E03Test. Trabajamos SOLO TODO(C14-E03) en src/main/java/com/bancared/clase10/PoliticaMensaje.java.
No consultes ../solucion/, no cambies tests ni dependencias y no rehagas pantallas. La base oficial contiene los incrementos anteriores completos.
Relaciona esta teoría con el código: Un mensaje con esquema inválido no se arregla reintentándolo. Se aparta en DLQ y se conserva evidencia; un consumidor nunca decide mover dinero.
Primero explica el flujo actual y presenta un plan de 3–5 pasos, los invariantes y las pruebas que deben cambiar de rojo a verde. Espera mi instrucción «implementa» antes de editar.
```

Después de explicar el plan:

```text
Implementa únicamente TODO(C14-E03). Ejecuta ./mvnw -Plab-e03 test y explica el resultado. Revisa git diff del archivo. No alteres pruebas ni escondas errores. Mantén E/S en Activities, claves estables y dinero long. Termina indicando qué verificaré en las pantallas y todos los comandos de build/reinicio de LABORATORIOS.md.
```
