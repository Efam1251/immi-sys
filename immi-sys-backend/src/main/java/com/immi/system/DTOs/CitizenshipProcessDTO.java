package com.immi.system.DTOs;

import com.immi.system.enums.CitizenshipStatusEnum;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class CitizenshipProcessDTO {

    private Long citizenshipId;
    private Long customerId;             // Reference to CustomerModel ID
    private String customerName;         // Optional: Full name (first + last)

    private String receiptNumber;
    private String uscisAccountNumber;

    private LocalDate applicationDate;
    private BigDecimal applicationPayment;

    private LocalDateTime biometricAppointmentDate;
    private LocalDateTime interviewDate;
    private LocalDateTime oathCeremonyDate;

    private String location;
    private CitizenshipStatusEnum currentStatus;

    private String citizenshipNotes;
    

    // ========== Getters and Setters ==========

    public Long getCitizenshipId() {
        return citizenshipId;
    }

    public void setCitizenshipId(Long citizenshipId) {
        this.citizenshipId = citizenshipId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public String getUscisAccountNumber() {
        return uscisAccountNumber;
    }

    public void setUscisAccountNumber(String uscisAccountNumber) {
        this.uscisAccountNumber = uscisAccountNumber;
    }

    public LocalDate getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(LocalDate applicationDate) {
        this.applicationDate = applicationDate;
    }

    public BigDecimal getApplicationPayment() {
        return applicationPayment;
    }

    public void setApplicationPayment(BigDecimal applicationPayment) {
        this.applicationPayment = applicationPayment;
    }

    public LocalDateTime getBiometricAppointmentDate() {
        return biometricAppointmentDate;
    }

    public void setBiometricAppointmentDate(LocalDateTime biometricAppointmentDate) {
        this.biometricAppointmentDate = biometricAppointmentDate;
    }

    public LocalDateTime getInterviewDate() {
        return interviewDate;
    }

    public void setInterviewDate(LocalDateTime interviewDate) {
        this.interviewDate = interviewDate;
    }

    public LocalDateTime getOathCeremonyDate() {
        return oathCeremonyDate;
    }

    public void setOathCeremonyDate(LocalDateTime oathCeremonyDate) {
        this.oathCeremonyDate = oathCeremonyDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public CitizenshipStatusEnum getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(CitizenshipStatusEnum currentStatus) {
        this.currentStatus = currentStatus;
    }

    public String getCitizenshipNotes() {
        return citizenshipNotes;
    }

    public void setCitizenshipNotes(String citizenshipNotes) {
        this.citizenshipNotes = citizenshipNotes;
    }
}
