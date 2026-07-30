package com.sigeo.clase08;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CsrfSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    void postFormWithoutCsrfIsForbidden() throws Exception {
        mockMvc.perform(post("/formulario")
                        .param("dato", "test"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    void postFormWithCsrfIsRedirected() throws Exception {
        mockMvc.perform(post("/formulario")
                        .param("dato", "test")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());
    }
}
