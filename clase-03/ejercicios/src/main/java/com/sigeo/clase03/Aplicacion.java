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
        switch (opcion) {
            case "1" ->
            System.out.println("");
            case "99" -> ejecutando = false;
            default -> System.out.println("Opción no válida. Intente nuevamente.");
        }    
    
    }


    private void mostrarOpciones() {

        this.seleccionado.mostrar(System.out, 1, "", 0);

    }


    private void crearMenu(){

        Complejo fruta = new Complejo("Frutas");
        Menu manzana = new Simple("Manzana", "Fruta roja y dulce");
        Menu pera = new Simple("Pera", "Fruta verde y jugosa");

        fruta.addMenu(manzana);
        fruta.addMenu(pera);

        this.menu.addMenu(fruta);

        Complejo verdura = new Complejo("Verduras");
        Menu lechuga = new Simple("Lechuga", "Verdura verde y crujiente");
        Menu tomate = new Simple("Tomate", "Verdura roja y jugosa");

        verdura.addMenu(lechuga);
        verdura.addMenu(tomate);

        this.menu.addMenu(verdura);

        Complejo carne = new Complejo("Carnes");
        Menu cerdo = new Simple("Cerdo", "Carne blanca y tierna");
        Menu res = new Simple("Res", "Carne roja y jugosa");

        carne.addMenu(cerdo);
        carne.addMenu(res);

        this.menu.addMenu(carne);


        Complejo ultraProcesados = new Complejo("Ultra procesados");
        Menu embutidos = new Simple("Embutidos", "Salchicas, Chorizos, fiambres");

        ultraProcesados.addMenu(embutidos);

        this.menu.addMenu(embutidos);
    }

}
