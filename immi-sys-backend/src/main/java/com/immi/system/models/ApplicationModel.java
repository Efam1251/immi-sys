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
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Table(name = "application")
public class ApplicationModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "process_id")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    @NotNull(message = "Customer is required.")
    private CustomerModel customer;

    @ManyToOne(optional = false)
    @JoinColumn(name = "form_type_id", nullable = false)
    @NotNull(message = "Form type is required.")
    private FormModel formType;

    @Column(name = "receipt_number", nullable = false, length = 50)
    @NotEmpty(message = "Receipt number is required.")
    private String receiptNumber;

    @Column(name = "uscis_account_number", nullable = true, length = 50)
    private String uscisAccountNumber;

    @Column(name = "application_date", nullable = false)
    @NotNull(message = "Application date is required.")
    private LocalDate applicationDate;

    @Column(name = "application_payment", precision = 10, scale = 2)
    private BigDecimal applicationPayment;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @NotNull(message = "Status is required.")
    private StatusEnum status; // e.g., PENDING, APPROVED, DENIED

    @Lob
    @Column(name = "notes", nullable = true)
    private String notes;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public ApplicationModel() {
    }

    public ApplicationModel(Long id, CustomerModel customer, FormModel formType, String receiptNumber, String uscisAccountNumber, LocalDate applicationDate, BigDecimal applicationPayment, StatusEnum status, String notes, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.customer = customer;
        this.formType = formType;
        this.receiptNumber = receiptNumber;
        this.uscisAccountNumber = uscisAccountNumber;
        this.applicationDate = applicationDate;
        this.applicationPayment = applicationPayment;
        this.status = status;
        this.notes = notes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CustomerModel getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerModel customer) {
        this.customer = customer;
    }

    public FormModel getFormType() {
        return formType;
    }

    public void setFormType(FormModel formType) {
        this.formType = formType;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "UscisProcessModel{" + "id=" + id + ", customer=" + customer + ", formType=" + formType + ", receiptNumber=" + receiptNumber + ", uscisAccountNumber=" + uscisAccountNumber + ", applicationDate=" + applicationDate + ", applicationPayment=" + applicationPayment + ", status=" + status + ", notes=" + notes + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + '}';
    }

}
