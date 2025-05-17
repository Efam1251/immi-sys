package com.immi.system.DTOs;

public class AddressDTO {
    
    private Long id;
    private String street;
    private String city;
    private Long stateId;
    private String stateName;
    private String zipCode;
    private Long countryId;
    private String countryName;

    public AddressDTO() {
    }

    public AddressDTO(Long id, String street, String city, Long stateId, String stateName, String zipCode, Long countryId, String countryName) {
        this.id = id;
        this.street = street;
        this.city = city;
        this.stateId = stateId;
        this.stateName = stateName;
        this.zipCode = zipCode;
        this.countryId = countryId;
        this.countryName = countryName;
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

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
    
}
