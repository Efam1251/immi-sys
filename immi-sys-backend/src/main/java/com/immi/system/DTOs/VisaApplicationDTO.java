package com.immi.system.DTOs;

import java.time.LocalDate;

public class VisaApplicationDTO {
    
    private Long id;
    private String customerName;
    private String visaType;
    private String country;
    private String reference;
    private LocalDate submissionDate;
    private String status;
    private String decision;

    public VisaApplicationDTO() {
    }

    public VisaApplicationDTO(Long id, String customerName, String visaType, String country, String reference, LocalDate submissionDate, String status, String decision) {
        this.id = id;
        this.customerName = customerName;
        this.visaType = visaType;
        this.country = country;
        this.reference = reference;
        this.submissionDate = submissionDate;
        this.status = status;
        this.decision = decision;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getVisaType() {
        return visaType;
    }

    public void setVisaType(String visaType) {
        this.visaType = visaType;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public LocalDate getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(LocalDate submissionDate) {
        this.submissionDate = submissionDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

}
