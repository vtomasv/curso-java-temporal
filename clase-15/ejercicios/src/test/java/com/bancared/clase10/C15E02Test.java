package com.bancared.clase10;
import org.junit.jupiter.api.*;
import java.util.List;
import static com.bancared.clase10.AsistenteModelos.*;
import static org.junit.jupiter.api.Assertions.*;
@Tag("lab-e02") class C15E02Test {
    @Test void fuentesInventadasUsanFallback(){var contexto=BaseConocimiento.recuperar("reversa");var mala=new Propuesta("Regla inventada",List.of("INVENTADA"),"MODELO_SIMULADO");var r=RespuestaSegura.validar(mala,contexto);assertEquals("FALLBACK_LOCAL",r.proveedor());assertEquals(List.of("POL-REVERSA"),r.fuentes());}
    @Test void estructuraInvalidaSeRechaza(){var c=BaseConocimiento.recuperar("timeout");assertEquals("FALLBACK_LOCAL",RespuestaSegura.validar(new Propuesta("",List.of(),"MODELO_SIMULADO"),c).proveedor());}
}
