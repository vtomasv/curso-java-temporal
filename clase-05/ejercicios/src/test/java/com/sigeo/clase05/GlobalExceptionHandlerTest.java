package com.sigeo.clase05;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SolicitudController.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SolicitudService solicitudService;

    @Test
    void cuandoLanzaSolicitudNotFoundException_DebeRetornar404ProblemDetail() throws Exception {
        when(solicitudService.obtenerPorId(anyLong())).thenThrow(new SolicitudNotFoundException(999L));

        mockMvc.perform(get("/api/solicitudes/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("No encontrado"))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.detail").value("Solicitud no encontrada con ID: 999"));
    }
}
