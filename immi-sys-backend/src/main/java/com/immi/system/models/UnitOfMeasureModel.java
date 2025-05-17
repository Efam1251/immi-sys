package com.immi.system.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "unitofmeasure")
public class UnitOfMeasureModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull(message = "Unit name is required")
    @Size(max = 10, message = "Unit name cannot exceed 10 characters")
    @Column(name = "name", length = 10, nullable = false)
    private String name; // e.g., "pcs", "kg", "l"
    
    @NotNull(message = "Description is required")
    @Size(max = 50, message = "Description cannot exceed 50 characters")
    @Column(name = "description", length = 50, nullable = false, unique = true)
    private String description; // e.g., "Piece", "Kilogram", "Liter"

    public UnitOfMeasureModel() {
    }

    public UnitOfMeasureModel(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
   
}
