package com.bancared.clase10;
import java.util.*;
public final class AsistenteModelos {
    private AsistenteModelos(){}
    public record Documento(String id,String titulo,String texto){}
    public record Propuesta(String respuesta,List<String> fuentes,String proveedor){}
    public record Consulta(String clave,String pregunta,String escenario){}
    public record Respuesta(String clave,String respuesta,List<String> fuentes,String estado,String proveedor){}
    public record Resumen(int transferencias,int completadas,int pendientes,long volumenCompletado,int liquidadas,int reversadas){}
}
