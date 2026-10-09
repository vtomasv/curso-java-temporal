package com.bancared.clase10;
import static com.bancared.clase10.AsistenteModelos.*;
import java.util.*;
public final class RespuestaSegura {
    private RespuestaSegura(){}
    public static Propuesta validar(Propuesta propuesta,List<Documento> contexto){
        // TODO(C15-E02): validar respuesta 1..600 caracteres y fuentes no vacías que pertenezcan al contexto recuperado. Salida inválida usa fallback citado, sin ejecutar acciones bancarias.
        return propuesta;
    }
    public static Propuesta fallback(List<Documento> contexto){var d=contexto.getFirst();return new Propuesta(d.texto(),List.of(d.id()),"FALLBACK_LOCAL");}
}
