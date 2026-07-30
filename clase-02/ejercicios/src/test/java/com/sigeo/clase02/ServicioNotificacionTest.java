package com.sigeo.clase02;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class ServicioNotificacionTest {

    // Fake para pruebas
    static class NotificadorFake implements Notificador {
        String ultimoMensaje;
        String ultimoDestinatario;
        
        @Override
        public void notificar(String mensaje, String destinatario) {
            this.ultimoMensaje = mensaje;
            this.ultimoDestinatario = destinatario;
        }
    }

    @Test
    void alertarUrgencia_UsaNotificadorInyectado() {
        // TODO(C02-E04): Descomentar cuando ServicioNotificacion acepte Notificador en constructor
        /*
        NotificadorFake fake = new NotificadorFake();
        ServicioNotificacion servicio = new ServicioNotificacion(fake);
        
        servicio.alertarUrgencia("jefe@sigeo.mil.cl");
        
        assertThat(fake.ultimoDestinatario).isEqualTo("jefe@sigeo.mil.cl");
        assertThat(fake.ultimoMensaje).isNotNull().isNotEmpty();
        */
        fail("TODO C02-E04: Implementar inyección de dependencias");
    }
}
