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
@Table(name="state")
public class StateModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")  // Explicitly set the column name for the id
    private Long id;
    
    @Column(nullable = false, name = "name")  // Explicitly set the column name for the name
    private String name;
    
    @Column(nullable = false, name = "code")  // Explicitly set the column name for the name
    private String code;
    
    @ManyToOne
    @JoinColumn(name = "country_id")
    private CountryModel country;
    
    public StateModel() {
    }
    
    public StateModel(Long id, String name, String code, CountryModel country) {
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
    
    public CountryModel getCountry() {
        return country;
    }
    
    public void setCountry(CountryModel country) {
        this.country = country;
    }
    
}
