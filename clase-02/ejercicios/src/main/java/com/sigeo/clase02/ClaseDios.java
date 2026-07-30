package com.sigeo.clase02;

import java.util.ArrayList;
import java.util.List;

// TODO(C02-E08): Identificar olores y refactorizar
// Olores presentes:
// 1. Clase Dios (hace de todo)
// 2. Feature Envy (envidia de características)
// 3. Long Method (método largo)
// 4. Primitive Obsession (obsesión por primitivos)
// 5. Data Clumps (grupos de datos)
// 6. Magic Numbers (números mágicos)
// 7. Duplicated Code (código duplicado)
// 8. Inappropriate Intimacy (intimidad inapropiada)
public class ClaseDios {
    
    // Datos de usuario
    public String nombreUsuario;
    public String emailUsuario;
    public String rutUsuario;
    
    // Datos de solicitud
    public int idSolicitud;
    public String estadoSolicitud;
    public double montoSolicitud;
    
    // Datos de base de datos (simulada)
    public List<String> logs = new ArrayList<>();
    
    public void procesarTodo(String n, String e, String r, int id, double m) {
        this.nombreUsuario = n;
        this.emailUsuario = e;
        this.rutUsuario = r;
        this.idSolicitud = id;
        this.montoSolicitud = m;
        this.estadoSolicitud = "NUEVA";
        
        // Validar usuario
        if (this.nombreUsuario == null || this.nombreUsuario.equals("")) {
            System.out.println("Error: nombre vacío");
            return;
        }
        if (!this.emailUsuario.contains("@")) {
            System.out.println("Error: email inválido");
            return;
        }
        
        // Validar solicitud
        if (this.montoSolicitud < 0) {
            System.out.println("Error: monto negativo");
            return;
        }
        if (this.montoSolicitud > 1000000) {
            System.out.println("Error: monto excede límite");
            return;
        }
        
        // Procesar
        if (this.montoSolicitud > 500000) {
            this.estadoSolicitud = "REQUIERE_APROBACION_GERENCIA";
            this.logs.add("Solicitud " + this.idSolicitud + " requiere gerencia");
            // Enviar email simulado
            System.out.println("Enviando email a gerente sobre solicitud " + this.idSolicitud);
        } else {
            this.estadoSolicitud = "APROBADA_AUTOMATICA";
            this.logs.add("Solicitud " + this.idSolicitud + " aprobada automática");
            // Enviar email simulado
            System.out.println("Enviando email a " + this.emailUsuario + " sobre aprobación");
        }
        
        // Guardar en BD simulada
        System.out.println("Guardando en BD: " + this.idSolicitud + ", " + this.estadoSolicitud);
    }
}
