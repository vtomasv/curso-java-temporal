# Solución guiada e inspiración — Evaluación 3 SICOR

## Propósito y reglas de uso

Este archivo ofrece pistas cuando una misión queda bloqueada. No define una
única solución oficial ni reemplaza la justificación del estudiante. Los caminos
permitidos pueden obtener el mismo puntaje si cumplen el contrato y están
respaldados por pruebas.

Si consulta una sección:

1. registre el bloqueo y la sección en `../ejercicios/BITACORA.md`;
2. elija conscientemente una alternativa;
3. adapte la idea al código existente;
4. escriba una prueba que pueda refutar su implementación;
5. explique el tradeoff con sus propias palabras.

Los fragmentos son ilustrativos. Pueden necesitar cambios de nombres o tipos
para compilar con la versión final de la plantilla.

## Mapa de responsabilidades

| Problema | Lugar natural | No debería vivir en |
|---|---|---|
| Invariantes de una orden | Dominio o política de dominio | Controlador web |
| Autorización | `PoliticaAcceso` / Spring Security | Plantilla HTML solamente |
| Reautorización temporal | `ReautenticacionService` y sesión | Parámetros de URL |
| Firmas y quórum | `PoliticaFirmas` + servicio transaccional | JavaScript del navegador |
| Efecto de entrega | Activity idempotente | Código de Workflow |
| Orden de pasos y espera | Workflow determinista | Activity monolítica |
| Incertidumbre | Estado explícito y evidencia | Reintento infinito |

---

## EV03-E01 — Modelo e invariantes

### Pregunta de diseño

¿La regla forma parte de lo que una `Orden` *es*, o es una política que podría
cambiar por institución/configuración?

- **Entidad:** ofrece consistencia fuerte y evita instancias inválidas aunque se
  construyan fuera de Spring. Su costo es que políticas variables obligan a
  cambiar la clase.
- **Política de dominio:** aísla reglas variables y facilita tablas de decisión.
  Su costo es asegurar que todos los puntos de creación la invoquen.

### Pista mínima

Centralice límites en constantes con nombres y aplique la validación antes de
asignar campos. Para una colección, distinga entre la entrada original y el
`Set` final si el contrato exige detectar duplicados: construir un `Set` elimina
duplicados silenciosamente.

### Estructura posible

```java
private static String contenidoValido(String valor) {
    String normalizado = texto(valor, "contenido");
    if (normalizado.length() < MIN || normalizado.length() > MAX) {
        throw new IllegalArgumentException("contenido fuera de rango");
    }
    return normalizado;
}
```

Si elige política, una forma razonable es que `Orden` delegue la validación
variable a un componente de dominio pequeño durante su construcción. Validarla
sólo en el servicio deja abierta la creación directa que usan las pruebas. No
es necesario convertir todo en una jerarquía.

### Pruebas que aportan evidencia

- límite inferior menos uno, límite exacto y límite superior más uno;
- cero destinatarios;
- transición desde un estado incorrecto;
- una construcción desde fuera del controlador.

### Error frecuente

Validar sólo con anotaciones del DTO. Eso protege el formulario, pero no una
creación desde pruebas, otro servicio o un proceso de importación.

---

## EV03-E02 — Acceso y confidencialidad

### Pregunta de diseño

¿La matriz es pequeña y estable o espera políticas distintas por nivel?

- **RBAC central:** hace visible toda la matriz en un solo método; es simple de
  auditar, aunque puede crecer como una condición extensa.
- **Estrategia por clasificación:** facilita agregar comportamientos diferentes;
  tiene más piezas y requiere una selección segura por defecto.

### Pista mínima

Empiece negando el acceso si no hay autenticación. Después determine los roles
desde `GrantedAuthority`; no confíe en un parámetro enviado por el navegador.
Use `ROLE_...` de manera consistente.

### Tabla de trabajo sugerida

