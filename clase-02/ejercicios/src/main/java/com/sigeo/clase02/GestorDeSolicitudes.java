package com.sigeo.clase02;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GestorDeSolicitudes {

    private boolean ejecutando = true;
    private List<Solicitud> solicitudes = new ArrayList<>();
    private Solicitud solicitudSeleccionada = null;

    public static void main(String[] args) {
        GestorDeSolicitudes app = new GestorDeSolicitudes();
        app.iniciar();
    }

    public void iniciar() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Bienvenido a el gestor de solicitudes v0 ===");
        
        while (ejecutando) {
            mostrarOpciones();
            String opcion = scanner.nextLine();
            procesarOpcion(opcion, scanner);
        }
        
        System.out.println("Saliendo del gestor de solicitudes...");
        scanner.close();
    }

    private void mostrarOpciones() {
        System.out.println("\nSeleccione una opción:");
        System.out.println("1. Crear nueva solicitud");
        System.out.println("2. Listado de solicitudes");
        System.out.println("3. Aprobar una solicitud");
        System.out.println("4. Rechazar una solicitud");
        System.out.println("5. Enviar a revisión");
        System.out.println("6. Enviar para modificación");
        System.out.println("7. Seleccionar una solicitud");
        System.out.println("99. Salir");
        System.out.print("> ");
    }


    private void procesarOpcion(String opcion, Scanner scanner) {
        switch (opcion) {
            case "1" ->
                this.crearSolicitud(scanner);
            case "2" ->
                this.listarSolicitudes(scanner);
            case "3" -> 
                this.aprobarSolicitud(scanner);
            case "7" ->
                this.solicitudSeleccionada = this.seleccionarSolicitud(scanner);
            case "4" ->
                this.rechazarSolicitud(scanner);
            case "5", "6" ->
                System.out.println("Funcionalidad aún no implementada.");
            case "99" -> ejecutando = false;
            default -> System.out.println("Opción no válida. Intente nuevamente.");
        }
    }

    private void rechazarSolicitud(Scanner scanner) {
        try {
            this.solicitudSeleccionada.rechazar("firma de rechazo");
        } catch (IllegalStateException e) {
            System.out.println("No se puede rechazar la solicitud en su estado actual: " + this.solicitudSeleccionada.getEstado().getNombre());
        }
    }

    private void aprobarSolicitud(Scanner scanner) {
        try {
            this.solicitudSeleccionada.aprobar("firma de aprueba");
        } catch (IllegalStateException e) {
            System.out.println("No se puede aprobar la solicitud en su estado actual: " + this.solicitudSeleccionada.getEstado().getNombre());
        }
    }

    private Solicitud seleccionarSolicitud(Scanner scanner) {
        System.out.print("Ingrese el ID de la solicitud: ");
        String id = scanner.nextLine();
        for (Solicitud solicitud : this.solicitudes) {
            if (solicitud.getId().equals(id)) {
                System.out.println("Solicitud seleccionada: " + solicitud.getId());
                return solicitud;
            }
        }
        System.out.println("Solicitud no encontrada.");
        return null;    
    }

    private Solicitud crearSolicitud(Scanner scanner) {
        System.out.print("Ingrese el motivo de la solicitud: ");
        String motivo = scanner.nextLine();
        Solicitud solicitud = new Solicitud(motivo);
        this.solicitudes.add(solicitud);
        return solicitud;
    }

    private void listarSolicitudes(Scanner scanner) {
        
        for (Solicitud solicitud : this.solicitudes)
        {
            System.out.println("[ID: " + solicitud.getId() + "] - Estado de solicitud: [" + solicitud.getEstado().getNombre() + "]" );
        }
        
    }
}
