package com.immi.system.DTOs;

import java.math.BigDecimal;
import java.time.LocalDate;

public class InvoiceProfitDTO {
    private Long invoiceNumber;
    private String customerName;
    private LocalDate invoiceDate;
    private BigDecimal income;
    private BigDecimal customerFees;
    private BigDecimal amountPaid;
    private BigDecimal openBalance;
    private BigDecimal profit;

    public Long getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(Long invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

    public BigDecimal getCustomerFees() {
        return customerFees;
    }

    public void setCustomerFees(BigDecimal customerFees) {
        this.customerFees = customerFees;
    }

    public BigDecimal getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(BigDecimal amountPaid) {
        this.amountPaid = amountPaid;
    }

    public BigDecimal getOpenBalance() {
        return openBalance;
    }

    public void setOpenBalance(BigDecimal openBalance) {
        this.openBalance = openBalance;
    }

    public BigDecimal getProfit() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }

}
