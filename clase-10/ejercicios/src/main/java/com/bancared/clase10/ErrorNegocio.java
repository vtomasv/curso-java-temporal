package com.bancared.clase10;
public class ErrorNegocio extends RuntimeException {
    private final String tipo;
    public ErrorNegocio(String tipo, String mensaje) { super(mensaje); this.tipo = tipo; }
    public String tipo() { return tipo; }
}
