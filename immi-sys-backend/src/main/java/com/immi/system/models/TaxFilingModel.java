package com.immi.system.models;

import com.immi.system.enums.FilingStatusEnum;
import com.immi.system.enums.StatusEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "tax_filing")
public class TaxFilingModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tax_filing_id")
    private Long taxFilingId;

    @NotNull(message = "Date is required.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @NotNull(message = "Customer is required.")
    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private CustomerModel customer;

    @NotNull(message = "Tax year is required.")
    @Column(name = "tax_year", nullable = false)
    private Integer taxYear;

    @NotNull(message = "Filing date is required.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "filing_date", nullable = false)
    private LocalDate filingDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "filing_status", length = 30)
    private FilingStatusEnum filingStatus;

    @Min(value = 0, message = "Number of dependents must be 0 or more.")
    @Column(name = "dependents")
    private Integer numberOfDependents;

    @NotNull(message = "Total income is required.")
    @DecimalMin(value = "0.0", inclusive = true, message = "Total income must be positive.")
    @Column(name = "income", precision = 12, scale = 2, nullable = false)
    private BigDecimal totalIncome;

    @DecimalMin(value = "0.0", inclusive = true, message = "Tax outcome must be positive.")
    @Column(name = "tax_outcome_amount", precision = 12, scale = 2)
    private BigDecimal taxOutcomeAmount; // Could be negative for "owed", document that in comments/UI

    @Column(name = "prepared_by", length = 100)
    private String preparedBy;

    @NotNull(message = "Status is required.")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private StatusEnum status;

    @Size(max = 500, message = "Notes cannot exceed 500 characters.")
    @Column(name = "notes", length = 500)
    private String notes;
    
    public TaxFilingModel() {
    }

    public TaxFilingModel(Long taxFilingId, LocalDate date, CustomerModel customer, Integer taxYear, LocalDate filingDate, FilingStatusEnum filingStatus, Integer numberOfDependents, BigDecimal totalIncome, BigDecimal taxOutcomeAmount, String preparedBy, StatusEnum status, String notes) {
        this.taxFilingId = taxFilingId;
        this.date = date;
        this.customer = customer;
        this.taxYear = taxYear;
        this.filingDate = filingDate;
        this.filingStatus = filingStatus;
        this.numberOfDependents = numberOfDependents;
        this.totalIncome = totalIncome;
        this.taxOutcomeAmount = taxOutcomeAmount;
        this.preparedBy = preparedBy;
        this.status = status;
        this.notes = notes;
    }

    public Long getTaxFilingId() {
        return taxFilingId;
    }

    public void setTaxFilingId(Long taxFilingId) {
        this.taxFilingId = taxFilingId;
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

    public Integer getTaxYear() {
        return taxYear;
    }

    public void setTaxYear(Integer taxYear) {
        this.taxYear = taxYear;
    }

    public LocalDate getFilingDate() {
        return filingDate;
    }

    public void setFilingDate(LocalDate filingDate) {
        this.filingDate = filingDate;
    }

    public FilingStatusEnum getFilingStatus() {
        return filingStatus;
    }

    public void setFilingStatus(FilingStatusEnum filingStatus) {
        this.filingStatus = filingStatus;
    }

    public Integer getNumberOfDependents() {
        return numberOfDependents;
    }

    public void setNumberOfDependents(Integer numberOfDependents) {
        this.numberOfDependents = numberOfDependents;
    }

    public BigDecimal getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(BigDecimal totalIncome) {
        this.totalIncome = totalIncome;
    }

    public BigDecimal getTaxOutcomeAmount() {
        return taxOutcomeAmount;
    }

    public void setTaxOutcomeAmount(BigDecimal taxOutcomeAmount) {
        this.taxOutcomeAmount = taxOutcomeAmount;
    }

    public String getPreparedBy() {
        return preparedBy;
    }

    public void setPreparedBy(String preparedBy) {
        this.preparedBy = preparedBy;
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
        return "TaxFilingModel{" +
                "taxFilingId=" + taxFilingId +
                ", date=" + date +
                ", customer=" + (customer) +
                ", taxYear=" + taxYear +
                ", filingDate=" + filingDate +
                ", filingStatus=" + filingStatus +
                ", numberOfDependents=" + numberOfDependents +
                ", totalIncome=" + totalIncome +
                ", taxOutcomeAmount=" + taxOutcomeAmount +
                ", preparedBy='" + preparedBy + '\'' +
                ", status=" + status +
                ", notes='" + notes + '\'' +
                '}';
    }
    
}
