package com.sigeo.clase05;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SolicitudController.class)
class SolicitudControllerMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SolicitudService solicitudService;

    @Test
    void testCrearSolicitud_DebeRetornar201YLocation() throws Exception {
        Solicitud mockSolicitud = new Solicitud(1L, "Test", "Desc", "CREADA", "ALTA");
        when(solicitudService.crearSolicitud(anyString(), anyString(), anyString())).thenReturn(mockSolicitud);

        String json = """
                {
                    "titulo": "Test",
                    "descripcion": "Desc",
                    "prioridad": "ALTA"
                }
                """;

        mockMvc.perform(post("/api/solicitudes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/solicitudes/1"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.estado").value("CREADA"));
    }

    @Test
    void testCrearSolicitud_ConDatosInvalidos_DebeRetornar400() throws Exception {
        String json = """
                {
                    "descripcion": "Desc",
                    "prioridad": "ALTA"
                }
                """; // Falta titulo

        mockMvc.perform(post("/api/solicitudes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Error de validación"));
    }

    @Test
    void testConsultarFiltros_DebeRetornarLista() throws Exception {
        Solicitud mockSolicitud = new Solicitud(1L, "Test", "Desc", "CREADA", "ALTA");
        when(solicitudService.buscarSolicitudes("CREADA", null)).thenReturn(List.of(mockSolicitud));

        mockMvc.perform(get("/api/solicitudes?estado=CREADA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }
}
