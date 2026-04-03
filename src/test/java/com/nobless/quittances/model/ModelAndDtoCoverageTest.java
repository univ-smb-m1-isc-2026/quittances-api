package com.nobless.quittances.model;

import com.nobless.quittances.controller.dto.ApiResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ModelAndDtoCoverageTest {

    @Test
    void locataire_constructorAndAccessors_work() {
        Locataire locataire = new Locataire("Doe", "Jane", "jane@example.com", "0612345678", "ignored");
        locataire.setId(1L);
        locataire.setNom("Durand");
        locataire.setPrenom("Julie");
        locataire.setEmail("julie@example.com");
        locataire.setTelephone("0600000000");

        assertEquals(1L, locataire.getId());
        assertEquals("Durand", locataire.getNom());
        assertEquals("Julie", locataire.getPrenom());
        assertEquals("julie@example.com", locataire.getEmail());
        assertEquals("0600000000", locataire.getTelephone());
    }

    @Test
    void proprio_constructorAndAccessors_work() {
        Proprio proprio = new Proprio("Martin", "Paul", "paul@example.com", "0700000000", "secret");
        proprio.setId(2L);
        proprio.setNom("Bernard");
        proprio.setPrenom("Luc");
        proprio.setEmail("luc@example.com");
        proprio.setTelephone("0711111111");
        proprio.setPassword("hashed");

        assertEquals(2L, proprio.getId());
        assertEquals("Bernard", proprio.getNom());
        assertEquals("Luc", proprio.getPrenom());
        assertEquals("luc@example.com", proprio.getEmail());
        assertEquals("0711111111", proprio.getTelephone());
        assertEquals("hashed", proprio.getPassword());
    }

    @Test
    void propriete_constructorAndAccessors_work() {
        Proprio proprio = new Proprio();
        Locataire locataire = new Locataire();

        Propriete propriete = new Propriete(
                "12 avenue de la Gare",
                "Lyon",
                "France",
                52.0,
                "T2",
                700.0,
                50.0,
                3L,
                4L,
                36,
                12,
                "Refaite a neuf",
                "image.png"
        );

        propriete.setId(9L);
        propriete.setAdresse("14 avenue de la Gare");
        propriete.setVille("Paris");
        propriete.setPays("France");
        propriete.setSurfaceM2(60.0);
        propriete.setType("T3");
        propriete.setLoyer(900.0);
        propriete.setCharges(100.0);
        propriete.setIdProprio(8L);
        propriete.setProprio(proprio);
        propriete.setIdLocataire(10L);
        propriete.setLocataire(locataire);
        propriete.setDureeBail(24);
        propriete.setPeriodicite(1);
        propriete.setInfosComplementaires("Balcon");
        propriete.setImage("photo.jpg");

        assertEquals(9L, propriete.getId());
        assertEquals("14 avenue de la Gare", propriete.getAdresse());
        assertEquals("Paris", propriete.getVille());
        assertEquals("France", propriete.getPays());
        assertEquals(60.0, propriete.getSurfaceM2());
        assertEquals("T3", propriete.getType());
        assertEquals(900.0, propriete.getLoyer());
        assertEquals(100.0, propriete.getCharges());
        assertEquals(8L, propriete.getIdProprio());
        assertEquals(proprio, propriete.getProprio());
        assertEquals(10L, propriete.getIdLocataire());
        assertEquals(locataire, propriete.getLocataire());
        assertEquals(24, propriete.getDureeBail());
        assertEquals(1, propriete.getPeriodicite());
        assertEquals("Balcon", propriete.getInfosComplementaires());
        assertEquals("photo.jpg", propriete.getImage());
    }

    @Test
    void quittance_accessors_work() {
        Quittance quittance = new Quittance();
        Proprio proprio = new Proprio();
        Locataire locataire = new Locataire();
        Propriete propriete = new Propriete();

        quittance.setId(99L);
        quittance.setProprio(proprio);
        quittance.setLocataire(locataire);
        quittance.setPropriete(propriete);
        quittance.setPeriod("avril 2026");
        quittance.setPaymentDate("01/04/2026");
        quittance.setSignatureCity("Paris");
        quittance.setSignatureImage("base64data");
        quittance.setStatut("ENVOYEE");

        assertEquals(99L, quittance.getId());
        assertEquals(proprio, quittance.getProprio());
        assertEquals(locataire, quittance.getLocataire());
        assertEquals(propriete, quittance.getPropriete());
        assertEquals("avril 2026", quittance.getPeriod());
        assertEquals("01/04/2026", quittance.getPaymentDate());
        assertEquals("Paris", quittance.getSignatureCity());
        assertEquals("base64data", quittance.getSignatureImage());
        assertEquals("ENVOYEE", quittance.getStatut());
    }

    @Test
    void apiResponse_factoriesAndAccessors_work() {
        ApiResponse<String> success = ApiResponse.success("ok");
        ApiResponse<String> successDetails = ApiResponse.success("ok", "created");
        ApiResponse<String> info = ApiResponse.info("data", "hello");
        ApiResponse<String> error = ApiResponse.error("boom");
        ApiResponse<String> blankInfo = ApiResponse.info("data", "   ");

        assertEquals("ok", success.getData());
        assertEquals("[SUCCESS]", success.getState());

        assertEquals("[SUCCESS] created", successDetails.getState());
        assertEquals("[INFO] hello", info.getState());
        assertNull(error.getData());
        assertEquals("[ERROR] boom", error.getState());
        assertEquals("[INFO]", blankInfo.getState());

        success.setData("updated");
        success.setState("[INFO] updated");
        assertEquals("updated", success.getData());
        assertEquals("[INFO] updated", success.getState());
    }
}
