package com.immi.system.DTOs;

import java.math.BigDecimal;
import java.time.LocalDate;

public class InvoiceListDTO {

    private Long invoiceId;
    private Long invoiceNumber;
    private String customerFullName;  // Combination of firstName and lastName
    private LocalDate invoiceDate;
    private BigDecimal totalAmount;
    private String status;  // Converted from InvoiceStatusEnum to String
    private BigDecimal openBalance;

    public InvoiceListDTO() {
    }

    public InvoiceListDTO(Long invoiceId, Long invoiceNumber, String customerFullName, LocalDate invoiceDate, BigDecimal totalAmount, String status, BigDecimal openBalance) {
        this.invoiceId = invoiceId;
        this.invoiceNumber = invoiceNumber;
        this.customerFullName = customerFullName;
        this.invoiceDate = invoiceDate;
        this.totalAmount = totalAmount;
        this.status = status;
        this.openBalance = openBalance;
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Long getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(Long invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getCustomerFullName() {
        return customerFullName;
    }

    public void setCustomerFullName(String customerFullName) {
        this.customerFullName = customerFullName;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getOpenBalance() {
        return openBalance;
    }

    public void setOpenBalance(BigDecimal openBalance) {
        this.openBalance = openBalance;
    }

}
