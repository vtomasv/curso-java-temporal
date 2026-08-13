package com.sigeo.clase03;

import java.util.Scanner;

public class ObtenerCantidad implements Accion {

    @Override
    public void ejecutar() {
        System.out.println("Obteniendo la cantidad");
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese la cantidad: ");
        int cantidad = scanner.nextInt();
        System.out.println("La cantidad ingresada es: " + cantidad);
    }

}
