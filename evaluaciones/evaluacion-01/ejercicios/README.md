# Evaluación 01: gestor básico de solicitudes SIGEO

Este es el código de inicio de la prueba práctica. Contiene las firmas requeridas, `TODO(EV01-Exx)` y pruebas públicas que describen el comportamiento esperado.

## Reglas de trabajo

1. No modifique `GestorSolicitudesTest.java` ni agregue dependencias.
2. Trabaje en pasos pequeños y ejecute la prueba indicada después de cada cambio.
3. Cree `PruebasPropiasTest.java` con al menos dos casos adicionales relevantes.
4. Registre en `PROMPTS.md` cómo utilizó Codex o Copilot y qué decisiones tomó usted.
5. La entrega final debe aprobar `./mvnw -q test`.

## Preparación

```bash
java --version
./mvnw -v
./mvnw -q test
```

La primera ejecución debe compilar, pero varias pruebas fallarán porque los métodos contienen implementaciones provisionales. Eso es esperado.

En Windows use `mvnw.cmd` en lugar de `./mvnw`.

## Ruta sugerida por pruebas

### Paso 1: prioridad y modelo

Complete `Prioridad` y las validaciones del constructor compacto de `Solicitud`.

```bash
./mvnw -q -Dtest=GestorSolicitudesTest#prioridadesExponenSusHorasDeAtencion test
./mvnw -q -Dtest=GestorSolicitudesTest#rechazaCamposDeTextoEnBlanco test
```

### Paso 2: registrar y buscar

Implemente `registrar` y `buscarPorId`, incluidos los errores controlados.

```bash
./mvnw -q -Dtest=GestorSolicitudesTest#registraYBuscaUnaSolicitudPorId test
./mvnw -q -Dtest=GestorSolicitudesTest#rechazaUnIdDuplicadoConMensajeContextual test
```

### Paso 3: filtrar y contar

Use colecciones y Streams sin exponer la lista interna.

```bash
./mvnw -q -Dtest=GestorSolicitudesTest#filtraPorPrioridadSinExponerLaListaInterna test
./mvnw -q -Dtest=GestorSolicitudesTest#cuentaLasSolicitudesObservadasPorPrioridad test
```

### Paso 4: exportar

Genere un archivo UTF-8 con un encabezado y una línea legible por solicitud.

```bash
./mvnw -q -Dtest=GestorSolicitudesTest#exportaUnReporteUtf8ConEncabezadoYUnaLineaPorSolicitud test
```

### Paso 5: pruebas propias y cierre

Cree `src/test/java/com/sigeo/evaluacion01/PruebasPropiasTest.java`, agregue dos casos que no dupliquen mecánicamente los públicos y ejecute todo:

```bash
./mvnw -q test
git diff
git status
```

## Si usa Codex o Copilot

Pídale un solo incremento por vez. Un ejemplo útil:

> Revisa únicamente el TODO EV01-E02 y la prueba que estoy intentando hacer pasar. Antes de editar, explícame el contrato y propón el cambio mínimo. Después ejecuta solo ese test, muéstrame el diff y detente.

Usted debe poder explicar cada cambio y realizar una modificación breve durante la defensa.

