# Desarrollo de aplicaciones con Java y Temporal

Programa vigente: **15 clases**. Las clases 1–9 mantienen su código. Desde la clase 10 se construye **BancoRed**, un simulador educativo de dos bancos y un intermediario. No reproduce protocolos ni reglas reales de Redbanc.

La metodología asistida combina teoría breve, pantallas preparadas, tres TODO por clase, CLI de IA y evidencia de pruebas. **Cada clase incluye resuelto todo lo anterior**: no depende de la entrega del alumno. Al terminar la clase 15 funciona el proyecto completo.

## Clases vigentes

| Clase | Funcionalidades que se agregan |
|---|---|
| 01–09 | Java, web, persistencia, seguridad y fundamentos Temporal originales |
| [10](clase-10/README.md) | Transferencias, retry, idempotencia y reversa |
| [11](clase-11/README.md) | Cumpleaños y procesos nocturnos |
| [12](clase-12/README.md) | Aprobación, vencimiento y transferencias hijas |
| [13](clase-13/README.md) | Compensación por lotes y liquidación interbancaria |
| [14](clase-14/README.md) | Notificaciones, outbox, inbox y mensajes fallidos |
| [15](clase-15/README.md) | Asistente bancario, replay y cierre del proyecto |

Las carpetas 16–19 conservan material histórico. No son clases pendientes del programa vigente. La correspondencia con el programa anterior está en [PLANIFICACION.md](docs/PLANIFICACION.md).

## Probar una clase sin resolver los TODO

Desde la raíz del repositorio, generar una copia docente nueva, sin sobrescribir ejercicios ni trabajos:

```bash
python3 clase-11/solucion/preparar.py ../bancared-c11-prueba
cd ../bancared-c11-prueba
./mvnw -Plaboratorios clean verify
```

En otra terminal iniciar Temporal y mantenerlo abierto:

```bash
temporal server start-dev --ip 127.0.0.1 --ui-port 8233
```

En la terminal del proyecto generado:

```bash
python3 scripts/laboratorio.py --reset
```

Portal: http://localhost:8080, docente / laboratorio. Consola: http://localhost:8233. Para probar otra clase cambiar `11` por `12`, `13`, `14` o `15` y usar un destino nuevo. Una sola clase a la vez usa los puertos 8080, 8081 y 8082.

Alternativa sin consola ni Schedule externo:

```bash
python3 scripts/laboratorio.py --temporal embedded --reset
```

El Schedule diario de cumpleaños requiere el servidor externo. La simulación de una/dos noches funciona también en modo embebido.

## Trabajar como alumno

Dentro de `clase-NN/ejercicios`, `./mvnw clean verify` comprueba la base preparada. Los perfiles `-Plab-e01`, `-Plab-e02` y `-Plab-e03` son deliberadamente rojos hasta completar su TODO. `-Plaboratorios` comprueba los tres incrementos juntos. Leer LABORATORIOS.md y PROMPTS.md antes de editar.

Después de cambiar Java: salir del CLI, Ctrl+C al launcher, test del lab, `git diff`, build del JAR y relanzar. **`--reset` no compila ni elimina History o Schedules de Temporal externo.** Usar claves nuevas y no resetear con Workflows en ejecución. Se conserva el paquete `com.bancared.clase10` para mantener contratos.

## Entorno de BancoRed (clases 10–15)

| Componente | Implementación |
|---|---|
| Java / build | JDK 25, Maven Wrapper |
| Web | Spring Boot 4.1.0, Thymeleaf, Spring Security y CSRF |
| Bancos | Procesos HTTP independientes, H2 persistente por banco |
| Intermediario | Portal + Worker, H2 propia, Temporal Java SDK 1.37.0 |
| Pruebas | JUnit y TestWorkflowEnvironment, time skipping y replay real |
| Eventos (14–15) | SQL_LOCAL reproducible y adaptador RabbitMQ AMQP opcional |
| Asistente (15) | Recuperación léxica, MODELO_SIMULADO y proveedor HTTP opcional |

RabbitMQ y el proveedor remoto se configuran en la guía de cada clase. Los labs locales no necesitan credenciales de IA ni llamadas facturables. Las clases anteriores mantienen sus dependencias y sus guías propias.

## Documentación

* [Planificación vigente y correspondencia con 19 clases](docs/PLANIFICACION.md).
* [Arquitectura, invariantes y límites de BancoRed](docs/BANCORED.md).
* [Guía de prueba docente de las cinco clases](docs/PRUEBA_DOCENTE_11_15.md).
* [Uso de IA](docs/GUIA_IA.md), [setup original](docs/SETUP.md) y [rúbricas transversales](docs/RUBRICAS.md).
* Cada clase tiene laboratorios, prompts, bitácora, capturas y solución docente por incremento.

La entrega exige pruebas verdes, diff explicado y evidencia propia. El alumno debe defender el comportamiento sin depender de una respuesta de IA.
