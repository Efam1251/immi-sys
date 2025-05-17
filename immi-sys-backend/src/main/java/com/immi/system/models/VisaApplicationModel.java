package com.immi.system.models;

import com.immi.system.enums.StatusEnum;
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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "visaapplication")
public class VisaApplicationModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")  // Explicitly set the column name for id
    private Long id;

    @Temporal(TemporalType.DATE)
    @Column(name = "date")  // Explicitly set the column name for date
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)  // Explicitly set the column name for customer_id
    private CustomerModel customer;

    @ManyToOne
    @JoinColumn(name = "visa_type_id", nullable = false)  // Explicitly set the column name for visa_type_id
    private VisaTypeModel visaType;

    @ManyToOne
    @JoinColumn(name = "country_id", nullable = false)  // Explicitly set the column name for country_id
    private CountryModel country;

    @Column(name = "reference", nullable = false, length = 25)  // Explicitly set the column name for reference
    private String reference;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "submission_date", nullable = true)  // Explicitly set the column name for submission_date
    private LocalDate submissionDate;
    
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column(name = "biometric_appointment", nullable = true)  // Explicitly set the column name for biometric_appointment
    private LocalDateTime biometricAppointment;
    
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column(name = "consular_appointment", nullable = true)  // Explicitly set the column name for consular_appointment
    private LocalDateTime consularAppointment;
    
    @Column(name = "cost", nullable = true)  // Explicitly set the column name for cost
    private BigDecimal cost;
    
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    @Column(name = "notes")
    @Lob
    private String notes;
    
    public VisaApplicationModel() {
    }

    public VisaApplicationModel(Long id, LocalDate date, CustomerModel customer, VisaTypeModel visaType, CountryModel country, String reference, LocalDate submissionDate, LocalDateTime biometricAppointment, LocalDateTime consularAppointment, BigDecimal cost, StatusEnum status, String notes) {
        this.id = id;
        this.date = date;
        this.customer = customer;
        this.visaType = visaType;
        this.country = country;
        this.reference = reference;
        this.submissionDate = submissionDate;
        this.biometricAppointment = biometricAppointment;
        this.consularAppointment = consularAppointment;
        this.cost = cost;
        this.status = status;
        this.notes = notes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public CustomerModel getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerModel customer) {
        this.customer = customer;
    }

    public VisaTypeModel getVisaType() {
        return visaType;
    }

    public void setVisaType(VisaTypeModel visaType) {
        this.visaType = visaType;
    }

    public CountryModel getCountry() {
        return country;
    }

    public void setCountry(CountryModel country) {
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

    public LocalDateTime getBiometricAppointment() {
        return biometricAppointment;
    }

    public void setBiometricAppointment(LocalDateTime biometricAppointment) {
        this.biometricAppointment = biometricAppointment;
    }

    public LocalDateTime getConsularAppointment() {
        return consularAppointment;
    }

    public void setConsularAppointment(LocalDateTime consularAppointment) {
        this.consularAppointment = consularAppointment;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    
    @Override
    public String toString() {
        return "VisaApplicationModel{" +
                "id=" + id +
                ", date=" + date +
                ", customer=" + (customer) +
                ", visaType=" + (visaType) +
                ", country=" + (country) +
                ", reference='" + reference +
                ", submissionDate=" + submissionDate +
                ", biometricAppointment=" + biometricAppointment +
                ", consularAppointment=" + consularAppointment +
                ", cost=" + cost +
                ", status=" + status +
                '}';
    }

}
