package com.nobless.quittances.controller;

import com.nobless.quittances.config.ApiAccessProperties;
import com.nobless.quittances.model.Propriete;
import com.nobless.quittances.service.ProprieteService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProprieteController.class)
@Import(ApiAccessProperties.class)
class ProprieteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProprieteService proprieteService;

    @Test
    void listProprietes_returnsInfoWhenEmpty() throws Exception {
        when(proprieteService.list()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/proprietes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value("[INFO] Aucune propriete en bdd"));
    }

    @Test
    void listProprietesByProprio_returnsSuccessWhenDataExists() throws Exception {
        Propriete propriete = new Propriete();
        propriete.setId(12L);
        propriete.setAdresse("12 rue des Fleurs");
        when(proprieteService.listByIdProprio(1L)).thenReturn(Collections.singletonList(propriete));

        mockMvc.perform(get("/api/proprietes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value("[SUCCESS]"));
    }

    @Test
    void createPropriete_returnsCreated() throws Exception {
        Propriete propriete = new Propriete();
        propriete.setId(10L);
        propriete.setAdresse("10 rue des Lilas");

        when(proprieteService.create(any(Propriete.class))).thenReturn(propriete);

        mockMvc.perform(post("/api/proprietes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"adresse\":\"10 rue des Lilas\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.state").value("[SUCCESS] Propriete creee"));
    }

    @Test
    void updatePropriete_returnsSuccess() throws Exception {
        Propriete propriete = new Propriete();
        propriete.setId(10L);
        propriete.setAdresse("11 rue des Lilas");

        when(proprieteService.updateById(eq(10L), any(Propriete.class))).thenReturn(propriete);

        mockMvc.perform(put("/api/proprietes/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"adresse\":\"11 rue des Lilas\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value("[SUCCESS] Propriete modifiee"));
    }

    @Test
    void deletePropriete_returnsSuccess() throws Exception {
        doNothing().when(proprieteService).deleteById(10L);

        mockMvc.perform(delete("/api/proprietes/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value("[SUCCESS] Propriete supprimee"));
    }
}
