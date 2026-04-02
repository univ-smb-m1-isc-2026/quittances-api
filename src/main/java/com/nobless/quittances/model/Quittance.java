package com.nobless.quittances.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "quittances")
public class Quittance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_proprio", nullable = false)
    private Long idProprio;

    @Embedded
    private LessorDetails lessor;

    @Embedded
    private TenantDetails tenant;

    @Column(name = "property_address")
    private String propertyAddress;

    @Column(name = "property_city")
    private String propertyCity;

    private Double rent;
    private Double charges;
    private String period;

    @Column(name = "payment_date")
    private String paymentDate;

    @Column(name = "signature_city")
    private String signatureCity;

    @Lob
    @Column(name = "signature_image", columnDefinition = "TEXT")
    private String signatureImage;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdProprio() {
        return idProprio;
    }

    public void setIdProprio(Long idProprio) {
        this.idProprio = idProprio;
    }

    public LessorDetails getLessor() {
        return lessor;
    }

    public void setLessor(LessorDetails lessor) {
        this.lessor = lessor;
    }

    public TenantDetails getTenant() {
        return tenant;
    }

    public void setTenant(TenantDetails tenant) {
        this.tenant = tenant;
    }

    public String getPropertyAddress() {
        return propertyAddress;
    }

    public void setPropertyAddress(String propertyAddress) {
        this.propertyAddress = propertyAddress;
    }

    public String getPropertyCity() {
        return propertyCity;
    }

    public void setPropertyCity(String propertyCity) {
        this.propertyCity = propertyCity;
    }

    public Double getRent() {
        return rent;
    }

    public void setRent(Double rent) {
        this.rent = rent;
    }

    public Double getCharges() {
        return charges;
    }

    public void setCharges(Double charges) {
        this.charges = charges;
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
