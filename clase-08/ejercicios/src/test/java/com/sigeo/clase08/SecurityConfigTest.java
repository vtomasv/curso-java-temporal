package com.sigeo.clase08;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testDefaultDeny() throws Exception {
        // Cualquier endpoint no declarado debe estar protegido (401 Unauthorized)
        mockMvc.perform(get("/api/unknown"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "user", roles = "LECTOR")
    void testLabUsers() throws Exception {
        // Con un usuario autenticado, debería poder acceder a un endpoint protegido (o al menos no dar 401)
        // Dependiendo de la configuración de roles, podría dar 403 o 200, pero no 401.
        mockMvc.perform(get("/api/solicitudes"))
                .andExpect(status().isOk());
    }
}
