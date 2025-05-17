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
@Table(name = "petitiondependent")
public class PetitionDependentModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")  // Explicitly set the column name for the id
    private Long id;

    @Column(name = "petition_id")  // Explicitly set the column name for petitionId
    private Long petitionId;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)  // Explicitly set the column name for dependentId
    private CustomerModel dependentId;

    @ManyToOne
    @JoinColumn(name = "dependentType_id", nullable = false)  // Explicitly set the column name for dependentTypeId
    private DependentTypeModel dependentTypeId;

    public PetitionDependentModel() {
    }

    public PetitionDependentModel(Long id, Long petitionId, CustomerModel dependentId, DependentTypeModel dependentTypeId) {
        this.id = id;
        this.petitionId = petitionId;
        this.dependentId = dependentId;
        this.dependentTypeId = dependentTypeId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPetitionId() {
        return petitionId;
    }

    public void setPetitionId(Long petitionId) {
        this.petitionId = petitionId;
    }

    public CustomerModel getDependentId() {
        return dependentId;
    }

    public void setDependentId(CustomerModel dependentId) {
        this.dependentId = dependentId;
    }

    public DependentTypeModel getDependentTypeId() {
        return dependentTypeId;
    }

    public void setDependentTypeId(DependentTypeModel dependentTypeId) {
        this.dependentTypeId = dependentTypeId;
    }

}
