package com.immi.system.DTOs;

public class SimpleDTO {
    
    private Long id;
    private String name;

    public SimpleDTO() {
    }

    public SimpleDTO(Long id, String name) {
        this.id = id;
        this.name = name;
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

}
