package com.immi.system.DTOs;

import com.immi.system.models.PaymentModel;
import java.math.BigDecimal;

public class PaymentDTO {
    private Long id;
    private Long invoiceNumber;
    private BigDecimal amount;
    private String paymentDate;
    private String paymentMethodName;
    private String notes;

    public PaymentDTO(PaymentModel payment) {
        this.id = payment.getId();
        this.invoiceNumber = payment.getInvoiceNumber();
        this.amount = payment.getAmount();
        this.paymentDate = payment.getPaymentDate().toString(); // or format as needed
        this.notes = payment.getNotes();
        this.paymentMethodName = payment.getPaymentMethod().getName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(Long invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentMethodName() {
        return paymentMethodName;
    }

    public void setPaymentMethodName(String paymentMethodName) {
        this.paymentMethodName = paymentMethodName;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

}
