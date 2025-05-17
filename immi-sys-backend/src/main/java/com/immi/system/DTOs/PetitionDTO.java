package com.immi.system.DTOs;

import java.time.LocalDate;

public class PetitionDTO {
    private Long id;
    private String petitioner;
    private String beneficiary;
    private String uscisNumber;
    private String category;
    private LocalDate priorityDate;

    public PetitionDTO() {
    }

    public PetitionDTO(Long id, String petitioner, String beneficiary, String uscisNumber, String category, LocalDate priorityDate) {
        this.id = id;
        this.petitioner = petitioner;
        this.beneficiary = beneficiary;
        this.uscisNumber = uscisNumber;
        this.category = category;
        this.priorityDate = priorityDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPetitioner() {
        return petitioner;
    }

    public void setPetitioner(String petitioner) {
        this.petitioner = petitioner;
    }

    public String getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(String beneficiary) {
        this.beneficiary = beneficiary;
    }

    public String getUscisNumber() {
        return uscisNumber;
    }

    public void setUscisNumber(String uscisNumber) {
        this.uscisNumber = uscisNumber;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDate getPriorityDate() {
        return priorityDate;
    }

    public void setPriorityDate(LocalDate priorityDate) {
        this.priorityDate = priorityDate;
    }
    
}
