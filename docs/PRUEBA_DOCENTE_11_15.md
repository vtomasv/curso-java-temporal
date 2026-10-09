# Probar las clases 11–15

## Referencia completa

Para cada N entre 11 y 15, desde la raíz ejecutar `python3 clase-N/solucion/preparar.py ../bancared-cN-prueba`, entrar al destino nuevo y ejecutar `./mvnw -Plaboratorios clean verify`. Mantener el servidor Temporal externo abierto y arrancar `python3 scripts/laboratorio.py --reset`. La guía LABORATORIOS.md contiene todas las acciones de pantalla, claves de ejemplo y resultados. Cambiar claves en cada repetición.

Si se desea revisar solo un incremento: añadir `--hasta e01` o `--hasta e02` al generador. El destino debe ser nuevo. Nunca sobrescribe ejercicios ni trabajo del alumno.

## Evidencia de comprobación local

| Clase | Tests base | Tests referencia completa |
|---|---:|---:|
| 11 | 25 | 30 |
| 12 | 30 | 36 |
| 13 | 37 | 45 |
| 14 | 46 | 50 |
| 15 | 50 | 54 |

Cada uno de los 15 TODO tiene perfil rojo comprobado antes de resolverlo. Los tests completos incluyen contratos, workflows, rechazo conocido, incertidumbre, deduplicación y replay de una historia real en clase 15, y 31 noches con timer y Continue-As-New real en clase 11. Los conteos son por módulo; varias pruebas anteriores se repiten para comprobar la base heredada.

Las vistas esperadas se generan con HTML/JS del portal y datos de ejecuciones HTTP reales con dos bancos y Worker separados. No son screenshots del navegador ni de la consola. Reproducir con la infraestructura externa del equipo docente para capturas propias.

## Infraestructura externa

1. Clase 11: crear Schedule, verificar 00:05 America/Santiago y overlap SKIP, disparar/pausar/reanudar. El disparo usa fecha real de Chile. Al terminar, pausar el Schedule antes de cambiar de clase.
2. Clase 14: modo local para los tres labs. Opcional RabbitMQ: `docker compose up -d` desde ejercicios, esperar healthy y `python3 scripts/laboratorio.py --broker rabbit --reset`. Confirmar publicación, una notificación ante redelivery y versión 99 en DLQ.
3. Clase 15: modo mock para reproducir salida válida/fuente inventada/proveedor caído. Opcional proveedor remoto con URL completa, modelo y clave externalizados; relanzar con `--ai remote`.

Schedule externo, broker RabbitMQ y proveedor remoto requieren validación manual en el equipo con esas infraestructuras. Los tests y la evidencia automatizada usan Temporal embebido, transporte SQL_LOCAL y MODELO_SIMULADO.

## Checklist de prueba

Antes de cada escenario: detener launcher, comprobar JAR actualizado, reset sin Workflows activos, claves nuevas y bancos Disponible. No confundir neto bancario con monto bruto transferido, ni estado de negocio con estado técnico COMPLETED de Temporal. No esperar todos los escenarios superpuestos en una misma pantalla: las capturas corresponden a pruebas independientes.

Al finalizar clase 15: demo de ocho minutos, suite completa, bitácora, captura de History/replay y explicación individual de un fallo y de la variante propia.
