package com.immi.system.DTOs;

public class StateDTO {
    
    private Long id;
    private String name;
    private String code;
    private DropDownDTO country;

    public StateDTO() {
    }

    public StateDTO(Long id, String name, String code, DropDownDTO country) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.country = country;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public DropDownDTO getCountry() {
        return country;
    }

    public void setCountry(DropDownDTO country) {
        this.country = country;
    }

}
