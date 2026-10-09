package com.bancared.clase10;
import static com.bancared.clase10.AsistenteModelos.*;
import java.util.*;
public final class RespuestaSegura {
    private RespuestaSegura(){}
    public static Propuesta validar(Propuesta propuesta,List<Documento> contexto){
        Set<String> permitidas=new HashSet<>(contexto.stream().map(Documento::id).toList());
        if(propuesta==null||propuesta.respuesta()==null||propuesta.respuesta().isBlank()||propuesta.respuesta().length()>600||propuesta.fuentes()==null||propuesta.fuentes().isEmpty()||!permitidas.containsAll(propuesta.fuentes()))return fallback(contexto);
        return propuesta;
    }
    public static Propuesta fallback(List<Documento> contexto){var d=contexto.getFirst();return new Propuesta(d.texto(),List.of(d.id()),"FALLBACK_LOCAL");}
}
