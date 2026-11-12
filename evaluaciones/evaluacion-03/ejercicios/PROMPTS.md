# Registro de uso de IA — Evaluación 3 SICOR

**Estudiante:** <!-- nombre y apellido --><br>
**Herramienta(s):** <!-- nombre y versión si se conoce -->

El uso de IA está permitido como apoyo, pero cada respuesta debe revisarse y
adaptarse. Registre las interacciones que hayan influido en código, pruebas o
decisiones. No incluya contraseñas, tokens, datos personales ni información
real de organizaciones.

## Cómo registrar una interacción

Copie esta sección por cada interacción relevante:

### Interacción N — EV03-E0X

**Fecha y hora:**<br>
**Objetivo:**<br>
**Prompt enviado:**

> <!-- Pegue el prompt o un resumen fiel si fue una conversación extensa. -->

**Respuesta útil recibida:**

> <!-- Resuma la propuesta; no es necesario copiar una respuesta completa. -->

**Verificación realizada:**

- <!-- contrato o prueba que consultó -->
- <!-- comando que ejecutó -->
- <!-- caso límite que comprobó -->

**Qué acepté y adapté:**<br>
**Qué rechacé y por qué:**<br>
**Decisión final propia:**

---

## Ejemplo de nivel de detalle

### Interacción de ejemplo — EV03-E05

**Objetivo:** comparar claves idempotentes para la entrega.<br>
**Prompt enviado:** “Compara una clave natural orden+unidad con un comando
explícito estable para una Activity de Temporal. Incluye riesgos de colisión y
reintentos; no escribas la solución completa.”<br>
**Respuesta útil recibida:** la clave natural es simple si sólo existe una
entrega por unidad; el comando explícito permite más de una operación lógica,
pero obliga al llamador a conservar su identidad.<br>
**Verificación realizada:** leí el contrato de `DespachoActivities`, ejecuté dos
veces el mismo comando y verifiqué recibo igual y un efecto.<br>
**Qué acepté y adapté:** usé una clave natural con validación de los datos.<br>
**Qué rechacé y por qué:** descarté generar un UUID dentro de la Activity porque
cambiaría la identidad entre reintentos.<br>
**Decisión final propia:** `CLAVE_ORDEN_UNIDAD`, documentada en E05.

> El ejemplo muestra el formato; no prueba su propia decisión y no debe copiarse
> como evidencia.

## Resumen de trazabilidad

| Interacción | Misión | Aporte de la IA | Verificación humana | Archivo afectado |
|---|---|---|---|---|
| <!-- N --> | <!-- E0X --> | <!-- breve --> | <!-- breve --> | <!-- ruta --> |

## Lista de control

- [ ] Registré todas las respuestas de IA que influyeron materialmente.
- [ ] No compartí secretos ni información real.
- [ ] Revisé cada propuesta contra el contrato y las pruebas.
- [ ] Puedo explicar las líneas incorporadas.
- [ ] Mis comentarios `DECISION` expresan mi criterio, no una justificación
      genérica generada por la herramienta.
- [ ] Anoté en `BITACORA.md` si consulté la solución guiada.

Si no usó IA, indíquelo aquí y explique brevemente cómo abordó la evaluación:

<!-- No utilicé IA. Mi estrategia fue... -->
