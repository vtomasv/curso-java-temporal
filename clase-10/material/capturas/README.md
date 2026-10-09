# Vistas de resultados · clase 10

Estas imágenes son **vistas renderizadas del portal con datos de ejecución real**, no capturas obtenidas desde un navegador. Se generaron el 2026-10-08 con la referencia docente completa, tres procesos Spring Boot, bancos H2 independientes y el servidor Temporal embebido real del SDK de pruebas.

La verificación envió HTTP real a cada servicio, ejecutó los Workflows, comprobó saldos, movimientos, retries, repetición de solicitudes, reversa y permisos. Después se ejecutó el `app.js` original sobre el HTML del portal en un DOM, cargando las respuestas `/api/state` obtenidas durante esa ejecución. HTML/CSS se renderizaron a imagen; los controles se representaron con su valor visible para impresión y se recortó la región principal del portal. El navegador local y el dev server externo no arrancan en este entorno por restricciones del runtime. No hay captura de la consola Temporal.

| Imagen | Escenario | Resultado comprobado |
|---|---|---|
| initial.png | Inicio limpio | Ana $1.000.000, Bruno $500.000 |
| e01-ok.png | TRANSITORIO, fallos=2 | Crédito falla 1 y 2; intento 3 aplica |
| e02-ok.png | RESPUESTA_PERDIDA, fallos=1 | Un débito y un crédito; Ana $900.000, Bruno $600.000 |
| e03-ok.png | Transferencia NORMAL | Original COMPLETADA; saldos $900.000 / $600.000 |
| e03-reversa.png | Reversa completada | $1.000.000 / $500.000; dos movimientos por banco; original conservado |

Las vistas provienen de la referencia completa: el botón de reversa está disponible. En el código de alumnos se habilita después de E03.

## Obtener print screens de navegador en el equipo docente

1. Generar solución con `solucion/preparar.py` y ejecutar `./mvnw -Plaboratorios clean verify`.
2. Iniciar Temporal CLI externo y el launcher, según README. Entrar como `docente / laboratorio`.
3. Repetir cada escenario de `ejercicios/LABORATORIOS.md`, con datos iniciales y claves nuevas; esperar estados y saldos finales.
4. Capturar Mi cuenta, Intermediario y Bancos con el navegador (1440×1020 recomendado). Incluir clave, estado, saldos y movimientos; no usar datos personales reales.
5. En localhost:8233 capturar la lista y History de `transferencia-tx-e03-ok` y `reversa-tx-e03-ok`. Su ejecución técnica debe ser COMPLETED; los estados de negocio se ven en el portal.

En la PPT están los resultados renderizados identificados y los pasos de captura/History. El modo embebido no ofrece consola web.
