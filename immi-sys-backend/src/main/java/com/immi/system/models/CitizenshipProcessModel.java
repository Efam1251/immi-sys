package com.immi.system.models;

import com.immi.system.enums.CitizenshipStatusEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "citizenship_process")
public class CitizenshipProcessModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "citizenship_id")
    private Long citizenshipId;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    @NotNull(message = "Customer is required.")
    private CustomerModel customer;
    
    @Column(name = "receipt_number", length = 50, nullable = false)
    @NotEmpty(message = "Receipt number is required.")
    private String receiptNumber;

    @Column(name = "uscis_account_number", length = 50)
    private String uscisAccountNumber;

    @Column(name = "application_date", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Application date is required.")
    private LocalDate applicationDate;
    
    @Column(name = "application_payment", precision = 15, scale = 2)
    private BigDecimal applicationPayment;
    
    @Column(name = "biometric_appointment_date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime biometricAppointmentDate;
    
    @Column(name = "interview_date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime interviewDate;

    @Column(name = "oath_ceremony_date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime oathCeremonyDate;

    @Column(name = "location", length = 100)
    private String location;

    @Column(name = "current_status")
    @Enumerated(EnumType.STRING)
    private CitizenshipStatusEnum currentStatus;

    @Column(name = "citizenship_notes")
    @Lob
    private String citizenshipNotes;

    public CitizenshipProcessModel() {
    }

    public CitizenshipProcessModel(Long citizenshipId, CustomerModel customer, String receiptNumber, String uscisAccountNumber, LocalDate applicationDate, BigDecimal applicationPayment, LocalDateTime biometricAppointmentDate, LocalDateTime interviewDate, LocalDateTime oathCeremonyDate, String location, CitizenshipStatusEnum currentStatus, String citizenshipNotes) {
        this.citizenshipId = citizenshipId;
        this.customer = customer;
        this.receiptNumber = receiptNumber;
        this.uscisAccountNumber = uscisAccountNumber;
        this.applicationDate = applicationDate;
        this.applicationPayment = applicationPayment;
        this.biometricAppointmentDate = biometricAppointmentDate;
        this.interviewDate = interviewDate;
        this.oathCeremonyDate = oathCeremonyDate;
        this.location = location;
        this.currentStatus = currentStatus;
        this.citizenshipNotes = citizenshipNotes;
    }

    public Long getCitizenshipId() {
        return citizenshipId;
    }

    public void setCitizenshipId(Long citizenshipId) {
        this.citizenshipId = citizenshipId;
    }

    public CustomerModel getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerModel customer) {
        this.customer = customer;
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