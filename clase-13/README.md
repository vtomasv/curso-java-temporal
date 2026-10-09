# Clase 13 · Compensación por lotes y liquidación interbancaria

Cerrar transferencias en un lote exclusivo y liquidar posiciones netas entre dos bancos.

Duración: **4 horas**. BancoRed es un simulador educativo; no reproduce protocolos ni reglas reales de Redbanc. Dos bancos HTTP/H2 independientes y un portal/Worker. Todos los datos y credenciales son ficticios. Montos enteros CLP.

## Base oficial independiente

Incluye resueltas las funcionalidades de las clases 10–12. No requiere copiar la entrega del alumno. Solo tres TODO nuevos de esta clase. El paquete heredado `com.bancared.clase10` mantiene los contratos de la aplicación; el JAR y Task Queue distinguen la clase. Puertos: 8080 portal, 8081 Cordillera, 8082 Pacífico. Ejecutar una clase a la vez.

## Teoría aplicada

* Compensación por neteo, liquidación y reversa.
* Snapshot, reserva exclusiva y transacciones SQL locales.
* Saga con comandos idempotentes y recuperación de recibos.
* Incertidumbre, compensación conocida y conservación de liquidez.

## Agenda · 240 minutos

| Minutos | Trabajo | Evidencia |
|---|---|---|
| 0–15 | Base y pantallas | Tests base verdes |
| 15–40 | Teoría y demo | Flujo visible |
| 40–80 | E01 con IA | Primer incremento comprobado |
| 80–95 | Pausa | Diff explicado |
| 95–145 | Teoría y E02 | Segundo incremento |
| 145–195 | Teoría y E03 | Tercer incremento |
| 195–220 | Variante propia | TODO y prueba antes de editar |
| 220–240 | Verificación, defensa y continuidad | Bitácora y base docente |

## Terminales y reinicio del JAR

Preparar JDK 25, Python 3 y el CLI de IA autenticado antes de la clase. Desde la raíz del repositorio, en terminal A:

```bash
cd clase-13/ejercicios
./mvnw clean verify
```

En terminal B iniciar Temporal **antes** del launcher y mantenerlo abierto:

```bash
temporal server start-dev --ip 127.0.0.1 --ui-port 8233
```

En terminal A:

```bash
python3 scripts/laboratorio.py --reset
```

En terminal C, también dentro de `clase-13/ejercicios`, ejecutar tests y `codex`. Portal http://localhost:8080, docente / laboratorio. Consola http://localhost:8233. Windows: `mvnw.cmd` y `python` según instalación.

Después de cada cambio Java: salir del CLI, pulsar Ctrl+C **en A**, ejecutar perfil del lab, revisar diff y construir. Esperar BUILD SUCCESS y relanzar el launcher en A. `--reset` restablece únicamente los datos ficticios de H2; **no compila y no borra History ni Schedules de Temporal externo**. Cada operación manual usa una clave nueva. Los fallos bancarios se configuran después del reinicio. No resetear con Workflows ejecutándose.

Alternativa sin consola:

```bash
python3 scripts/laboratorio.py --temporal embedded --reset
```

Mantener `--temporal embedded` al relanzar si se eligió ese modo. Usa el servidor real de pruebas del SDK; no ofrece consola ni Schedule externo. El Schedule de cumpleaños usa la fecha real de Chile. La simulación de fechas se ejecuta mediante los botones de noche/ciclo. La marca del saludo es una fecha de negocio simulada, no un reloj de producción.

## Material

* [Laboratorios](ejercicios/LABORATORIOS.md): comandos completos, controles y resultados.
* [Prompts](ejercicios/PROMPTS.md): plan, implementación acotada y diagnóstico.
* [Bitácora](ejercicios/BITACORA.md): evidencias y explicación del alumno.
* [Solución docente](solucion/SOLUCION.md): generador por incremento y solución completa.

La base compila antes de empezar. Los perfiles del incremento son deliberadamente rojos hasta completar su TODO. E02 requiere E01; E03 requiere E01 y E02.
