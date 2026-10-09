# Verificación de la entrega · 2026-10-08

JDK Temurin 25, Spring Boot 4.1.0, Temporal Java SDK 1.37.0, H2. Sin cambios en clases 1–9.

| Verificación | Resultado |
|---|---|
| Base alumno: `./mvnw clean verify` | 11 pruebas, 0 fallos/errores |
| Referencia generada: `./mvnw -Plaboratorios clean verify` | 23 pruebas, 0 fallos/errores |
| Labs pendientes en base alumno: `./mvnw -Plaboratorios test` | Fallan únicamente tests de incrementos; los 11 de seguridad permanecen verdes |
| E01 por HTTP entre tres procesos | Dos 503 y éxito en intento 3; saldos y ledger correctos |
| E02 por HTTP | Respuesta perdida, dos intentos y un crédito; solicitud repetida sin duplicar |
| E03 por HTTP | Reversa restaura saldos; original preservado; repetición sin movimientos extra |
| Autorización y CSRF por HTTP | Cliente no ve otra cuenta; origen ajeno y fallos docentes rechazados 403; POST sin CSRF rechazado |
| PPT | 29 diapositivas; tablas y texto editables; tamaño y fuentes de la referencia; revisión visual |

Los tests usan TestWorkflowEnvironment real del SDK. La verificación HTTP usa el modo embebido; el modo externo está configurado para la clase, pero el dev server y la consola no pudieron ejecutarse en este entorno. Imágenes de resultados renderizadas con datos reales: ver `capturas/README.md`.

La lectura del portal combina consultas sucesivas a ambos bancos y al intermediario: durante una transición puede mostrar un estado nuevo junto con un saldo que se refrescará en el próximo sondeo. Esperar estado **y** saldos esperados antes de capturar; no es una instantánea global transaccional.
