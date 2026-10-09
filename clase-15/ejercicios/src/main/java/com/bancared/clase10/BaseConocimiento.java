package com.bancared.clase10;
import java.util.*;
import static com.bancared.clase10.AsistenteModelos.*;
/** Recuperación léxica local: se entrega explícitamente el contexto, sin modelo de embeddings. */
public final class BaseConocimiento {
    private BaseConocimiento(){}
    public static List<Documento> documentos(){return List.of(
        new Documento("POL-REVERSA","Reversa antes de liquidar","Una transferencia completada puede solicitar reversa mientras no pertenezca a un lote reservado o liquidado. La reversa crea movimientos nuevos y conserva el original."),
        new Documento("POL-INCERTO","Resultado incierto","Un timeout no confirma que el banco no movió dinero. Se consulta el recibo; si no puede confirmarse el efecto, la operación queda PENDIENTE_REVISION."),
        new Documento("POL-LIQUIDACION","Liquidación entre bancos","La compensación calcula posiciones netas entre bancos. La liquidación mueve su liquidez y no vuelve a debitar ni acreditar las cuentas de los clientes.")
    );}
    public static List<Documento> recuperar(String pregunta){String p=pregunta.toLowerCase(Locale.ROOT);String id=p.contains("liquid")||p.contains("compens")?"POL-LIQUIDACION":p.contains("timeout")||p.contains("inciert")||p.contains("respuesta")?"POL-INCERTO":"POL-REVERSA";return documentos().stream().filter(d->id.equals(d.id())).toList();}
}
