# Evaluación 3 — SICOR

## Sistema de Comunicaciones y Órdenes Resilientes

SICOR es una aplicación didáctica para administrar comunicaciones y órdenes entre
unidades **completamente ficticias**. El escenario permite practicar diseño de
dominio, persistencia, seguridad y orquestación durable sin representar
procedimientos ni información operativa reales.

La plantilla se entrega en un estado ejecutable y seguro. Su objetivo no es
reemplazar toda la aplicación, sino completar siete misiones y ocho constantes
de decisión (E07 contiene dos). En cada misión deberá elegir los caminos propuestos, implementarlos,
justificarlo y demostrarlo con pruebas.

> Alcance académico: se pueden usar únicamente conceptos cubiertos hasta la
> clase 13 inclusive. No se requiere mensajería externa, OAuth, integración real
> con IA, cifrado de payloads de Temporal ni contenidos de clases posteriores.

## Resultado esperado

Al finalizar, SICOR deberá:

- crear y listar órdenes sin exponer su contenido en los resúmenes;
- aplicar una política de lectura según rol y confidencialidad;
- exigir nuevamente usuario y contraseña antes de revelar una orden de
  confidencialidad `MAXIMA`;
- aceptar firmas de personas distintas y habilitar el despacho sólo al alcanzar
  la política elegida;
- entregar una orden una sola vez por unidad, aun cuando Temporal reintente una
  Activity;
- orquestar el despacho con código determinista;
- recibir confirmaciones por Signal, informar progreso por Query y aceptar una
  cancelación controlada por Update;
- dejar en `PENDIENTE_REVISION` cualquier resultado incierto, o compensar sólo
  resultados cuya ejecución esté confirmada.

## Requisitos

- JDK 25.
- Git.
- No necesita instalar Maven: el repositorio incluye Maven Wrapper.
- Para las pruebas de Workflow se usa `TestWorkflowEnvironment`; no es
  necesario levantar un servidor Temporal externo.

Compruebe el entorno:

```bash
java -version
./mvnw -version
./mvnw clean verify
```

La última orden debe terminar con `BUILD SUCCESS`. Si la línea base falla antes
de modificar el código, registre la evidencia y comuníquelo al docente.

## Inicio rápido

```bash
./mvnw spring-boot:run
```

Abra `http://localhost:8080`. Todas las cuentas de laboratorio usan la clave
`laboratorio`:

| Usuario | Rol |
|---|---|
| `operador` | `OPERADOR` |
| `oficial1` | `OFICIAL` |
| `oficial2` | `OFICIAL` |
| `comandante1` | `COMANDANTE` |
| `comandante2` | `COMANDANTE` |
| `auditor` | `AUDITOR` |

Las credenciales, unidades y órdenes precargadas son ficticias y se usan sólo
para evaluación.

## Método de trabajo obligatorio

Para cada misión:

1. Lea el contrato y la prueba del perfil antes de cambiar código.
2. Compare al menos los dos caminos permitidos.
3. Elija un camino en `Decisiones.java`; no deje el valor `PENDIENTE`.
4. Complete sólo los `TODO(EV03-...)` correspondientes.
5. Escriba el comentario `DECISION` junto a la constante elegida.
6. Ejecute el perfil de la misión y luego toda la batería.
7. Registre la evidencia en `BITACORA.md`.

Formato mínimo del comentario:

```java
// DECISION(EV03-E04):
// Elegí: UMBRAL_CONFIGURABLE.
// Alternativa descartada: ROLES_FIJOS, porque ...
// Tradeoff aceptado: ...
// Prueba que demuestra el camino: mi_prueba_de_umbral_maximo().
public static final Firmas FIRMAS = Firmas.UMBRAL_CONFIGURABLE;
```

Una justificación debe explicar la consecuencia de la decisión. Frases como
“es más fácil”, “lo indicó la IA” o “pasa el test” no son suficientes por sí
solas.

## Misiones

