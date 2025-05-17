package com.immi.system.DTOs;

public class PetitionDependentDTO {
    
    private Long id;
    private Long petitionId;
    private Long dependentId;
    private String dependentName;
    private Long dependentTypeId;
    private String dependentTypeName;

    public PetitionDependentDTO() {
        
    }

    public PetitionDependentDTO(Long id, Long petitionId, Long dependentId, String dependentName, Long dependentTypeId, String dependentTypeName) {
        this.id = id;
        this.petitionId = petitionId;
        this.dependentId = dependentId;
        this.dependentName = dependentName;
        this.dependentTypeId = dependentTypeId;
        this.dependentTypeName = dependentTypeName;
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

    public Long getDependentId() {
        return dependentId;
    }

    public void setDependentId(Long dependentId) {
        this.dependentId = dependentId;
    }

    public String getDependentName() {
        return dependentName;
    }

    public void setDependentName(String dependentName) {
        this.dependentName = dependentName;
    }

    public Long getDependentTypeId() {
        return dependentTypeId;
    }

    public void setDependentTypeId(Long dependentTypeId) {
        this.dependentTypeId = dependentTypeId;
    }

    public String getDependentTypeName() {
        return dependentTypeName;
    }

    public void setDependentTypeName(String dependentTypeName) {
        this.dependentTypeName = dependentTypeName;
    }
}
