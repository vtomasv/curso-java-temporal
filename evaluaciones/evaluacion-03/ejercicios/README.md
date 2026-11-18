# Evaluación 3 · SICOR

**Prueba práctica individual** · III CRIM · Mención Comunicaciones e Informática

**210 minutos** · Ponderación 30 % · 100 puntos · Aprobación con 60 puntos · Clases 1 a 13

### Objetivo

Complete el Sistema de Comunicaciones y Órdenes Resilientes (SICOR). Una orden válida debe llegar a sus unidades ficticias solo después de la autorización correspondiente. El sistema debe controlar quién puede leer el contenido, registrar las lecturas autorizadas y conservar un resultado verificable cuando una entrega se confirma, vence o se cancela.

**Qué se entrega.** Un único ZIP con el proyecto fuente `ejercicios/`, sus pruebas y los archivos para ejecutarlo. El docente lo descomprimirá, ejecutará Maven y probará SICOR. La justificación se escribe junto al código. No se solicitan capturas, bitácoras, registros de conversaciones con IA ni informes. El Word es material de consulta.

### Cómo se calificará

| Ítem | Qué comprobará el docente en el proyecto | Puntos |
|---|---|---:|
| E01 | Construcción de órdenes válidas y transiciones de estado | 10 |
| E02 | Acceso según rol y clasificación, privacidad del contenido | 15 |
| E03 | Reautenticación vinculada a la persona y a la orden | 15 |
| E04 | Firmas de personas habilitadas y despacho en el momento correcto | 15 |
| E05 | Un solo efecto de entrega por intención, incluso ante reintentos | 15 |
| E06 | Orquestación durable y determinista de los destinos | 15 |
| E07 | Confirmación, cancelación y tratamiento de resultados inciertos | 10 |
| Transversal | Coherencia entre decisiones, implementación y pruebas propias | 5 |
| **Total** | | **100** |

Trabaje sobre la plantilla. Conserve los paquetes, las firmas públicas y las pruebas proporcionadas. Puede agregar clases auxiliares y pruebas. Use las dependencias del proyecto. La guía `solucion/SOLUCION_GUIADA.md` contiene pistas autorizadas. Puede usar IA para comparar alternativas o proponer pruebas. Usted es responsable de adaptar, comprender y verificar su código. Use sólo datos ficticios.

**Distribución sugerida.** Reserve 15 minutos para verificar el entorno, 70 para E01–E03, 80 para E04–E06, 25 para E07 y 20 para terminar sus pruebas, verificar el ZIP y entregarlo. El docente podrá ejecutar pruebas adicionales que respeten estos contratos.

## 1. Preparar y ejecutar SICOR

Necesita **JDK 25**, Temporal CLI y dos terminales. La plantilla incluye Maven Wrapper y utiliza Spring Boot 4.1.0, Temporal Java SDK 1.37.0 y H2 en memoria. La primera ejecución de Maven requiere conexión para descargar las dependencias que falten.

1. Descomprima el material. Entre en `ejercicios/`, la carpeta que contiene `pom.xml`. Desde la raíz del repositorio, la ruta es `evaluaciones/evaluacion-03/ejercicios`.
2. Compruebe que Java y el Wrapper indican versión **25**. Si indican otra, ajuste `JAVA_HOME` y el `PATH` de esa terminal. En Windows PowerShell sustituya `./mvnw` por `.\mvnw.cmd` en todos los comandos. En macOS/Linux, si el ZIP perdió el permiso de ejecución, use `chmod +x mvnw`.
3. Ejecute la línea base antes de modificar el código. Debe terminar con `BUILD SUCCESS`. Las pruebas de las misiones y las propias se activan por perfil. La línea base usa un servidor de pruebas y puede ejecutarse con SICOR detenido.

```text
java -version
temporal --version
./mvnw -version
./mvnw clean verify
```

### Iniciar la aplicación

**Terminal 1.** Inicie Temporal y manténgalo activo.

```text
temporal server start-dev --ip 127.0.0.1 --port 7233 --ui-port 8233
```

**Terminal 2.** Desde `ejercicios/`, ejecute `./mvnw spring-boot:run`. Abra SICOR en `http://localhost:8080` y la interfaz de Temporal en `http://localhost:8233`. Spring conecta con el servidor en `127.0.0.1:7233` y registra el Worker en la cola `SICOR-EV03`.

