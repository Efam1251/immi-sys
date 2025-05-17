package com.immi.system.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="address")
public class AddressModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(nullable = false, name = "street")
    private String street;
    
    @Column(nullable = false, name = "city")
    private String city;
    
    @ManyToOne
    @JoinColumn(name = "state_id", nullable = true) // Setting the join column name
    private StateModel state; // References the Gender entity.
    
    @Column(nullable = false, name = "zipCode")
    private String zipCode;
    
    @ManyToOne
    @JoinColumn(name = "country_id", nullable = false) // Setting the join column name
    private CountryModel country; // References the Gender entity.

    public AddressModel() {
    }

    public AddressModel(Long id, String street, String city, StateModel state, String zipCode, CountryModel country) {
        this.id = id;
        this.street = street;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.country = country;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public StateModel getState() {
        return state;
    }

    public void setState(StateModel state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public CountryModel getCountry() {
        return country;
    }

    public void setCountry(CountryModel country) {
        this.country = country;
    }

}
