package com.sigeo.clase02;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class CorreoInstitucionalTest {

    @Test
    void constructor_ConCorreoValido_CreaInstancia() {
        CorreoInstitucional correo = new CorreoInstitucional("usuario@sigeo.mil.cl");
        assertThat(correo.valor()).isEqualTo("usuario@sigeo.mil.cl");
    }

    @Test
    void constructor_NormalizaDominioAMinusculas() {
        CorreoInstitucional correo = new CorreoInstitucional("usuario@SIGEO.MIL.CL");
        assertThat(correo.valor()).isEqualTo("usuario@sigeo.mil.cl");
        assertThat(correo.getDominio()).isEqualTo("sigeo.mil.cl");
    }

    @Test
    void constructor_SinArroba_LanzaExcepcion() {
        assertThatThrownBy(() -> new CorreoInstitucional("usuariosigeo.mil.cl"))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void equals_MismoValor_SonIguales() {
        CorreoInstitucional correo1 = new CorreoInstitucional("usuario@sigeo.mil.cl");
        CorreoInstitucional correo2 = new CorreoInstitucional("usuario@SIGEO.MIL.CL");
        
        assertThat(correo1).isEqualTo(correo2);
        assertThat(correo1.hashCode()).isEqualTo(correo2.hashCode());
    }
}
