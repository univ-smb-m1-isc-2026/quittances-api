package com.nobless.quittances.controller;

import com.nobless.quittances.config.ApiAccessProperties;
import com.nobless.quittances.model.Proprio;
import com.nobless.quittances.security.JwtUtil;
import com.nobless.quittances.service.ProprioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProprioController.class)
@Import(ApiAccessProperties.class)
class ProprioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProprioService proprioService;

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    void listProprios_returnsInfoWhenEmpty() throws Exception {
        when(proprioService.list()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/proprios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value("[INFO] Aucun proprio en bdd"));
    }

    @Test
    void createProprio_returnsCreated() throws Exception {
        Proprio proprio = new Proprio();
        proprio.setId(1L);
        proprio.setEmail("mail@test.com");

        when(proprioService.create(any(Proprio.class))).thenReturn(proprio);

        mockMvc.perform(post("/api/proprios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nom\":\"Doe\",\"prenom\":\"John\",\"email\":\"mail@test.com\",\"telephone\":\"0600000000\",\"password\":\"pwd\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.state").value("[SUCCESS] Proprio cree"));
    }

    @Test
    void deleteProprio_returnsSuccess() throws Exception {
        doNothing().when(proprioService).deleteById(1L);

        mockMvc.perform(delete("/api/proprios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value("[SUCCESS] Proprio supprime"));
    }

    @Test
    void login_returnsUnauthorizedWhenEmailNotFound() throws Exception {
        when(proprioService.findByEmail("missing@test.com")).thenReturn(null);

        mockMvc.perform(post("/api/proprios/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"missing@test.com\",\"password\":\"secret\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.state").value("[ERROR] Identifiants invalides"));
    }

    @Test
    void login_returnsTokenWhenCredentialsValid() throws Exception {
        Proprio proprio = new Proprio();
        proprio.setId(99L);
        proprio.setEmail("john@test.com");
        proprio.setNom("Doe");
        proprio.setPrenom("John");
        proprio.setPassword("encoded");

        when(proprioService.findByEmail(eq("john@test.com"))).thenReturn(proprio);
        when(proprioService.passwordMatches("secret", "encoded")).thenReturn(true);
        when(jwtUtil.generateToken(proprio)).thenReturn("jwt-token");

        mockMvc.perform(post("/api/proprios/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"john@test.com\",\"password\":\"secret\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value("[SUCCESS] Connexion reussie"))
                .andExpect(jsonPath("$.data.token").value("jwt-token"));
    }
}
