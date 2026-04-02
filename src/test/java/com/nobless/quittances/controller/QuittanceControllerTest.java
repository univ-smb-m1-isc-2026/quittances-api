package com.nobless.quittances.controller;

import com.nobless.quittances.config.ApiAccessProperties;
import com.nobless.quittances.model.Quittance;
import com.nobless.quittances.service.QuittanceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(QuittanceController.class)
@Import(ApiAccessProperties.class)
class QuittanceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QuittanceService quittanceService;

    @Test
    void listQuittances_returnsInfoWhenEmpty() throws Exception {
        when(quittanceService.list()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/quittances"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value("[INFO] Aucune quittance en bdd"));
    }

    @Test
    void listQuittancesByProprio_returnsSuccessWhenDataExists() throws Exception {
        Quittance quittance = new Quittance();
        quittance.setId(7L);
        quittance.setPeriod("avril 2026");
        when(quittanceService.listByProprioId(1L)).thenReturn(Collections.singletonList(quittance));

        mockMvc.perform(get("/api/quittances/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value("[SUCCESS]"));
    }

    @Test
    void createQuittance_returnsCreated() throws Exception {
        Quittance quittance = new Quittance();
        quittance.setId(42L);
        quittance.setPeriod("mars 2026");

        when(quittanceService.create(any(Quittance.class))).thenReturn(quittance);

        mockMvc.perform(post("/api/quittances")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"period\":\"mars 2026\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.state").value("[SUCCESS] Quittance creee"));
    }

    @Test
    void deleteQuittance_returnsSuccess() throws Exception {
        doNothing().when(quittanceService).delete(42L);

        mockMvc.perform(delete("/api/quittances/42"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value("[SUCCESS] Quittance supprimee"));
    }
}
