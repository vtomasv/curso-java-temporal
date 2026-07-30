package com.sigeo.clase16.controller;

import com.sigeo.clase16.domain.Solicitud;
import com.sigeo.clase16.repository.SolicitudRepository;
import io.temporal.client.WorkflowClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("h2")
class SolicitudControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SolicitudRepository repository;

    @MockBean
    private WorkflowClient workflowClient; // Mockeamos Temporal para no requerir el servidor en este test

    @Test
    void iniciarSolicitud_deberiaCrearSolicitudYRetornarIds() throws Exception {
        // Act
        mockMvc.perform(post("/api/solicitudes")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"descripcion\": \"Nueva solicitud de prueba\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.solicitudId").exists())
                .andExpect(jsonPath("$.workflowId").exists());

        // Assert
        assertThat(repository.findAll()).hasSize(1);
        Solicitud guardada = repository.findAll().get(0);
        assertThat(guardada.getDescripcion()).isEqualTo("Nueva solicitud de prueba");
        assertThat(guardada.getEstado()).isEqualTo("CREADO");
        assertThat(guardada.getWorkflowId()).isNotNull();
    }
}