Construya su tabla a partir del contrato de pruebas antes de escribir `if`:

| Confidencialidad | OPERADOR | OFICIAL | COMANDANTE | AUDITOR |
|---|---:|---:|---:|---:|
| INTERNA | según contrato | según contrato | según contrato | según contrato |
| RESERVADA | según contrato | según contrato | según contrato | según contrato |
| MAXIMA | según contrato | según contrato | según contrato | según contrato |

Una implementación central puede usar un `switch` de confidencialidad. Una
estrategia puede mapear cada enum a un `Predicate<Authentication>` o a una clase
pequeña. En ambos casos, una clasificación desconocida debe denegar.

### Defensa en profundidad

Aunque la vista oculte un botón, `OrdenService.detalle()` debe volver a aplicar
la autorización. `OrdenResumen` no debería tener un campo de contenido. No
incluya el cuerpo en el mensaje de una excepción.

### Pruebas que aportan evidencia

- usuario sin autenticar: denegado;
- cada frontera rol–clasificación de la matriz;
- el resumen serializado no contiene una frase única del cuerpo;
- una llamada directa al servicio no elude la regla de la interfaz.

---

## EV03-E03 — Reautenticación de máxima confidencialidad

### Pregunta de diseño

¿Se prioriza máxima fricción de seguridad o una experiencia breve de consulta?

- **Por cada lectura:** reduce reutilización; obliga a escribir credenciales en
  cada acceso.
- **Ventana corta por orden:** mejora la navegación; exige modelar expiración y
  aislamiento correctamente.

### Invariante de seguridad

La autorización no es “esta sesión ya se autenticó”; es “este usuario se
reautenticó para esta orden hasta cierto instante o para un único consumo”. Una
clave conceptual segura es:

```text
(session-id implícito, username, ordenId) -> vencimiento/consumo
```

No guarde la contraseña. Verifíquela con el `PasswordEncoder` y descarte el valor
recibido.

### Pista para cada lectura

Al autenticar, registre una marca. Al consumir, elimínela antes de revelar el
contenido. Una segunda llamada debe fallar hasta una nueva autenticación.

### Pista para ventana corta

Guarde un instante límite para el par usuario–orden. Al consumir:

```java
if (hasta == null || reloj.actual().isAfter(hasta)) {
    permisos.remove(clave);
    return false;
}
return true;
```

Para una prueba rápida y determinista, es mejor inyectar un `Clock` en el
servicio que usar `Thread.sleep`. `Clock` es contenido de Java estándar y no
debe introducirse dentro del Workflow.

### Pruebas que aportan evidencia

- contraseña incorrecta;
- nombre diferente del usuario autenticado;
- autorización para orden A no permite orden B;
- otra cuenta no reutiliza la marca;
- consumo único o expiración, según el camino elegido.

### Error frecuente

Conceder el acceso máximo sólo porque la sesión normal está autenticada. El
requisito pide una verificación adicional inmediatamente antes de revelar.

---

## EV03-E04 — Firmas múltiples

### Pregunta de diseño

¿La aprobación expresa una composición de responsabilidades o un número de
personas autorizadas?

- **Roles fijos:** representa reglas como “un OFICIAL y un COMANDANTE”. Es muy
  explícito, pero menos flexible.
- **Umbral configurable:** representa “N firmas de roles habilitados”. Cambia con
  facilidad, pero no garantiza una combinación particular salvo que se agregue
  esa condición.

### Separar tres controles

1. el rol puede firmar esta orden;
2. esta persona todavía no firmó esta orden;
3. las firmas registradas satisfacen la política.

La restricción única `(orden_id, firmante)` protege el segundo control ante
concurrencia, además de la comprobación amigable del servicio.

### Estructura para umbral

Una tabla pequeña y explícita puede expresarse con un record:

```java
record ReglaFirma(int minimo, Set<String> roles) {}
```

