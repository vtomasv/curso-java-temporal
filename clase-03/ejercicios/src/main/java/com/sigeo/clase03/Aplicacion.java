package com.sigeo.clase03;

import java.util.Scanner;

public class Aplicacion {

    private Complejo menu = null; 
    private boolean ejecutando = true;

    private Menu seleccionado = null;

    public Aplicacion(){
        this.menu = new Complejo("Menu principal - Tipos de comida");
        this.crearMenu();
    }


    public static void main(String[] args){

        Aplicacion app = new Aplicacion();
        app.seleccionado = app.menu; 

        app.iniciar();


    }

    private void iniciar() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Bienvenido a nuestro sistema de gesión de menus v0 ===");
        
        while (ejecutando) {
            mostrarOpciones();
            String opcion = scanner.nextLine();
            procesarOpcion(opcion, scanner);
        }

        System.out.println("Saliendo del gestor de menu...");
        scanner.close();
        
    }


    private void procesarOpcion(String opcion, Scanner scanner) {

    
       try {
            int opcionInt = Integer.parseInt(opcion.trim());
            switch (opcionInt) {
                case 99 -> {
                    this.ejecutando = false;
                    return; 
                }
                case 0 -> {
                    if (this.seleccionado != this.menu) {
                        this.seleccionado = this.menu; 
                    } else {
                        System.out.println("Ya estás en el menú principal.");
                    }
                    return;
                }
            }

            this.seleccionado = this.seleccionado.getMenu(opcionInt).ejecutarAccion();
        } catch (NumberFormatException e) {
            System.out.println("Por favor, ingrese un número válido.");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Opción inválida. Por favor, seleccione una opción válida.");
        } catch (Exception e) {
            System.out.println("Ocurrió un error: " + e.getMessage());
        }




   
    }


    private void mostrarOpciones() {

        this.seleccionado.mostrar(System.out, 1, "", 0);

    }


    private void crearMenu(){

        Complejo fruta = new Complejo("Frutas");
        Menu manzana = new Simple("Manzana", "Fruta roja y dulce", fruta);
        manzana.setAccion(new ObtenerCantidad());
        Menu pera = new Simple("Pera", "Fruta verde y jugosa", fruta);
        pera.setAccion(new ObtenerCantidad());

        fruta.addMenu(manzana);
        fruta.addMenu(pera);

        this.menu.addMenu(fruta);

        Complejo verdura = new Complejo("Verduras");
        Menu lechuga = new Simple("Lechuga", "Verdura verde y crujiente", verdura);
        lechuga.setAccion(new ObtenerCantidad());
        Menu tomate = new Simple("Tomate", "Verdura roja y jugosa", verdura);
        tomate.setAccion(new ObtenerCantidad());

        verdura.addMenu(lechuga);
        verdura.addMenu(tomate);

        this.menu.addMenu(verdura);

        Linea linea = new Linea("Linea divisoria");
        this.menu.addMenu(linea);


        Complejo carne = new Complejo("Carnes");
        Menu cerdo = new Simple("Cerdo", "Carne blanca y tierna", carne);
        cerdo.setAccion(new ObtenerCantidad());
        Menu res = new Simple("Res", "Carne roja y jugosa", carne);
        res.setAccion(new ObtenerCantidad());

        carne.addMenu(cerdo);
        carne.addMenu(res);

        this.menu.addMenu(carne);


        Complejo ultraProcesados = new Complejo("Ultra procesados");
        Menu embutidos = new Simple("Embutidos", "Salchicas, Chorizos, fiambres", ultraProcesados);
        embutidos.setAccion(new ObtenerCantidad());

        ultraProcesados.addMenu(embutidos);

        this.menu.addMenu(ultraProcesados);
    }

}
