package com.nobless.quittances.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class LessorDetails {

    @Column(name = "lessor_name")
    private String name;

    @Column(name = "lessor_address")
    private String address;

    @Column(name = "lessor_city")
    private String city;

    @Column(name = "lessor_phone")
    private String phone;

    @Column(name = "lessor_email")
    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
