# Solucionario Clase 02

Este documento contiene las soluciones paso a paso para los ejercicios de la Clase 02.

## C02-E01 — Invariante de Recurso

**Por qué:** Las invariantes aseguran que un objeto siempre esté en un estado válido desde su creación. Evitar setters públicos previene que el objeto sea modificado de forma inconsistente desde el exterior.

```java
package com.sigeo.clase02;

public class Recurso {
    private final String codigo;
    private String nombre;
    private int cantidad;
    private String estado;

    public Recurso(String codigo, String nombre, int cantidad, String estado) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El código no puede ser nulo ni vacío");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo ni vacío");
        }
        if (cantidad < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa");
        }
        if (estado == null || estado.trim().isEmpty()) {
            throw new IllegalArgumentException("El estado no puede ser nulo ni vacío");
        }
        
        this.codigo = codigo;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.estado = estado;
    }

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public int getCantidad() { return cantidad; }
    public String getEstado() { return estado; }

    public void actualizarNombre(String nuevoNombre) {
        if (nuevoNombre == null || nuevoNombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nuevo nombre no puede ser nulo ni vacío");
        }
        this.nombre = nuevoNombre;
    }
    
    public void agregarCantidad(int cantidadAdicional) {
        if (cantidadAdicional <= 0) {
            throw new IllegalArgumentException("La cantidad a agregar debe ser mayor a cero");
        }
        this.cantidad += cantidadAdicional;
    }
    
    public void consumirCantidad(int cantidadAConsumir) {
        if (cantidadAConsumir <= 0) {
            throw new IllegalArgumentException("La cantidad a consumir debe ser mayor a cero");
        }
        if (this.cantidad < cantidadAConsumir) {
            throw new IllegalArgumentException("No hay suficiente cantidad disponible");
        }
        this.cantidad -= cantidadAConsumir;
    }
    
    public void cambiarEstado(String nuevoEstado) {
        if (nuevoEstado == null || nuevoEstado.trim().isEmpty()) {
            throw new IllegalArgumentException("El nuevo estado no puede ser nulo ni vacío");
        }
        this.estado = nuevoEstado;
    }
}
```

## C02-E02 — CorreoInstitucional

**Por qué:** Los objetos valor (Value Objects) deben ser inmutables y compararse por su valor, no por su identidad. Los `record` de Java son ideales para esto porque generan automáticamente constructores, getters, `equals`, `hashCode` y `toString`.

```java
package com.sigeo.clase02;

public record CorreoInstitucional(String valor) {
    
    public CorreoInstitucional {
        if (valor == null || !valor.contains("@")) {
            throw new IllegalArgumentException("Formato de correo inválido");
        }
        
        String[] partes = valor.split("@");
        if (partes.length != 2 || partes[0].isEmpty() || partes[1].isEmpty()) {
            throw new IllegalArgumentException("Formato de correo inválido");
        }
        
        // Normalizar dominio a minúsculas
        valor = partes[0] + "@" + partes[1].toLowerCase();
    }
    
    public String getDominio() {
        return valor.split("@")[1];
    }
}
```

## C02-E03 — Prioridad y SLA

**Por qué:** Los enums en Java son clases completas que pueden tener estado y comportamiento. Poner la lógica dependiente del enum dentro del mismo enum evita los "switch statements" esparcidos por el código (Feature Envy).

```java
package com.sigeo.clase02;

import java.time.LocalDateTime;

public enum Prioridad {
    BAJA(72, 1.0),
    MEDIA(48, 1.5),
    ALTA(24, 2.0),
    CRITICA(4, 3.0);
    
    private final int horasAtencion;
    private final double factorEscalamiento;
    
    Prioridad(int horasAtencion, double factorEscalamiento) {
        this.horasAtencion = horasAtencion;
        this.factorEscalamiento = factorEscalamiento;
    }
    
    public LocalDateTime deadlineFrom(LocalDateTime inicio) {
        if (inicio == null) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser nula");
        }
        return inicio.plusHours(this.horasAtencion);
    }
    
    public int getHorasAtencion() {
        return horasAtencion;
    }
    
    public double getFactorEscalamiento() {
        return factorEscalamiento;
    }
}
```

## C02-E04 — Canales de notificación

**Por qué:** El Principio de Inversión de Dependencias (DIP) establece que los módulos de alto nivel no deben depender de módulos de bajo nivel, sino de abstracciones. Esto permite cambiar la implementación (ej. de consola a SMS) sin modificar el servicio.

