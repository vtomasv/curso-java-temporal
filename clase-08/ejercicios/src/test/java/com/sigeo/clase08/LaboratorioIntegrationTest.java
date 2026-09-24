package com.sigeo.clase08;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.*;
import com.sigeo.clase08.service.LabTokenService;
import java.security.*;
import java.util.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = "lab.password=clase08-demo")
@AutoConfigureMockMvc
class LaboratorioIntegrationTest {
    @Autowired MockMvc mvc;
    @Autowired LabTokenService tokens;
    @Autowired KeyPair keys;
    @Autowired UserDetailsService users;
    @Autowired PasswordEncoder passwords;
    String bearer(String name, String role, String scenario) {
        var auth = UsernamePasswordAuthenticationToken.authenticated(name, "", List.of(new SimpleGrantedAuthority("ROLE_" + role)));
        return "Bearer " + tokens.issue(auth, scenario);
    }
    String valid() { return bearer("user1", "OPERADOR", "valid"); }
    @Test void loginPageIsPublicAndHasCsrf() throws Exception {
        mvc.perform(get("/login")).andExpect(status().isOk()).andExpect(content().string(containsString("name=\"_csrf\"")));
    }
    @Test void laboratoryRequiresLogin() throws Exception {
        mvc.perform(get("/laboratorio")).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/login"));
    }
    @Test void correctPasswordOpensLab() throws Exception {
        mvc.perform(formLogin().user("user1").password("clase08-demo")).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/laboratorio"));
    }
    @Test void incorrectPasswordIsRejected() throws Exception {
        mvc.perform(formLogin().user("user1").password("incorrecta")).andExpect(redirectedUrl("/login?error"));
    }
    @Test void loginWithoutCsrfIsRejected() throws Exception {
        mvc.perform(post("/login").param("username","user1").param("password","clase08-demo")).andExpect(status().isForbidden());
    }
    @Test void passwordsUseBcrypt() {
        for (String name : List.of("lector","user1","user2","supervisor")) {
            var encoded = users.loadUserByUsername(name).getPassword();
            assertThat(encoded).startsWith("$2");
            assertThat(passwords.matches("clase08-demo", encoded)).isTrue();
            assertThat(encoded).isNotEqualTo("clase08-demo");
        }
    }
    @Test void publicApiWorksWithoutToken() throws Exception {
        mvc.perform(get("/api/public/info")).andExpect(status().isOk());
    }
    @Test void unknownRouteIsDeniedEvenWithValidJwt() throws Exception {
        mvc.perform(get("/api/unknown").header("Authorization", valid())).andExpect(status().isForbidden());
    }
    @Test void genuineJwtPassesAllChecks() throws Exception {
        mvc.perform(get("/api/jwt/validar").header("Authorization",valid()))
            .andExpect(status().isOk()).andExpect(jsonPath("$.valid").value(true))
            .andExpect(jsonPath("$.subject").value("user1"))
            .andExpect(jsonPath("$.checks.signature").value(true))
            .andExpect(jsonPath("$.checks.audience").value(true));
    }
    @ParameterizedTest @ValueSource(strings={"expired","issuer","audience","future"})
    void signedInvalidClaimsAreRejected(String scenario) throws Exception {
        mvc.perform(get("/api/jwt/validar").header("Authorization",bearer("user1","OPERADOR",scenario)))
            .andExpect(status().isUnauthorized());
    }
    @Test void alteredSignatureIsRejected() throws Exception {
        String[] parts=valid().substring(7).split("\\.");
        parts[2]=(parts[2].startsWith("A") ? "B" : "A")+parts[2].substring(1);
        mvc.perform(get("/api/jwt/validar").header("Authorization","Bearer "+String.join(".",parts)))
            .andExpect(status().isUnauthorized());
    }
    @Test void differentSigningKeyIsRejected() throws Exception {
        var generator=KeyPairGenerator.getInstance("RSA"); generator.initialize(2048);
        var parsed=SignedJWT.parse(valid().substring(7));
        parsed=new SignedJWT(parsed.getHeader(),parsed.getJWTClaimsSet());
        parsed.sign(new RSASSASigner(generator.generateKeyPair().getPrivate()));
        mvc.perform(get("/api/jwt/validar").header("Authorization","Bearer "+parsed.serialize()))
            .andExpect(status().isUnauthorized());
    }
    @Test void unsignedTokenIsRejected() throws Exception {
        var plain=new PlainJWT(SignedJWT.parse(valid().substring(7)).getJWTClaimsSet());
        mvc.perform(get("/api/jwt/validar").header("Authorization","Bearer "+plain.serialize()))
            .andExpect(status().isUnauthorized());
    }
    @Test void missingExpirationIsRejected() throws Exception {
        var parsed=SignedJWT.parse(valid().substring(7));
        var claims=new JWTClaimsSet.Builder(parsed.getJWTClaimsSet()).expirationTime(null).build();
        var jwt=new SignedJWT(parsed.getHeader(),claims); jwt.sign(new RSASSASigner(keys.getPrivate()));
        mvc.perform(get("/api/jwt/validar").header("Authorization","Bearer "+jwt.serialize())).andExpect(status().isUnauthorized());
    }
    @Test void sessionAloneCannotAuthenticateApi() throws Exception {
        var session=(MockHttpSession)mvc.perform(formLogin().user("user1").password("clase08-demo"))
            .andReturn().getRequest().getSession(false);
        mvc.perform(get("/api/solicitudes").session(session)).andExpect(status().isUnauthorized());
    }
    @Test void issuanceRequiresCsrf() throws Exception {
        mvc.perform(post("/lab/token").with(user("user1").roles("OPERADOR"))).andExpect(status().isForbidden());
    }
    @Test void issuanceUsesSessionIdentity() throws Exception {
        mvc.perform(post("/lab/token").with(user("user1").roles("OPERADOR")).with(csrf()))
            .andExpect(status().isOk()).andExpect(jsonPath("$.token").isString());
    }
    @Test void invalidScenarioIsBadRequest() throws Exception {
        mvc.perform(post("/lab/token").param("scenario","admin").with(user("user1")).with(csrf()))
            .andExpect(status().isBadRequest());
    }
    @Test void jwtWithoutAllowedRoleCannotRead() throws Exception {
        mvc.perform(get("/api/solicitudes").header("Authorization",bearer("otro","INVITADO","valid")))
            .andExpect(status().isForbidden());
    }
    @Test void lectorCannotCreateWithRealJwt() throws Exception {
        mvc.perform(post("/api/solicitudes").header("Authorization",bearer("lector","LECTOR","valid"))
            .contentType(MediaType.APPLICATION_JSON).content("{\"descripcion\":\"Ejemplo\"}"))
            .andExpect(status().isForbidden());
    }
    @Test void clientCannotAssignOwnerOrApproval() throws Exception {
        mvc.perform(post("/api/solicitudes").header("Authorization",valid()).contentType(MediaType.APPLICATION_JSON)
            .content("{\"descripcion\":\"Intento\",\"propietario\":\"supervisor\",\"estado\":\"APROBADO\",\"id\":999}"))
            .andExpect(status().isOk()).andExpect(jsonPath("$.propietario").value("user1"))
            .andExpect(jsonPath("$.estado").value("PENDIENTE")).andExpect(jsonPath("$.id").value(not(999)));
    }
    @Test void ownerCanEditWithRealJwt() throws Exception {
        mvc.perform(put("/api/solicitudes/1").header("Authorization",valid()).contentType(MediaType.APPLICATION_JSON)
            .content("{\"descripcion\":\"Actualización propia\"}"))
            .andExpect(status().isOk()).andExpect(jsonPath("$.propietario").value("user1"));
    }
    @Test void otherOperatorCannotEditWithRealJwt() throws Exception {
        mvc.perform(put("/api/solicitudes/1").header("Authorization",bearer("user2","OPERADOR","valid"))
            .contentType(MediaType.APPLICATION_JSON).content("{\"descripcion\":\"Cruce\"}"))
            .andExpect(status().isForbidden());
    }
    @Test void supervisorCanEditWithRealJwt() throws Exception {
        mvc.perform(put("/api/solicitudes/2").header("Authorization",bearer("supervisor","SUPERVISOR","valid"))
            .contentType(MediaType.APPLICATION_JSON).content("{\"descripcion\":\"Supervisión\"}"))
            .andExpect(status().isOk());
    }
    @Test void operatorCannotApproveWithRealJwt() throws Exception {
        mvc.perform(post("/api/solicitudes/1/approve").header("Authorization",valid())).andExpect(status().isForbidden());
    }
    @Test void supervisorApprovalIsPersisted() throws Exception {
        String jwt=bearer("supervisor","SUPERVISOR","valid");
        mvc.perform(post("/api/solicitudes/2/approve").header("Authorization",jwt)).andExpect(status().isOk());
        mvc.perform(get("/api/solicitudes").header("Authorization",jwt))
            .andExpect(jsonPath("$[?(@.id == 2)].estado", hasItem("APROBADO")));
    }
    @Test void blankDescriptionIsBadRequest() throws Exception {
        mvc.perform(post("/api/solicitudes").header("Authorization",valid()).contentType(MediaType.APPLICATION_JSON)
            .content("{\"descripcion\":\"\"}")).andExpect(status().isBadRequest());
    }
    @Test void malformedJsonDoesNotLeakStackTrace() throws Exception {
        mvc.perform(post("/api/solicitudes").header("Authorization",valid()).contentType(MediaType.APPLICATION_JSON)
            .content("{")).andExpect(status().isBadRequest()).andExpect(content().string(not(containsString("at com.sigeo"))));
    }
    @Test void missingResourceIs404ForSupervisor() throws Exception {
        mvc.perform(post("/api/solicitudes/99999/approve").header("Authorization",bearer("supervisor","SUPERVISOR","valid")))
            .andExpect(status().isNotFound());
    }
    @Test void missingResourceDoesNotRevealExistenceToOperator() throws Exception {
        mvc.perform(put("/api/solicitudes/99999").header("Authorization",valid()).contentType(MediaType.APPLICATION_JSON)
            .content("{\"descripcion\":\"No existe\"}")).andExpect(status().isForbidden());
    }
    @Test void allowedPreflightSucceedsWithoutJwt() throws Exception {
        mvc.perform(options("/api/solicitudes").header("Origin","http://localhost:8080")
            .header("Access-Control-Request-Method","POST").header("Access-Control-Request-Headers","Authorization,Content-Type"))
            .andExpect(status().isOk()).andExpect(header().string("Access-Control-Allow-Origin","http://localhost:8080"));
    }
    @Test void untrustedOriginIsRejected() throws Exception {
        mvc.perform(options("/api/solicitudes").header("Origin","https://otro.local").header("Access-Control-Request-Method","POST"))
            .andExpect(status().isForbidden());
    }
    @Test void labAndFormRenderAndHaveSecurityHeaders() throws Exception {
        mvc.perform(get("/laboratorio").with(user("user1"))).andExpect(status().isOk())
            .andExpect(content().string(containsString("Validar JWT"))).andExpect(header().string("X-Content-Type-Options","nosniff"));
        mvc.perform(get("/formulario").with(user("user1"))).andExpect(status().isOk())
            .andExpect(content().string(containsString("name=\"_csrf\"")));
    }
    @Test void logoutRequiresCsrf() throws Exception {
        mvc.perform(post("/logout").with(user("user1"))).andExpect(status().isForbidden());
        mvc.perform(logout()).andExpect(redirectedUrl("/login?logout"));
    }
}