El `minimo` puede derivarse de `TipoOrden` y `Confidencialidad`. Al menos una
combinación debe exigir dos personas distintas. Evite valores mágicos repetidos
en servicio, controlador y vista.

### Estructura para roles fijos

La cantidad por sí sola no prueba la combinación. Para una regla de roles,
compare el conjunto de roles presentes con el conjunto obligatorio. Si permite
dos firmas del mismo rol, explíquelo de forma explícita.

### Pruebas que aportan evidencia

- el mismo usuario dos veces se rechaza;
- un rol no habilitado no crea registro;
- antes del límite la orden sigue `PENDIENTE_FIRMAS`;
- justo en el límite cambia una sola vez a `LISTA_PARA_ENVIO`;
- un intento posterior no retrocede ni duplica el estado.

---

## EV03-E05 — Entrega idempotente

### Pregunta de diseño

¿Existe exactamente una intención de entrega por orden y unidad, o podrían
existir varias operaciones con la misma pareja?

- **Clave orden–unidad:** simple y natural cuando sólo hay un despacho lógico.
- **Comando explícito:** distingue intenciones sucesivas, pero obliga a crear y
  conservar el ID antes del primer intento.

### Patrón mínimo

El resultado se asocia a la clave idempotente en almacenamiento duradero. En la
simulación se usa un mapa, pero la regla es la misma:

```java
return recibos.computeIfAbsent(claveEstable, ignorada -> ejecutarEfectoUnaVez());
```

No basta con devolver el mismo recibo si el efecto ya se repitió. Incremente el
contador o invoque el sistema externo sólo dentro de la creación atómica.

### Validar compatibilidad

En `COMANDO_EXPLICITO`, si un `comandoId` ya existe, compruebe que representa la
misma orden y unidad: reutilizarlo con otros datos es un conflicto. En
`CLAVE_ORDEN_UNIDAD`, esa pareja ya es la intención completa y no existe un
payload adicional que comparar.

### Pruebas que aportan evidencia

- dos llamadas equivalentes: mismo recibo y `efectosRegistrados() == 1`;
- llamadas concurrentes equivalentes: un efecto;
- comando explícito repetido con unidad distinta: rechazo;
- dos claves realmente distintas: dos efectos.

### Error frecuente

Crear `UUID.randomUUID()` para el comando dentro del Workflow o Activity. Cada
intento se vuelve una operación nueva y la idempotencia deja de existir.

---

## EV03-E06 — Orquestación durable

### Pregunta de diseño

Compare latencia, aislamiento y complejidad:

- **Secuencial:** comportamiento fácil de seguir; una unidad lenta retrasa las
  siguientes.
- **Paralela:** reduce el tiempo total; requiere reunir y ordenar resultados de
  forma determinista.
- **Child Workflow:** aísla el historial y ciclo de vida por unidad; agrega
  coordinación e identidades hijas.

### Secuencial

Conserve el orden estable de `input.unidades()` y agregue el recibo de cada
Activity. La clave se deriva sólo de datos del input.

### Paralela

Use las primitivas de Temporal:

```java
List<Promise<ReciboEnvio>> promesas = unidades.stream()
        .map(u -> Async.function(() -> activity.enviar(ordenId, u, comando(ordenId, u))))
        .toList();
List<ReciboEnvio> resultados = promesas.stream().map(Promise::get).toList();
```

No use `CompletableFuture`, un pool de threads o un `parallelStream` dentro del
Workflow.

### Child Workflow por unidad

El ID debe ser estable para replay y reintentos:

```text
entrega-{ordenId}-{unidad}
```

Cree un stub por unidad y ejecute el método del hijo. Si los hijos corren en
paralelo, use las primitivas de Temporal también para invocarlos.

### Reglas de determinismo

Dentro del Workflow:

- sí: input, estado local, `Workflow.await`, `Workflow.currentTimeMillis`,
  `Async`, `Promise`, stubs de Activity/Child;
