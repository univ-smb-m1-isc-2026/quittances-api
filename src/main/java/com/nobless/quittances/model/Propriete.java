package com.nobless.quittances.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "proprietes")
public class Propriete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 255)
    private String adresse;

    @Column(nullable = false)
    private Double surfaceM2;

    @Column(nullable = false, length = 2)
    private String type;

    @Column(nullable = false)
    private Double loyer;

    @Column(nullable = false)
    private Double charges;

    @Column(name = "id_proprio", nullable = false)
    private Long idProprio;

    @ManyToOne
    @JoinColumn(
        name = "id_proprio",
        referencedColumnName = "id",
        insertable = false,
        updatable = false,
        foreignKey = @ForeignKey(name = "fk_proprietes_proprio_id")
    )
    private Proprio proprio;

    @Column(name = "id_locataire", nullable = false)
    private Long idLocataire;

    @ManyToOne
    @JoinColumn(
        name = "id_locataire",
        referencedColumnName = "id",
        insertable = false,
        updatable = false,
        foreignKey = @ForeignKey(name = "fk_proprietes_locataire_email")
    )
    private Locataire locataire;

    @Column(nullable = false)
    private Integer dureeBail;

    @Column
    private Integer periodicite;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String infosComplementaires;

    public Propriete() {
    }

    public Propriete(
        String adresse,
        Double surfaceM2,
        String type,
        Double loyer,
        Double charges,
        Long idProprio,
        Long idLocataire,
        Integer dureeBail,
        Integer periodicite,
        String infosComplementaires
    ) {
        this.adresse = adresse;
        this.surfaceM2 = surfaceM2;
        this.type = type;
        this.loyer = loyer;
        this.charges = charges;
        this.idProprio = idProprio;
        this.idLocataire = idLocataire;
        this.dureeBail = dureeBail;
        this.periodicite = periodicite;
        this.infosComplementaires = infosComplementaires;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public Double getSurfaceM2() {
        return surfaceM2;
    }

    public void setSurfaceM2(Double surfaceM2) {
        this.surfaceM2 = surfaceM2;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getLoyer() {
        return loyer;
    }

    public void setLoyer(Double loyer) {
        this.loyer = loyer;
    }

    public Double getCharges() {
        return charges;
    }

    public void setCharges(Double charges) {
        this.charges = charges;
    }

    public Long getIdProprio() {
        return idProprio;
    }

    public void setIdProprio(Long idProprios) {
        this.idProprio = idProprios;
    }

    public Proprio getProprio() {
        return proprio;
    }

    public void setProprio(Proprio proprio) {
        this.proprio = proprio;
    }

    public Long getIdLocataire() {
        return idLocataire;
    }

    public void setIdLocataire(Long idLocataire) {
        this.idLocataire = idLocataire;
    }

    public Locataire getLocataire() {
        return locataire;
    }

    public void setLocataire(Locataire locataire) {
        this.locataire = locataire;
    }

    public Integer getDureeBail() {
        return dureeBail;
    }

    public void setDureeBail(Integer dureeBail) {
        this.dureeBail = dureeBail;
    }

    public Integer getPeriodicite() {
        return periodicite;
    }

    public void setPeriodicite(Integer periodicite) {
        this.periodicite = periodicite;
    }

    public String getInfosComplementaires() {
        return infosComplementaires;
    }

    public void setInfosComplementaires(String infosComplementaires) {
        this.infosComplementaires = infosComplementaires;
    }
}
