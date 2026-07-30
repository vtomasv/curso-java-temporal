package com.sigeo.clase07;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sigeo.clase07.controller.SolicitudController;
import com.sigeo.clase07.domain.Solicitud;
import com.sigeo.clase07.service.SolicitudService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
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

    @MockBean
    private SolicitudService solicitudService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void actualizar_ConflictoOptimista_Retorna409() throws Exception {
        // Arrange
        Solicitud request = new Solicitud("Test", "PENDIENTE");
        
        when(solicitudService.actualizarSolicitud(eq(1L), any()))
            .thenThrow(new ObjectOptimisticLockingFailureException(Solicitud.class, 1L));

        // Act & Assert
        // TODO(C07-E05): El test fallará hasta que se maneje la excepción en el controller
        mockMvc.perform(put("/api/solicitudes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }
}