### Si algún puerto está ocupado

Puede usar tres puertos libres. Por ejemplo, en la terminal 1, repita el comando de arranque de Temporal con `--port 17233 --ui-port 18233`. En la terminal 2 use las dos líneas de su sistema antes de iniciar SICOR.

```text
# macOS / Linux
export SICOR_TEMPORAL_TARGET=127.0.0.1:17233
export SERVER_PORT=18080
# Windows PowerShell
$env:SICOR_TEMPORAL_TARGET="127.0.0.1:17233"
$env:SERVER_PORT="18080"
```

En ese ejemplo, SICOR queda en `http://localhost:18080` y Temporal en `http://localhost:18233`. Si Spring no conecta, compruebe que el puerto de `SICOR_TEMPORAL_TARGET` coincide con `--port`. Para repetir desde cero, detenga sus dos procesos con Ctrl+C y vuelva a iniciarlos, primero Temporal y luego SICOR. Ambos almacenan los datos de esta práctica en memoria.

## 2. Decisiones, modelo y acceso

En `src/main/java/com/enlace/evaluacion03/config/Decisiones.java` hay ocho constantes para siete misiones. E07 contiene dos. Los tipos de comunicación son INFORMATIVA, INSTRUCCION y ORDEN. Para cada misión, lea su clase `E0x...Test` en `src/test/java/com/enlace/evaluacion03/`, compare las alternativas y complete los `TODO(EV03-...)` de su camino. Los TODO de los caminos descartados pueden permanecer en la plantilla.

Junto a cada decisión, complete el comentario `DECISION(EV03-Exx)`. Indique la opción elegida, la alternativa descartada, una consecuencia concreta y el nombre de una prueba que compruebe el resultado. Escriba siete comentarios en total. El de E07 debe justificar ambas constantes. Puede usar este formato.

```java
// DECISION(EV03-E04): Elegí [opción] porque [regla del caso].
// Descarté [alternativa]. Acepto [costo o limitación].
// Lo compruebo en [nombre de la prueba].
```

### E01 · Modelo e invariantes · 10 puntos

En `Orden`, elija `INVARIANTES_EN_ENTIDAD` o `POLITICA_DE_DOMINIO`. Proteja la construcción directa y la creación desde la web. El contenido admite entre 20 y 2000 caracteres inclusive. Debe haber al menos un destinatario y cualquier duplicado debe rechazarse antes de convertir la colección a conjunto. Preserve las transiciones de estado. Una entrada inválida no debe persistir ni iniciar Workflows. Una válida inicia `despacho-<ordenId>` y `accesos-<ordenId>`.

### E02 · Acceso y confidencialidad · 15 puntos

En `PoliticaAcceso`, elija `RBAC_CENTRAL` o `ESTRATEGIA_POR_CLASIFICACION`. Una sesión sin autenticar no puede leer el contenido. Aplique la siguiente matriz. Todas las cuentas indicadas son ficticias y su contraseña de laboratorio es `laboratorio`.

| Rol | Cuentas | INTERNA | RESERVADA | MAXIMA |
|---|---|---|---|---|
| OPERADOR | `operador` | Sí | No | No |
| OFICIAL | `oficial1`, `oficial2` | Sí | Sí | No |
| COMANDANTE | `comandante1`, `comandante2` | Sí | Sí | Sí* |
| AUDITOR | `auditor` | Sí | Sí | Sí* |

(*) La lectura MAXIMA requiere la reautenticación de E03. La bandeja muestra metadatos. Mantenga el cuerpo de la orden fuera de `OrdenResumen`, URLs, logs, errores y del historial de Temporal (History). Una lectura autorizada registra un Update `registrarAcceso`. Una denegación no registra una lectura. Reintentar el mismo identificador con los mismos datos cuenta una vez. Reutilizarlo con otros datos debe rechazar sólo ese Update. La auditoría permanece disponible después de terminar el despacho.

## 3. Reautenticación, firmas y entrega

### E03 · Reautenticación de MAXIMA · 15 puntos

En `ReautenticacionService`, elija `POR_CADA_LECTURA` o `VENTANA_CORTA_POR_ORDEN`. Antes de mostrar el contenido, compruebe otra vez el usuario y la contraseña de la sesión actual. Vincule el permiso al par usuario–orden. En el primer camino, el permiso se consume una vez. En el segundo, use la ventana breve de la plantilla (dos minutos) y compruebe su expiración.