```java
// Notificador.java
package com.sigeo.clase02;

public interface Notificador {
    void notificar(String mensaje, String destinatario);
}

// NotificadorConsola.java
package com.sigeo.clase02;

public class NotificadorConsola implements Notificador {
    @Override
    public void notificar(String mensaje, String destinatario) {
        System.out.println("Notificando a " + destinatario + ": " + mensaje);
    }
}

// NotificadorCorreo.java
package com.sigeo.clase02;

public class NotificadorCorreo implements Notificador {
    @Override
    public void notificar(String mensaje, String destinatario) {
        System.out.println("Enviando correo a " + destinatario + ": " + mensaje);
    }
}

// NotificadorSMS.java
package com.sigeo.clase02;

public class NotificadorSMS implements Notificador {
    @Override
    public void notificar(String mensaje, String destinatario) {
        System.out.println("Enviando SMS a " + destinatario + ": " + mensaje);
    }
}

// ServicioNotificacion.java
package com.sigeo.clase02;

public class ServicioNotificacion {
    
    private final Notificador notificador;
    
    public ServicioNotificacion(Notificador notificador) {
        if (notificador == null) {
            throw new IllegalArgumentException("El notificador no puede ser nulo");
        }
        this.notificador = notificador;
    }
    
    public void alertarUrgencia(String destinatario) {
        notificador.notificar("URGENTE: Requiere su atención inmediata", destinatario);
    }
}
```

## C02-E05 — Eliminar herencia frágil

**Por qué:** "Favorecer la composición sobre la herencia". La herencia crea un acoplamiento fuerte y es estática. La composición permite cambiar comportamientos en tiempo de ejecución y evita la explosión combinatoria de subclases.

```java
// Capacidad.java
package com.sigeo.clase02;

public interface Capacidad {
    String getNombre();
}

// Sirena.java
package com.sigeo.clase02;

public class Sirena implements Capacidad {
    @Override
    public String getNombre() {
        return "Sirena";
    }
    
    public void encender() {
        System.out.println("Sirena encendida: WEE-WOO-WEE-WOO");
    }
}

// Vehiculo.java
package com.sigeo.clase02;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Vehiculo {
    private final String patente;
    private final List<Capacidad> capacidades = new ArrayList<>();
    
    public Vehiculo(String patente) {
        this.patente = patente;
    }
    
    public void agregarCapacidad(Capacidad capacidad) {
        if (capacidad != null) {
            capacidades.add(capacidad);
        }
    }
    
    public boolean tieneCapacidad(Class<? extends Capacidad> tipo) {
        return capacidades.stream().anyMatch(tipo::isInstance);
    }
    
    public <T extends Capacidad> Optional<T> getCapacidad(Class<T> tipo) {
        return capacidades.stream()
                .filter(tipo::isInstance)
                .map(tipo::cast)
                .findFirst();
    }
    
    public void encenderSirena() {
        Optional<Sirena> sirenaOpt = getCapacidad(Sirena.class);
        if (sirenaOpt.isPresent()) {
            sirenaOpt.get().encender();
        } else {
            throw new IllegalStateException("El vehículo no tiene sirena");
        }
    }
}
```

## C02-E06 — Estados sellados

**Por qué:** Las interfaces selladas (`sealed interface`) restringen qué clases pueden implementarlas. Junto con el pattern matching en `switch`, el compilador puede verificar que hemos manejado todos los casos posibles, eliminando la necesidad de un caso `default` propenso a errores.

```java
// EstadoSolicitud.java
package com.sigeo.clase02;

public sealed interface EstadoSolicitud permits 
    EstadoSolicitud.Borrador, 
    EstadoSolicitud.EnRevision, 
    EstadoSolicitud.Aprobada, 
    EstadoSolicitud.Rechazada {
    
    record Borrador(String mensaje) implements EstadoSolicitud {}
    record EnRevision(String revisor) implements EstadoSolicitud {}
    record Aprobada(String fecha, String aprobador) implements EstadoSolicitud {}
    record Rechazada(String motivo) implements EstadoSolicitud {}
}

// ProcesadorEstado.java
package com.sigeo.clase02;

public class ProcesadorEstado {
    
    public String describirEstado(EstadoSolicitud estado) {
        return switch (estado) {
            case EstadoSolicitud.Borrador b -> "Borrador pendiente: " + b.mensaje();
            case EstadoSolicitud.EnRevision r -> "En revisión por: " + r.revisor();
            case EstadoSolicitud.Aprobada a -> "Aprobada el " + a.fecha() + " por " + a.aprobador();
            case EstadoSolicitud.Rechazada r -> "Rechazada. Motivo: " + r.motivo();
        };
    }
}
```