- no: base de datos, HTTP, sistema de archivos, `Instant.now`, UUID aleatorio de
  Java, threads, bloqueos o beans Spring.

### Pruebas que aportan evidencia

Use `TestWorkflowEnvironment`, registre Workflow, Activities y Child Workflow
si corresponde. Verifique cantidad de recibos, identidad por unidad y resultado
ante reintento. No use pausas reales para esperar 12 horas: el entorno de prueba
puede adelantar el tiempo durable.

---

## EV03-E07 — Confirmación e incertidumbre

### Confirmación

- **Todas:** `confirmadas.size() == total`, después de validar que la señal
  corresponde a un recibo emitido.
- **Quórum:** defina una fórmula reproducible. Por ejemplo, mayoría estricta:
  `floor(total / 2) + 1`. `DespachoInput` ya rechaza cero destinos; documente
  el mínimo para toda entrada válida.

El uso de un `Set` evita que dos Signals válidos de la misma unidad aumenten el
conteo. El `reciboId` evita confirmar una unidad con evidencia inventada.

### Incertidumbre

- **Revisión manual:** registra `PENDIENTE_REVISION` y conserva la evidencia; no
  afirma éxito ni reenvía automáticamente.
- **Compensar sólo confirmados:** recorre únicamente recibos cuya unidad aparece
  en `confirmadas`. Cada compensación debe ser idempotente.

Una unidad sin confirmación puede estar entregada o no. Por eso no debe
compensarse como si el resultado fuera conocido.

### Estructura posible de compensación

```java
recibos.stream()
    .filter(r -> confirmadas.contains(r.unidad()))
    .forEach(r -> activities.compensar(r.ordenId(), r.unidad(), r.reciboId()));
```

El método común de término registra `PENDIENTE_REVISION` una sola vez. El estado
final sigue explicitando la incertidumbre; compensar lo conocido no demuestra
el resultado de las demás unidades.

### Pruebas que aportan evidencia

- Signal con recibo desconocido no cuenta;
- Signal duplicado cuenta una vez;
- totalidad o quórum exacto termina en `ENTREGADA`;
- una menos que el requisito no termina prematuramente;
- al vencer, queda `PENDIENTE_REVISION`;
- sólo recibos confirmados se compensan;
- Update de cancelación con motivo vacío se rechaza y con motivo válido cambia
  de forma controlada.

---

## Cómo crear tres buenas pruebas propias

No repita textualmente una prueba proporcionada. Elija un riesgo que surja de
su arquitectura:

1. **Seguridad:** una reautorización de la orden A no abre la B.
2. **Idempotencia:** veinte llamadas concurrentes conservan un solo efecto.
3. **Workflow:** dos Signals iguales no adelantan el quórum.

La estructura Arrange–Act–Assert ayuda a que el objetivo sea visible:

```java
@Test
void otra_orden_no_reutiliza_la_reautorizacion() {
    // Arrange: autenticar para A.
    // Act: intentar consumir para B.
    // Assert: el acceso es falso.
}
```

Una prueba es valiosa si falla al introducir el defecto que declara detectar.
Compruébelo temporalmente, revierta el defecto y registre la evidencia.

## Comprobación final

```bash
./mvnw clean verify -Pev03-all
```

Antes de entregar, revise:

- las decisiones de código y `DECISIONES.md` coinciden;
- ninguna misión sigue en `PENDIENTE`;
- no hay pruebas desactivadas;
- no se filtró el contenido máximo en DTOs, logs o errores;
- no hay I/O ni APIs no deterministas dentro del Workflow;
- la idempotencia controla el efecto, no sólo el valor de retorno;
- la incertidumbre se conserva como estado explícito.

La mejor solución no es la de mayor cantidad de clases: es la que mantiene un
contrato claro, explicita sus costos y demuestra su comportamiento con pruebas.
