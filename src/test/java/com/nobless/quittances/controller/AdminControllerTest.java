package com.nobless.quittances.controller;

import com.nobless.quittances.config.ApiAccessProperties;
import com.nobless.quittances.model.Admin;
import com.nobless.quittances.security.JwtUtil;
import com.nobless.quittances.service.AdminService;
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

@WebMvcTest(AdminController.class)
@Import(ApiAccessProperties.class)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService;

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    void listAdmins_returnsInfoWhenEmpty() throws Exception {
        when(adminService.list()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/admins"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value("[INFO] Aucun admin en bdd"));
    }

    @Test
    void createAdmin_returnsCreated() throws Exception {
        Admin admin = new Admin();
        admin.setId(1L);
        admin.setLogin("root");

        when(adminService.create(any(Admin.class))).thenReturn(admin);

        mockMvc.perform(post("/api/admins")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\":\"root\",\"password\":\"secret\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.state").value("[SUCCESS] Admin cree"));
    }

    @Test
    void updateAdmin_returnsSuccess() throws Exception {
        Admin admin = new Admin();
        admin.setId(1L);
        admin.setLogin("root2");

        when(adminService.updateById(eq(1L), any(Admin.class))).thenReturn(admin);

        mockMvc.perform(put("/api/admins/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\":\"root2\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value("[SUCCESS] Admin modifie"));
    }

    @Test
    void deleteAdmin_returnsSuccess() throws Exception {
        doNothing().when(adminService).deleteById(1L);

        mockMvc.perform(delete("/api/admins/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value("[SUCCESS] Admin supprime"));
    }

    @Test
    void loginAdmin_returnsUnauthorizedWhenLoginPatternInvalid() throws Exception {
        mockMvc.perform(post("/api/admins/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"john@test.com\",\"password\":\"secret\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.state").value("[ERROR] Identifiants invalides"));
    }

    @Test
    void loginAdmin_returnsUnauthorizedWhenAdminNotFound() throws Exception {
        when(adminService.findByLogin("admin@root.com")).thenReturn(null);

        mockMvc.perform(post("/api/admins/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"admin@root.com\",\"password\":\"secret\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.state").value("[ERROR] Identifiants invalides"));
    }

    @Test
    void loginAdmin_returnsTokenWhenCredentialsValid() throws Exception {
        Admin admin = new Admin();
        admin.setId(1L);
        admin.setLogin("admin@root.com");
        admin.setPassword("encoded");

        when(adminService.findByLogin("admin@root.com")).thenReturn(admin);
        when(adminService.passwordMatches("secret", "encoded")).thenReturn(true);
        when(jwtUtil.generateAdminToken(admin)).thenReturn("admin-token");

        mockMvc.perform(post("/api/admins/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"admin@root.com\",\"password\":\"secret\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value("[SUCCESS] Connexion reussie"))
                .andExpect(jsonPath("$.data.token").value("admin-token"))
                .andExpect(jsonPath("$.data.admin").value(true));
    }
}
