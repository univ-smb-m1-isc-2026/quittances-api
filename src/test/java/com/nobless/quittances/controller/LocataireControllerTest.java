package com.nobless.quittances.controller;

import com.nobless.quittances.model.Locataire;
import com.nobless.quittances.service.LocataireService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nobless.quittances.config.ApiAccessProperties;

@WebMvcTest(LocataireController.class)
@Import(ApiAccessProperties.class)
class LocataireControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LocataireService locataireService;

    @Test
    void listLocataires_returnsAllLocataires() throws Exception {
        when(locataireService.list()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/locataires"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value("[INFO] Aucun locataire en bdd"));
    }

    @Test
    void createLocataire_returnsCreatedStatus() throws Exception {
        Locataire l = new Locataire();
        l.setPrenom("Test");
        when(locataireService.create(any(Locataire.class))).thenReturn(l);

        mockMvc.perform(post("/api/locataires")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"prenom\":\"Test\",\"nom\":\"User\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.state").value("[SUCCESS] Locataire cree"));
    }

    @Test
    void deleteLocataire_returnsSuccess() throws Exception {
        mockMvc.perform(delete("/api/locataires/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value("[SUCCESS] Locataire supprime"));
    }
}
