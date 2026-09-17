package com.sigeo.clase07;

import com.sigeo.clase07.controller.SolicitudController;
import com.sigeo.clase07.domain.Solicitud;
import com.sigeo.clase07.service.SolicitudService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SolicitudController.class)
class SolicitudControllerSliceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SolicitudService solicitudService;


    @Test
    void actualizar_ConflictoOptimista_Retorna409() throws Exception {
        // Arrange
        Solicitud request = new Solicitud("Test", "PENDIENTE");
        
        when(solicitudService.actualizarSolicitud(eq(1L), any()))
            .thenThrow(new ObjectOptimisticLockingFailureException(Solicitud.class, 1L));

        // Act & Assert
        mockMvc.perform(put("/api/solicitudes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"descripcion\":\"Test\", \"estado\":\"PENDIENTE\"}"))
                .andExpect(status().isConflict());
    }
}