### EV03-E01 — Modelo e invariantes (10 puntos)

Complete las invariantes avanzadas de `Orden` sin acoplar el dominio a Spring o
a la vista. Debe rechazar destinatarios vacíos y contenidos fuera del rango que
indican las pruebas. Mantenga las transiciones de estado válidas.

Elija:

- `INVARIANTES_EN_ENTIDAD`: la entidad protege su consistencia desde su
  constructor y métodos; o
- `POLITICA_DE_DOMINIO`: `Orden` delega la regla variable a un componente de
  dominio durante su construcción y conserva las invariantes estructurales.

Justifique dónde queda la única fuente de verdad y cómo evita crear una orden
inválida desde otro adaptador. Validar sólo en `OrdenService` no satisface el
contrato: la construcción directa también debe quedar protegida.

```bash
./mvnw -Pev03-e01 test
```

### EV03-E02 — Acceso y confidencialidad (15 puntos)

Implemente `PoliticaAcceso`. Un usuario no autenticado nunca puede leer. La
decisión debe aplicar de forma coherente la matriz de roles publicada en la
prueba y mantener el contenido fuera de `OrdenResumen`.

Elija:

- `RBAC_CENTRAL`: una matriz central evalúa rol y clasificación; o
- `ESTRATEGIA_POR_CLASIFICACION`: cada confidencialidad delega en una política
  específica.

No registre el contenido de una orden `MAXIMA` en logs, excepciones ni DTO de
listado.

```bash
./mvnw -Pev03-e02 test
```

### EV03-E03 — Reautenticación de máxima confidencialidad (15 puntos)

La vista sólo puede revelar el contenido `MAXIMA` después de comprobar otra vez
el nombre del usuario actual y su contraseña. La autorización debe estar ligada
al par usuario–orden; no puede reutilizarse para otra cuenta u otra orden.

Elija:

- `POR_CADA_LECTURA`: el permiso se consume una sola vez; o
- `VENTANA_CORTA_POR_ORDEN`: puede reutilizarse durante una ventana breve y
  expira de forma verificable.

No almacene contraseñas ni las incluya en la URL, logs o sesión.

```bash
./mvnw -Pev03-e03 test
```

### EV03-E04 — Firmas múltiples (15 puntos)

Una misma persona no puede firmar dos veces la misma orden. Sólo los roles
admitidos cuentan y el estado cambia a `LISTA_PARA_ENVIO` exactamente cuando se
alcanza el requisito.

Elija:

- `ROLES_FIJOS`: la política exige combinaciones explícitas de roles; o
- `UMBRAL_CONFIGURABLE`: una tabla de política define el número y los roles
  habilitados según tipo y confidencialidad.

Debe existir al menos un caso que requiera dos personas distintas. Justifique
el origen de la regla y pruebe duplicados, roles inválidos y el límite exacto.

```bash
./mvnw -Pev03-e04 test
```

### EV03-E05 — Entrega idempotente (15 puntos)

Implemente `DespachoActivitiesImpl` para que un reintento con la misma intención
devuelva el mismo recibo sin repetir el efecto. En el camino de comando
explícito, reutilizar un `comandoId` con otra orden o unidad debe rechazarse.

Elija:

- `CLAVE_ORDEN_UNIDAD`: la identidad natural es `ordenId + unidad`; o
- `COMANDO_EXPLICITO`: el llamador entrega un `comandoId` estable.

No use un UUID nuevo dentro de cada reintento. La demostración mínima es dos
llamadas equivalentes, un mismo recibo y un único efecto registrado.

```bash
./mvnw -Pev03-e05 test
```

### EV03-E06 — Orquestación durable (15 puntos)

Complete un camino de `DespachoWorkflowImpl`:

- `SECUENCIAL`: entrega una unidad después de otra;
- `PARALELA`: usa `Async` y `Promise` de Temporal; o
- `CHILD_WORKFLOW_POR_UNIDAD`: crea un hijo con Workflow ID estable por destino.