Una identidad distinta, una contraseña incorrecta o un permiso para otra orden no deben revelar el contenido. Sólo una lectura autorizada de MAXIMA envía `reautenticado=true`. La contraseña se usa en la autenticación y queda fuera de la URL, la sesión, los logs y el History.

### E04 · Firmas múltiples · 15 puntos

En `PoliticaFirmas`, elija `ROLES_FIJOS` o `UMBRAL_CONFIGURABLE`. Sólo OFICIAL y COMANDANTE pueden ser firmantes. Defina la regla para cada tipo y confidencialidad. El caso de verificación común es una orden de tipo `ORDEN` con clasificación `MAXIMA`.

| Camino | Resultado que debe obtener en ese caso |
|---|---|
| `ROLES_FIJOS` | Un OFICIAL y un COMANDANTE distintos. Cada rol cubre un puesto. Un segundo comandante deja el avance en 1/2 mientras falta el oficial. |
| `UMBRAL_CONFIGURABLE` | Dos personas distintas de un rol habilitado por su tabla pueden completar el umbral. Identifique en el código el mínimo y los roles admitidos. |

La acción **Firmar despacho** de la bandeja autoriza el envío a partir de los metadatos. Un oficial puede firmar allí si E04 lo habilita, aunque E02 le impida abrir el contenido MAXIMA. Firmar desde la bandeja no registra una lectura. El auditor puede leer según E02 y E03, pero no firma.

Cada firma entra por el Update `registrarFirma` y una Activity idempotente la persiste. Conserve el validador y la serialización de Updates. La misma persona sólo cuenta una vez. Las solicitudes concurrentes o rechazadas deben dejar el Workflow operativo. Hasta completar la política, el despacho espera sin emitir recibos. Al completarla, pasa por `LISTA_PARA_ENVIO` e inicia el envío automáticamente una sola vez.

### E05 · Entrega idempotente · 15 puntos

En `DespachoActivitiesImpl`, elija `CLAVE_ORDEN_UNIDAD` o `COMANDO_EXPLICITO`. Dos invocaciones con la misma intención deben devolver el mismo recibo y producir un único efecto. En el primer camino, la identidad es el par orden–unidad. En el segundo, el llamador conserva el mismo `comandoId` entre intentos y se rechaza su reutilización con otra orden o unidad. Pruebe el reintento desde un Workflow.

## 4. Despacho y pruebas propias

### E06 · Orquestación durable · 15 puntos

En `DespachoWorkflowImpl`, elija `SECUENCIAL`, `PARALELA` o `CHILD_WORKFLOW_POR_UNIDAD`. Entregue una vez por destino y conserve una clave estable por intención. En secuencia, espere cada entrega antes de la siguiente. En paralelo, use `Async` y `Promise` de Temporal. Con hijos, cree un Child Workflow con ID estable por orden y unidad.

El código de Workflow debe reproducirse de forma determinista. Las Activities realizan la entrada/salida y los efectos externos. Mantenga threads Java, `CompletableFuture`, `Instant.now()`, `UUID.randomUUID()` y clientes Spring fuera de los Workflows.

### E07 · Confirmación e incertidumbre · 10 puntos

Complete dos constantes en `Decisiones.java` e implemente el comportamiento en `DespachoWorkflowImpl`.

| Decisión | Alternativas y comportamiento esperado |
|---|---|
| `CONFIRMACION` | `TODAS_LAS_UNIDADES` exige todos los destinos. `QUORUM_CONFIGURABLE` usa un umbral explícito y justificable. Con tres unidades, dos confirmaciones alcanzan el quórum. |
| `INCERTIDUMBRE` | `REVISION_MANUAL` termina en `PENDIENTE_REVISION` sin compensar. `COMPENSAR_SOLO_CONFIRMADOS` termina en ese mismo estado y compensa exclusivamente los recibos confirmados. |

Valide cada Signal con la unidad y el identificador de su recibo. Una confirmación repetida cuenta una sola vez. La Query informa el progreso sin modificarlo. El Update de cancelación acepta una solicitud válida y termina en `CANCELADA`. Si se alcanza la confirmación, termina en `ENTREGADA`. Si vencen las 12 horas del Workflow, aplique la política de incertidumbre. Pruebe el plazo con el reloj virtual de Temporal.

