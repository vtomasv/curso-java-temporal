package com.sigeo.evaluacion02;

import com.sigeo.evaluacion02.api.ApiExceptionHandler;
import com.sigeo.evaluacion02.api.SolicitudController;
import com.sigeo.evaluacion02.api.SolicitudResponse;
import com.sigeo.evaluacion02.config.SecurityConfig;
import com.sigeo.evaluacion02.domain.EstadoSolicitud;
import com.sigeo.evaluacion02.domain.Prioridad;
import com.sigeo.evaluacion02.service.SolicitudNoEncontradaException;
import com.sigeo.evaluacion02.service.SolicitudService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SolicitudController.class)
@Import({SecurityConfig.class, ApiExceptionHandler.class})
class SolicitudControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private SolicitudService service;

    @Test
    @WithMockUser(username = "operador", roles = "OPERADOR")
    void crearResponde201LocationYUsaLaIdentidadAutenticada() throws Exception {
        when(service.crear(any(), eq("operador"), eq("KEY-200")))
                .thenReturn(response("SOL-200", EstadoSolicitud.PENDIENTE));

        mvc.perform(post("/api/solicitudes")
                        .header("Idempotency-Key", "KEY-200")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "id": "SOL-200",
                                  "solicitante": "Cabo Rojas",
                                  "descripcion": "Reponer equipo",
                                  "prioridad": "ALTA"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/solicitudes/SOL-200"))
                .andExpect(jsonPath("$.id").value("SOL-200"));

        verify(service).crear(any(), eq("operador"), eq("KEY-200"));
    }

    @Test
    @WithMockUser(username = "operador", roles = "OPERADOR")
    void unaEntradaInvalidaResponde400SinInvocarElServicio() throws Exception {
        mvc.perform(post("/api/solicitudes")
                        .header("Idempotency-Key", "KEY-200")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"id":"", "solicitante":"", "descripcion":"", "prioridad":null}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").isNotEmpty());

        verifyNoInteractions(service);
    }

    @Test
    @WithMockUser(username = "lector", roles = "LECTOR")
    void idInexistenteRespondeProblemDetail404() throws Exception {
        when(service.buscarPorId("SOL-404"))
                .thenThrow(new SolicitudNoEncontradaException("SOL-404"));

        mvc.perform(get("/api/solicitudes/SOL-404"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Solicitud no encontrada"))
                .andExpect(jsonPath("$.detail").value(org.hamcrest.Matchers.containsString("SOL-404")));
    }

    private static SolicitudResponse response(String id, EstadoSolicitud estado) {
        return new SolicitudResponse(
                id, "Cabo Rojas", "Reponer equipo", Prioridad.ALTA,
                estado, "operador", 0L);
    }
}

