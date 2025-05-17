package com.immi.system.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "payment")
public class PaymentModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")  // Explicitly set the column name for the id
    private Long id;

    @Column(nullable = false, name = "invoice_number")  // Explicitly set the column name for the name
    private Long invoiceNumber;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "payment_date", nullable = false)
    private LocalDate paymentDate;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_method_id", nullable = false)
    private PaymentMethodModel paymentMethod;
    
    @Column(name = "amount", nullable = false)
    private BigDecimal amount;
    
    @Column(name = "notes", length = 500)
    private String notes;
    
    @Column(name = "cancelled", nullable = false)
    private boolean cancelled = false;

    public PaymentModel() {
    }

    public PaymentModel(Long id, Long invoiceNumber, LocalDate paymentDate, PaymentMethodModel paymentMethod, BigDecimal amount, String notes, boolean cancelled) {
        this.id = id;
        this.invoiceNumber = invoiceNumber;
        this.paymentDate = paymentDate;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.notes = notes;
        this.cancelled = cancelled;
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

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public PaymentMethodModel getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethodModel paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
    
    @Override
    public String toString() {
        return "PaymentModel{" +
               "id=" + id +
               ", invoiceNumber=" + invoiceNumber +
               ", paymentDate=" + (paymentDate != null ? paymentDate.toString() : "N/A") +
               ", paymentMethod=" + (paymentMethod != null ? paymentMethod.getName() : "N/A") +
               ", amount=" + amount +
               ", notes=" + (notes != null ? "'" + notes + "'" : "N/A") +
               ", cancelled=" + cancelled +
               '}';
    }

}
