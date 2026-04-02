package com.nobless.quittances.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "quittances")
public class Quittance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "lessor_id")
    private Proprio lessor;

    @ManyToOne
    @JoinColumn(name = "tenant_id")
    private Locataire tenant;

    @ManyToOne
    @JoinColumn(name = "propriete_id")
    private Propriete propriete;

    private String period;

    @Column(name = "payment_date")
    private String paymentDate;

    @Column(name = "signature_city")
    private String signatureCity;

    @Column(name = "signature_image", columnDefinition = "TEXT")
    private String signatureImage;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Proprio getLessor() {
        return lessor;
    }

    public void setLessor(Proprio lessor) {
        this.lessor = lessor;
    }

    public Locataire getTenant() {
        return tenant;
    }

    public void setTenant(Locataire tenant) {
        this.tenant = tenant;
    }

    public Propriete getPropriete() {
        return propriete;
    }

    public void setPropriete(Propriete propriete) {
        this.propriete = propriete;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getSignatureCity() {
        return signatureCity;
    }

    public void setSignatureCity(String signatureCity) {
        this.signatureCity = signatureCity;
    }

    public String getSignatureImage() {
        return signatureImage;
    }

    public void setSignatureImage(String signatureImage) {
        this.signatureImage = signatureImage;
    }
}
