package com.sigeo.clase01;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MenuSigeo {

    private final List<String> registros = new ArrayList<>();
    private boolean ejecutando = true;

    public static void main(String[] args) {
        MenuSigeo app = new MenuSigeo();
        app.iniciar();
    }

    public void iniciar() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Bienvenido a SIGEO v0 ===");
        
        while (ejecutando) {
            mostrarOpciones();
            String opcion = scanner.nextLine();
            procesarOpcion(opcion, scanner);
        }
        
        System.out.println("Saliendo de SIGEO...");
        scanner.close();
    }

    private void mostrarOpciones() {
        System.out.println("\nSeleccione una opción:");
        System.out.println("1. Alta de registro");
        System.out.println("2. Listado de registros");
        System.out.println("3. Búsqueda");
        System.out.println("4. Salir");
        System.out.print("> ");
    }

    /**
     * Procesa la opción seleccionada por el usuario.
     * 
     * TODO(C01-E06): Implementar el procesamiento de opciones:
     * - "1": Pedir un texto y agregarlo a la lista 'registros'.
     * - "2": Mostrar todos los registros numerados. Si está vacía, mostrar "No hay registros".
     * - "3": Pedir un término de búsqueda y mostrar los registros que lo contengan.
     * - "4": Cambiar 'ejecutando' a false.
     * - Cualquier otra opción: Mostrar "Opción inválida".
     */
    public void procesarOpcion(String opcion, Scanner scanner) {
        switch (opcion) {
            case "1":
                System.out.print("Ingrese el texto del registro: ");
                String texto = scanner.nextLine();
                registros.add(texto);
                break;
            case "2":
                if (registros.isEmpty()) {
                    System.out.println("No hay registros.");
                } else {
                    int i = 0;
                    for (String registro : registros) {
                        System.out.println((i + 1) + ". " + registro);
                        i++;
                    }
                }
                break;
            case "3":
                System.out.print("Ingrese el término de búsqueda: ");
                String termino = scanner.nextLine();
                boolean encontrado = false;
                for (String registro : registros) {
                    if (registro.contains(termino)) {
                        System.out.println(registro);
                        encontrado = true;
                    }
                }
                if (!encontrado) {
                    System.out.println("No se encontraron registros que contengan el término de búsqueda.");
                }
                break;
            case "4":
                ejecutando = false;
                break;
            default:
                System.out.println("Opción inválida.");
                break;
        }
    }
    
    // Métodos para testing
    public List<String> getRegistros() {
        return registros;
    }
    
    public boolean isEjecutando() {
        return ejecutando;
    }
}