El Workflow debe ser determinista: no haga I/O, no use `Instant.now()`,
`UUID.randomUUID()`, `CompletableFuture`, threads ni clientes Spring desde el
código del Workflow. Los efectos externos pertenecen a Activities.

```bash
./mvnw -Pev03-e06 test
```

### EV03-E07 — Confirmación e incertidumbre (10 puntos)

Complete dos decisiones relacionadas:

1. `TODAS_LAS_UNIDADES` o `QUORUM_CONFIGURABLE` para determinar cuándo la
   entrega está confirmada.
2. `REVISION_MANUAL` o `COMPENSAR_SOLO_CONFIRMADOS` cuando vence la espera.

Las confirmaciones llegan por Signal, el progreso se obtiene por Query y la
cancelación se solicita por Update. Las señales duplicadas no deben aumentar el
conteo. Nunca compense ni reenvíe un resultado incierto como si fuera conocido.

```bash
./mvnw -Pev03-e07 test
```

## Pruebas propias

Además de las pruebas proporcionadas, entregue **al menos tres pruebas propias
activas**, asociadas a **tres decisiones distintas**. Nómbrelas de manera que
expliquen el riesgo, por ejemplo:

- `otra_orden_no_reutiliza_la_reautorizacion()`;
- `un_reintento_no_duplica_el_efecto()`;
- `una_confirmacion_duplicada_no_altera_el_quorum()`.

Cada prueba debe:

- tener una aserción observable, no sólo ejecutar código;
- poder fallar si se revierte la implementación;
- indicar en un comentario qué decisión demuestra;
- ser determinista y no depender de pausas reales (`Thread.sleep`).

Use `src/test/java/.../PruebasPropiasTest.java` o agregue pruebas pequeñas junto
a la capa correspondiente. No desactive pruebas para entregar.

## Comandos de validación

```bash
# Línea base
./mvnw clean verify

# Una misión
./mvnw -Pev03-e04 test

# Todas las pruebas de misiones
./mvnw -Pev03-all test

# Validación final desde cero
./mvnw clean verify -Pev03-all
```

No se considera resuelta una misión si sólo pasa de manera aislada pero rompe
otra. Antes de entregar, `clean verify -Pev03-all` debe finalizar correctamente.

## Uso permitido de IA

Puede usar un LLM como asistente para comparar alternativas, producir un primer
borrador o proponer casos de prueba. La autoría y responsabilidad siguen siendo
suyas. Debe:

- registrar cada interacción relevante en `PROMPTS.md`;
- contrastar la respuesta con el contrato y las pruebas;
- adaptar el código a la decisión elegida;
- poder explicar el código sin ayuda durante una defensa breve.

No está permitido ocultar prompts, presentar código que no comprende, pedir a
otra persona que rinda la evaluación ni incorporar dependencias o contenidos
fuera del alcance para eludir los contratos.

## Archivos que acompañan el trabajo

- `DECISIONES.md`: registro consolidado de las ocho constantes elegidas en siete misiones.
- `PROMPTS.md`: trazabilidad del uso de IA.
- `BITACORA.md`: comandos, resultados e incidentes.
- `../solucion/SOLUCION_GUIADA.md`: pistas de inspiración para usar sólo si se
  bloquea; consultarla debe registrarse en la bitácora.

## Entrega

Entregue el repositorio sin `target/`, archivos de IDE, secretos ni credenciales
reales. Deben estar presentes:

- código fuente y pruebas;
- las ocho constantes de las siete misiones sin `PENDIENTE`;
- siete comentarios `DECISION(EV03-Exx)` completos;
- al menos tres pruebas propias activas;
- `DECISIONES.md`, `PROMPTS.md` y `BITACORA.md` completos;
- evidencia de `./mvnw clean verify -Pev03-all`.

La evaluación valora la coherencia entre decisión, implementación, prueba y
explicación. No existe una única arquitectura correcta entre los caminos
permitidos.
