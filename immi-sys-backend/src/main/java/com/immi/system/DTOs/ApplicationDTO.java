package com.immi.system.DTOs;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ApplicationDTO {

    private Long id;
    private String customerName;
    private String formTypeName;
    private String receiptNumber;
    private String uscisAccountNumber;
    private LocalDate applicationDate;
    private BigDecimal applicationPayment;
    private String status; // Represented as String from StatusEnum
    private String notes;

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

    public String getFormTypeName() {
        return formTypeName;
    }

    public void setFormTypeName(String formTypeName) {
        this.formTypeName = formTypeName;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}