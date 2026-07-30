package com.sigeo.clase08;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OwnershipSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "user1", roles = "OPERADOR")
    void userCanEditOwnSolicitud() throws Exception {
        // user1 es el propietario de la solicitud 1
        mockMvc.perform(put("/api/solicitudes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"descripcion\":\"Updated\"}")
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user2", roles = "OPERADOR")
    void userCannotEditOtherSolicitud() throws Exception {
        // user2 NO es el propietario de la solicitud 1
        mockMvc.perform(put("/api/solicitudes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"descripcion\":\"Updated\"}")
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "supervisor1", roles = "SUPERVISOR")
    void supervisorCanEditAnySolicitud() throws Exception {
        // supervisor puede editar cualquier solicitud
        mockMvc.perform(put("/api/solicitudes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"descripcion\":\"Updated\"}")
                        .with(csrf()))
                .andExpect(status().isOk());
    }
}