### Tres pruebas propias obligatorias

Reemplace los tres ejemplos de `PruebasPropiasTest.java` por al menos tres pruebas activas sobre **tres decisiones distintas**. Identifique la decisión en un comentario. Cada prueba debe incluir una aserción observable, ser determinista y fallar si se rompe la regla. Puede probar, por ejemplo, el aislamiento entre dos órdenes, un reintento de entrega o un Signal duplicado. Use el tiempo virtual cuando corresponda y evite `Thread.sleep`.

Para probar una misión, use `./mvnw -Pev03-e04 test` sustituyendo `e04` por `e01` a `e07`. Para ejecutar únicamente sus pruebas, use `./mvnw -Pev03-all -Dtest=PruebasPropiasTest test`. La plantilla conserva tres pruebas que fallan deliberadamente en el perfil final hasta que las reemplace por sus casos.

## 5. Probar el recorrido y entregar

Con la solución implementada, inicie Temporal y SICOR como en la sección 1. Use dos órdenes distintas, una por cada desenlace. Si repite la práctica en la misma sesión, elija IDs y claves de creación nuevos.

1. **Crear.** Ingrese como `operador`. Cree dos órdenes de tipo `ORDEN` y nivel `MAXIMA`, por ejemplo `ORD-101` y `ORD-102`, con claves `CMD-101` y `CMD-102`. Use asunto de 5 a 120 caracteres, contenido ficticio de 20 a 2000 y una unidad por orden. Volverá a la bandeja. Para cada ID busque `despacho-` y `accesos-` en Temporal. El despacho debe estar esperando firmas.
2. **Probar permisos.** Como `oficial1`, pulse **Abrir** en una orden máxima. Debe obtener acceso denegado. Como `auditor`, abra la misma orden y confirme su propia identidad. La Query `consultarAccesos` debe contar la lectura autorizada. Un intento fallido no incrementa el contador.
3. **Firmar.** Cambie de cuenta con **Cerrar sesión**. En `ROLES_FIJOS`, firme ambas órdenes desde la bandeja como `comandante1` y luego como `oficial1`. Entre ambas firmas, `comandante2` no tiene un puesto pendiente. En `UMBRAL_CONFIGURABLE`, use dos cuentas distintas del rol habilitado por su tabla. Antes de la última firma habrá cero recibos. Después, la Query `consultarFirmas` indica que la política está completa y la entrega genera un recibo por destino.
4. **Confirmar la primera y cancelar la segunda.** Como `comandante1`, abra la primera orden, reautentíquese cuando se le solicite y pulse **Enviar Signal** en el recibo. Compruebe `ENTREGADA`. Abra la segunda, aún en despacho, indique un motivo y pulse **Cancelar por Update**. Compruebe `CANCELADA`. Si el recibo todavía no aparece, actualice el detalle. La auditoría de ambas órdenes sigue disponible.
5. **Inspeccionar.** En la interfaz de Temporal, abra el History de ambos Workflows. Compruebe el orden de las firmas y entregas, la confirmación o cancelación y la ausencia de contraseñas y cuerpos de órdenes. Por CLI puede usar `temporal workflow show --workflow-id despacho-ORD-101` y repetir con `accesos-ORD-101`. Con puertos alternativos, añada `--address 127.0.0.1:17233` a cada comando de consulta.

### Verificación y única entrega

Ejecute la verificación completa desde `ejercicios/`.

```text
./mvnw clean verify -Pev03-all
```

El resultado debe ser **BUILD SUCCESS**. Compruebe que ninguna de las ocho constantes está en `PENDIENTE`, que hay siete comentarios de decisión completos y que pasaron sus tres pruebas propias. En el camino `CLAVE_ORDEN_UNIDAD` la prueba exclusiva de comando explícito se omite por diseño.

Comprima la carpeta **ejercicios/**. Incluya `pom.xml`, `mvnw`, `mvnw.cmd`, la carpeta oculta `.mvn/`, `src/main/java/`, `src/main/resources/` y `src/test/java/`. Excluya `target/`, `.git/`, archivos del IDE y credenciales personales. Entregue ese ZIP por el canal del examen. Para comprobar el paquete, descomprímalo en otra carpeta y repita allí la verificación final.
