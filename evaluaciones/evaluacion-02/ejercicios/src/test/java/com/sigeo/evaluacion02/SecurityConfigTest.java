package com.sigeo.evaluacion02;

import com.sigeo.evaluacion02.api.ApiExceptionHandler;
import com.sigeo.evaluacion02.api.SolicitudController;
import com.sigeo.evaluacion02.api.SolicitudResponse;
import com.sigeo.evaluacion02.config.SecurityConfig;
import com.sigeo.evaluacion02.domain.EstadoSolicitud;
import com.sigeo.evaluacion02.domain.Prioridad;
import com.sigeo.evaluacion02.service.SolicitudService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SolicitudController.class)
@Import({SecurityConfig.class, ApiExceptionHandler.class})
class SecurityConfigTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private SolicitudService service;

    @Test
    void apiRechazaUnaConsultaAnonima() throws Exception {
        mvc.perform(get("/api/solicitudes/SOL-200"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "OPERADOR")
    void operadorNoPuedeAprobar() throws Exception {
        mvc.perform(post("/api/solicitudes/SOL-200/aprobacion"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "SUPERVISOR")
    void supervisorPuedeAprobar() throws Exception {
        when(service.aprobar("SOL-200")).thenReturn(new SolicitudResponse(
                "SOL-200", "Cabo Rojas", "Reponer equipo", Prioridad.ALTA,
                EstadoSolicitud.APROBADA, "operador", 1L));

        mvc.perform(post("/api/solicitudes/SOL-200/aprobacion"))
                .andExpect(status().isOk());
    }
}

