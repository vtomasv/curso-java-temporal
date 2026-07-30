package com.sigeo.clase05;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SolicitudWebController.class)
class SolicitudWebControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SolicitudService solicitudService;

    @Test
    void listar_DebeRetornarVistaListado() throws Exception {
        when(solicitudService.buscarSolicitudes(null, null)).thenReturn(List.of());

        mockMvc.perform(get("/web/solicitudes"))
                .andExpect(status().isOk())
                .andExpect(view().name("listado"))
                .andExpect(model().attributeExists("solicitudes"));
    }

    @Test
    void mostrarFormulario_DebeRetornarVistaFormulario() throws Exception {
        mockMvc.perform(get("/web/solicitudes/nueva"))
                .andExpect(status().isOk())
                .andExpect(view().name("formulario"))
                .andExpect(model().attributeExists("solicitud"));
    }

    @Test
    void guardar_ConDatosValidos_DebeRedirigir() throws Exception {
        mockMvc.perform(post("/web/solicitudes/nueva")
                .param("titulo", "Test")
                .param("descripcion", "Desc")
                .param("prioridad", "ALTA"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/web/solicitudes"));
    }

    @Test
    void guardar_ConDatosInvalidos_DebeRetornarFormulario() throws Exception {
        mockMvc.perform(post("/web/solicitudes/nueva")
                .param("descripcion", "Desc")) // Falta titulo
                .andExpect(status().isOk())
                .andExpect(view().name("formulario"))
                .andExpect(model().hasErrors());
    }
}