## C02-E07 — Validador contextual

**Por qué:** Las clases internas privadas son útiles cuando una clase auxiliar necesita acceso íntimo al estado de la clase externa, pero no tiene sentido que exista fuera de ese contexto. Encapsula la lógica compleja sin exponerla.

```java
package com.sigeo.clase02;

public class AgregadoSolicitud {
    private String estadoActual = "BORRADOR";
    private int monto = 0;
    
    public AgregadoSolicitud(int monto) {
        this.monto = monto;
    }
    
    public void aprobar() {
        ValidadorTransicion validador = new ValidadorTransicion();
        if (!validador.esValida()) {
            throw new IllegalStateException("No se puede aprobar la solicitud en el estado actual o con monto inválido");
        }
        
        this.estadoActual = "APROBADA";
    }
    
    public String getEstadoActual() {
        return estadoActual;
    }
    
    private class ValidadorTransicion {
        public boolean esValida() {
            boolean estadoValido = estadoActual.equals("BORRADOR") || estadoActual.equals("EN_REVISION");
            boolean montoValido = monto > 0;
            
            return estadoValido && montoValido;
        }
    }
}
```

## C02-E08 — Cazador de olores POO

**Por qué:** Refactorizar código legado es una habilidad clave. Identificar olores (code smells) ayuda a saber qué mejorar. Extraer clases (como `Usuario` y `Solicitud`) mejora la cohesión y reduce el acoplamiento.

```java
package com.sigeo.clase02;

import java.util.ArrayList;
import java.util.List;

// Refactorización:
// 1. Extraer clase Usuario (Data Clumps)
// 2. Extraer clase Solicitud (Data Clumps)
// 3. Extraer constantes (Magic Numbers)
// 4. Dividir método largo en métodos más pequeños

public class ClaseDiosRefactorizada {
    
    private static final double LIMITE_APROBACION_AUTOMATICA = 500000;
    private static final double LIMITE_MAXIMO = 1000000;
    
    private final List<String> logs = new ArrayList<>();
    
    public record Usuario(String nombre, String email, String rut) {
        public Usuario {
            if (nombre == null || nombre.trim().isEmpty()) {
                throw new IllegalArgumentException("Nombre vacío");
            }
            if (email == null || !email.contains("@")) {
                throw new IllegalArgumentException("Email inválido");
            }
        }
    }
    
    public class Solicitud {
        private final int id;
        private final double monto;
        private String estado;
        
        public Solicitud(int id, double monto) {
            if (monto < 0) {
                throw new IllegalArgumentException("Monto negativo");
            }
            if (monto > LIMITE_MAXIMO) {
                throw new IllegalArgumentException("Monto excede límite");
            }
            this.id = id;
            this.monto = monto;
            this.estado = "NUEVA";
        }
        
        public void procesar(Usuario usuario) {
            if (this.monto > LIMITE_APROBACION_AUTOMATICA) {
                requerirAprobacionGerencia();
            } else {
                aprobarAutomaticamente(usuario);
            }
            guardarEnBD();
        }
        
        private void requerirAprobacionGerencia() {
            this.estado = "REQUIERE_APROBACION_GERENCIA";
            logs.add("Solicitud " + this.id + " requiere gerencia");
            System.out.println("Enviando email a gerente sobre solicitud " + this.id);
        }
        
        private void aprobarAutomaticamente(Usuario usuario) {
            this.estado = "APROBADA_AUTOMATICA";
            logs.add("Solicitud " + this.id + " aprobada automática");
            System.out.println("Enviando email a " + usuario.email() + " sobre aprobación");
        }
        
        private void guardarEnBD() {
            System.out.println("Guardando en BD: " + this.id + ", " + this.estado);
        }
        
        public String getEstado() { return estado; }
    }
    
    public Solicitud procesarTodo(String nombre, String email, String rut, int id, double monto) {
        try {
            Usuario usuario = new Usuario(nombre, email, rut);
            Solicitud solicitud = new Solicitud(id, monto);
            solicitud.procesar(usuario);
            return solicitud;
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }
    
    public List<String> getLogs() {
        return new ArrayList<>(logs);
    }
}
```
