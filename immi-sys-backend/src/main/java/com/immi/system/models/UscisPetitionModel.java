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
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "uscis_petition")
public class UscisPetitionModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "petition_id")
    private Long petitionId;

    @NotNull(message = "Process date is required.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "process_date", nullable = false)
    private LocalDate processDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private CustomerModel client;

    @ManyToOne(optional = false)
    @JoinColumn(name = "petitioner_id", nullable = false)
    private CustomerModel petitioner;

    @ManyToOne(optional = false)
    @JoinColumn(name = "beneficiary_id", nullable = false)
    private CustomerModel beneficiary;

    @NotNull(message = "Priority date is required.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "priority_date", nullable = false)
    private LocalDate priorityDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryModel category;

    @NotEmpty(message = "USCIS number is required.")
    @Column(name = "uscis_number", length = 50, nullable = false)
    private String uscisNumber;

    @ManyToOne
    @JoinColumn(name = "uscis_office_id")
    private UscisOfficeModel uscisOffice;
    
    @Column(name = "uscis_payment", precision = 10, scale = 2)
    private BigDecimal uscisPayment;

    @Enumerated(EnumType.STRING)
    @Column(name = "petition_status")
    private StatusEnum petitionStatus;

    @Lob
    @Column(name = "petition_notes")
    private String petitionNotes;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public UscisPetitionModel() {
    }

    public UscisPetitionModel(Long petitionId, LocalDate processDate, CustomerModel client, CustomerModel petitioner, CustomerModel beneficiary, LocalDate priorityDate, CategoryModel category, String uscisNumber, UscisOfficeModel uscisOffice, BigDecimal uscisPayment, StatusEnum petitionStatus, String petitionNotes, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.petitionId = petitionId;
        this.processDate = processDate;
        this.client = client;
        this.petitioner = petitioner;
        this.beneficiary = beneficiary;
        this.priorityDate = priorityDate;
        this.category = category;
        this.uscisNumber = uscisNumber;
        this.uscisOffice = uscisOffice;
        this.uscisPayment = uscisPayment;
        this.petitionStatus = petitionStatus;
        this.petitionNotes = petitionNotes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getPetitionId() {
        return petitionId;
    }

    public void setPetitionId(Long petitionId) {
        this.petitionId = petitionId;
    }

    public LocalDate getProcessDate() {
        return processDate;
    }

    public void setProcessDate(LocalDate processDate) {
        this.processDate = processDate;
    }

    public CustomerModel getClient() {
        return client;
    }

    public void setClient(CustomerModel client) {
        this.client = client;
    }

    public CustomerModel getPetitioner() {
        return petitioner;
    }

    public void setPetitioner(CustomerModel petitioner) {
        this.petitioner = petitioner;
    }

    public CustomerModel getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(CustomerModel beneficiary) {
        this.beneficiary = beneficiary;
    }

    public LocalDate getPriorityDate() {
        return priorityDate;
    }

    public void setPriorityDate(LocalDate priorityDate) {
        this.priorityDate = priorityDate;
    }

    public CategoryModel getCategory() {
        return category;
    }

    public void setCategory(CategoryModel category) {
        this.category = category;
    }

    public String getUscisNumber() {
        return uscisNumber;
    }

    public void setUscisNumber(String uscisNumber) {
        this.uscisNumber = uscisNumber;
    }

    public UscisOfficeModel getUscisOffice() {
        return uscisOffice;
    }

    public void setUscisOffice(UscisOfficeModel uscisOffice) {
        this.uscisOffice = uscisOffice;
    }

    public BigDecimal getUscisPayment() {
        return uscisPayment;
    }

    public void setUscisPayment(BigDecimal uscisPayment) {
        this.uscisPayment = uscisPayment;
    }

    public StatusEnum getPetitionStatus() {
        return petitionStatus;
    }

    public void setPetitionStatus(StatusEnum petitionStatus) {
        this.petitionStatus = petitionStatus;
    }

    public String getPetitionNotes() {
        return petitionNotes;
    }

    public void setPetitionNotes(String petitionNotes) {
        this.petitionNotes = petitionNotes;
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
        return "UscisPetitionModel{" + "petitionId=" + petitionId + ", processDate=" + processDate + ", client=" + client + ", petitioner=" + petitioner + ", beneficiary=" + beneficiary + ", priorityDate=" + priorityDate + ", category=" + category + ", uscisNumber=" + uscisNumber + ", uscisOffice=" + uscisOffice + ", uscisPayment=" + uscisPayment + ", petitionStatus=" + petitionStatus + ", petitionNotes=" + petitionNotes + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + '}';
    }
    
}
