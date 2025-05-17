package com.immi.system.DTOs;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TaxFilingDTO {
    private Long taxFilingId;
    private String customerName;
    private Integer taxYear;
    private LocalDate filingDate;
    private BigDecimal totalIncome;
    private BigDecimal taxOutcomeAmount;
    private String status;

    public TaxFilingDTO() {
    }

    public TaxFilingDTO(Long taxFilingId, String customerName, Integer taxYear, LocalDate filingDate, BigDecimal totalIncome, BigDecimal taxOutcomeAmount, String status) {
        this.taxFilingId = taxFilingId;
        this.customerName = customerName;
        this.taxYear = taxYear;
        this.filingDate = filingDate;
        this.totalIncome = totalIncome;
        this.taxOutcomeAmount = taxOutcomeAmount;
        this.status = status;
    }

    public Long getTaxFilingId() {
        return taxFilingId;
    }

    public void setTaxFilingId(Long taxFilingId) {
        this.taxFilingId = taxFilingId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    
}
