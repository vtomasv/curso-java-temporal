package com.bancared.clase10;
public final class PoliticaAprobacion {
    private PoliticaAprobacion() {}
    public static void validar(long monto,int plazoSegundos){
        if(monto<1||monto>2_000_000L||plazoSegundos<5||plazoSegundos>300)throw new ErrorNegocio("VALIDACION","Monto 1..2.000.000 y plazo 5..300 segundos.");
    }
}
