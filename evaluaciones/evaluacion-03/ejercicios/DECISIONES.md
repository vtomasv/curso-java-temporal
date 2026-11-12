# Registro de decisiones — Evaluación 3 SICOR

**Estudiante:** <!-- nombre y apellido --><br>
**Sección:** <!-- sección --><br>
**Repositorio / commit:** <!-- URL o hash -->

Este documento resume las decisiones implementadas. Debe coincidir con las
constantes y comentarios `DECISION(EV03-Exx)` de `Decisiones.java`. Una opción
no se considera justificada sólo por completar este archivo: la implementación
y las pruebas deben demostrar la misma decisión.

## Criterio de una decisión completa

Para cada misión documente:

1. la alternativa elegida;
2. la alternativa real que descartó;
3. el tradeoff aceptado (beneficio y costo);
4. el efecto observable en la aplicación;
5. la prueba que respalda la elección;
6. cualquier supuesto relevante.

---

## EV03-E01 — Modelo e invariantes

**Elegí:** `INVARIANTES_EN_ENTIDAD` / `POLITICA_DE_DOMINIO`<br>
**Alternativa descartada:**<br>
**Tradeoff aceptado:**<br>
**Única fuente de verdad:**<br>
**Cómo evito crear órdenes inválidas desde otro adaptador:**<br>
**Prueba(s):**<br>
**Supuesto(s):**

<!-- Reemplace este comentario por una justificación de 4 a 8 líneas. -->

## EV03-E02 — Acceso y confidencialidad

**Elegí:** `RBAC_CENTRAL` / `ESTRATEGIA_POR_CLASIFICACION`<br>
**Alternativa descartada:**<br>
**Tradeoff aceptado:**<br>
**Dónde se aplica la matriz de acceso:**<br>
**Cómo evito filtrar el contenido MAXIMA en listados/logs/errores:**<br>
**Prueba(s):**<br>
**Supuesto(s):**

<!-- Explique qué ocurre con un usuario autenticado sin rol suficiente. -->

## EV03-E03 — Reautenticación

**Elegí:** `POR_CADA_LECTURA` / `VENTANA_CORTA_POR_ORDEN`<br>
**Alternativa descartada:**<br>
**Tradeoff aceptado:**<br>
**Alcance de la autorización (usuario y orden):**<br>
**Inicio, duración y expiración:**<br>
**Cómo impido reutilización por otro usuario u otra orden:**<br>
**Prueba(s):**<br>
**Supuesto(s):**

<!-- No copie contraseñas, hashes ni secretos en este documento. -->

## EV03-E04 — Firmas múltiples

**Elegí:** `ROLES_FIJOS` / `UMBRAL_CONFIGURABLE`<br>
**Alternativa descartada:**<br>
**Tradeoff aceptado:**<br>
**Regla para INFORMATIVA/INSTRUCCION/ORDEN:**<br>
**Regla adicional para MAXIMA:**<br>
**Cómo rechazo firmante duplicado y rol no autorizado:**<br>
**Prueba(s):**<br>
**Supuesto(s):**

<!-- Identifique al menos un escenario que requiera dos personas distintas. -->

## EV03-E05 — Idempotencia de entrega

**Elegí:** `CLAVE_ORDEN_UNIDAD` / `COMANDO_EXPLICITO`<br>
**Alternativa descartada:**<br>
**Tradeoff aceptado:**<br>
**Clave estable:**<br>
**Qué devuelvo ante un reintento equivalente:**<br>
**Compatibilidad:** en `COMANDO_EXPLICITO`, qué ocurre si el mismo comando trae otra orden/unidad; en `CLAVE_ORDEN_UNIDAD`, por qué la pareja representa toda la intención:<br>
**Prueba(s):**<br>
**Supuesto(s):**

<!-- Explique qué representa exactamente “un efecto” en su implementación. -->

## EV03-E06 — Orquestación durable

**Elegí:** `SECUENCIAL` / `PARALELA` / `CHILD_WORKFLOW_POR_UNIDAD`<br>
**Alternativa(s) descartada(s):**<br>
**Tradeoff aceptado:**<br>
**Identidad estable de cada entrega:**<br>
**Cómo preservo determinismo y replay:**<br>
**Dónde ocurren los efectos externos:**<br>
**Prueba(s):**<br>
**Supuesto(s):**

<!-- Compare costo, aislamiento y tiempo total; no justifique sólo por sintaxis. -->

## EV03-E07 — Confirmación e incertidumbre

**Confirmación elegida:** `TODAS_LAS_UNIDADES` / `QUORUM_CONFIGURABLE`<br>
**Tratamiento elegido:** `REVISION_MANUAL` / `COMPENSAR_SOLO_CONFIRMADOS`<br>
**Alternativas descartadas:**<br>
**Tradeoff aceptado:**<br>
**Cálculo exacto del quórum o condición de totalidad:**<br>
**Cómo trato Signals duplicados o desconocidos:**<br>
**Qué evidencia considero “resultado conocido”:**<br>
**Prueba(s):**<br>
**Supuesto(s):**

<!-- Nunca describa como confirmado un resultado incierto. -->

---

## Pruebas propias entregadas

Incluya al menos tres decisiones diferentes.

| Prueba | Decisión | Riesgo que demuestra | Resultado |
|---|---|---|---|
| <!-- nombre() --> | <!-- EV03-E0X --> | <!-- riesgo --> | <!-- PASS --> |
| <!-- nombre() --> | <!-- EV03-E0X --> | <!-- riesgo --> | <!-- PASS --> |
| <!-- nombre() --> | <!-- EV03-E0X --> | <!-- riesgo --> | <!-- PASS --> |

## Verificación de coherencia

- [ ] Las ocho constantes de `Decisiones.java` ya no tienen valor `PENDIENTE`.
- [ ] Cada misión posee un comentario `DECISION(EV03-Exx)` completo.
- [ ] Este documento coincide con el código realmente ejecutado.
- [ ] Las alternativas descartadas son caminos válidos de la evaluación.
- [ ] Cada prueba citada existe, está activa y contiene aserciones.
- [ ] `./mvnw clean verify -Pev03-all` termina con `BUILD SUCCESS`.

## Declaración

Declaro que puedo explicar las decisiones, el código y las pruebas entregadas;
que documenté el uso de IA en `PROMPTS.md`; y que no incorporé información,
dependencias ni técnicas fuera del alcance de las clases 1 a 13.

**Fecha:**<br>
**Firma o nombre del estudiante:**
