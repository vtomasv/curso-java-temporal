package com.sigeo.clase02;

public class Recurso {
    private final String codigo;
    private String nombre;
    private int cantidad;
    private String estado;

    public Recurso(String codigo, String nombre, int cantidad, String estado) {
        // TODO(C02-E01): Implementar validaciones de invariantes
        // 1. codigo no puede ser null ni vacío
        // 2. nombre no puede ser null ni vacío
        // 3. cantidad no puede ser negativa
        // 4. estado no puede ser null ni vacío
        // Lanzar IllegalArgumentException con mensajes útiles si no se cumplen
        
        this.codigo = codigo;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.estado = estado;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public String getEstado() {
        return estado;
    }

    // TODO(C02-E01): Implementar métodos de comportamiento en lugar de setters generales
    // 1. actualizarNombre(String nuevoNombre)
    // 2. agregarCantidad(int cantidadAdicional)
    // 3. consumirCantidad(int cantidadAConsumir)
    // 4. cambiarEstado(String nuevoEstado)
    
    public void actualizarNombre(String nuevoNombre) {
        throw new UnsupportedOperationException("TODO C02-E01");
    }
    
    public void agregarCantidad(int cantidadAdicional) {
        throw new UnsupportedOperationException("TODO C02-E01");
    }
    
    public void consumirCantidad(int cantidadAConsumir) {
        throw new UnsupportedOperationException("TODO C02-E01");
    }
    
    public void cambiarEstado(String nuevoEstado) {
        throw new UnsupportedOperationException("TODO C02-E01");
    }
}
