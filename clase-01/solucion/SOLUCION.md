# Solucionario Clase 1

Este documento contiene las soluciones paso a paso para los ejercicios de la Clase 1.

## C01-E01 — Auditoría del entorno

**Por qué:** Es fundamental asegurar que todos los alumnos tengan las herramientas correctas instaladas antes de comenzar a programar.

**Solución (`env-report.md`):**
```markdown
# Reporte de Entorno

- **Java:** `java --version` -> openjdk 25...
- **Maven Wrapper:** `./mvnw -v` -> Apache Maven 3.9.x...
- **Git:** `git --version` -> git version 2.43.x...
- **Docker:** `docker --version` -> Docker version 27.x...

El proyecto compila correctamente con `./mvnw test`.
```

## C01-E02 — Conversor de unidades

**Por qué:** Práctica básica de sintaxis, métodos estáticos y operaciones matemáticas en Java.

**Solución (`ConversorUnidades.java`):**
```java
package com.sigeo.clase01;

public class ConversorUnidades {

    public static double celsiusAFahrenheit(double celsius) {
        return (celsius * 9 / 5) + 32;
    }

    public static double kilometrosAMillas(double kilometros) {
        return kilometros * 0.621371;
    }
}
```

## C01-E03 — Clasificador de prioridad

**Por qué:** Introducir `switch expressions`, una característica moderna de Java que hace el código más conciso y seguro al obligar a cubrir todos los casos.

**Solución (`ClasificadorPrioridad.java`):**
```java
package com.sigeo.clase01;

public class ClasificadorPrioridad {

    public static String priorityFor(int codigo) {
        return switch (codigo) {
            case 1 -> "Crítica - 1 hora";
            case 2 -> "Alta - 4 horas";
            case 3 -> "Media - 24 horas";
            case 4 -> "Baja - 72 horas";
            case 5 -> "Planificada - 1 semana";
            default -> throw new IllegalArgumentException("Código de prioridad inválido: " + codigo);
        };
    }
}
```

## C01-E04 — Solicitud operativa

**Por qué:** Los `records` son la forma moderna en Java de crear clases portadoras de datos inmutables, reduciendo el código boilerplate (getters, equals, hashCode, toString).

**Solución (`Solicitud.java`):**
```java
package com.sigeo.clase01;

import java.time.LocalDate;

public record Solicitud(String id, String solicitante, String descripcion, int prioridad, LocalDate fecha) {
    
    public Solicitud {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El ID no puede ser nulo ni vacío");
        }
        if (solicitante == null || solicitante.isBlank()) {
            throw new IllegalArgumentException("El solicitante no puede ser nulo ni vacío");
        }
        if (prioridad < 1 || prioridad > 5) {
            throw new IllegalArgumentException("La prioridad debe estar entre 1 y 5");
        }
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha no puede ser nula");
        }
    }
    
    public String resumen() {
        return String.format("[%s] Solicitud de %s (Prioridad: %d) - %s", 
                id, solicitante, prioridad, fecha);
    }
}
```

## C01-E05 — De script a métodos

**Por qué:** Enseñar el principio de responsabilidad única (SRP) y cómo refactorizar código espagueti en métodos pequeños y testeables.

**Solución (`ProcesadorMonolitico.java`):**
```java
package com.sigeo.clase01;

import java.util.ArrayList;
import java.util.List;

public class ProcesadorMonolitico {

    public List<String> procesarDatos(List<String> datos) {
        List<String> resultados = new ArrayList<>();
        for (String dato : datos) {
            if (esValido(dato)) {
                int puntaje = calcularPuntaje(dato);
                resultados.add(formatearResultado(dato, puntaje));
            }
        }
        return resultados;
    }
    
    private boolean esValido(String dato) {
        return dato != null && !dato.trim().isEmpty() && dato.length() >= 3;
    }
    
    private int calcularPuntaje(String dato) {
        int puntaje = 0;
        for (char c : dato.toCharArray()) {
            if (Character.isUpperCase(c)) {
                puntaje += 2;
            } else {
                puntaje += 1;
            }
        }
        return puntaje;
    }
    
    private String formatearResultado(String dato, int puntaje) {
        return "DATO: " + dato.toUpperCase() + " | PUNTAJE: " + puntaje;
    }
}
```

## C01-E06 — Menú SIGEO v0

**Por qué:** Práctica de control de flujo interactivo, entrada por consola y manejo de estado básico en memoria.

**Solución (`MenuSigeo.java`):**
```java
    public void procesarOpcion(String opcion, Scanner scanner) {
        switch (opcion) {
            case "1" -> {
                System.out.print("Ingrese el nuevo registro: ");
                String nuevoRegistro = scanner.nextLine();
                registros.add(nuevoRegistro);
                System.out.println("Registro agregado.");
            }
            case "2" -> {
                if (registros.isEmpty()) {
                    System.out.println("No hay registros.");
                } else {
                    for (int i = 0; i < registros.size(); i++) {
                        System.out.println((i + 1) + ". " + registros.get(i));
                    }
                }
            }
            case "3" -> {
                System.out.print("Ingrese término de búsqueda: ");
                String termino = scanner.nextLine();
                boolean encontrado = false;
                for (String reg : registros) {
                    if (reg.contains(termino)) {
                        System.out.println("- " + reg);
                        encontrado = true;
                    }
                }
                if (!encontrado) {
                    System.out.println("No se encontraron coincidencias.");
                }
            }
            case "4" -> ejecutando = false;
            default -> System.out.println("Opción inválida. Intente nuevamente.");
        }
    }
```

## C01-E07 — Empaquetado reproducible

**Por qué:** Entender cómo Maven empaqueta una aplicación y cómo se ejecuta un JAR fuera del IDE.

**Solución (`pom.xml`):**
El `pom.xml` ya incluye la configuración necesaria en el starter:
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-jar-plugin</artifactId>
    <version>3.4.2</version>
    <configuration>
        <archive>
            <manifest>
                <mainClass>com.sigeo.clase01.MenuSigeo</mainClass>
            </manifest>
        </archive>
    </configuration>
</plugin>
```
Comando para ejecutar:
```bash
./mvnw clean package
java -jar target/clase01-ejercicios-1.0-SNAPSHOT.jar
```

## C01-E08 — Auditor de código generado

**Por qué:** Fomentar el pensamiento crítico al usar herramientas de IA, evaluando la calidad del código generado en lugar de aceptarlo ciegamente.

**Solución (`ia-review.md`):**
*(Ejemplo de respuesta esperada del alumno)*
```markdown
# Revisión de Código IA

**Prompt utilizado:** "Escribe una función en Java que valide si un RUT chileno es válido."

**Solución 1 (ChatGPT):** Usó expresiones regulares complejas y manejo de excepciones para el dígito verificador.
**Solución 2 (Claude):** Usó operaciones matemáticas simples y un arreglo de caracteres.

**Decisión:** Elegí la Solución 2 porque es más legible y no depende de atrapar excepciones para la lógica de negocio (lo cual es un antipatrón).

**Defecto detectado:** Ninguna de las dos validaba correctamente si el RUT venía con puntos o guiones en formatos inconsistentes.
**Prueba añadida:** `assertThat(validarRut("12.345.6789")).isFalse();`
```
