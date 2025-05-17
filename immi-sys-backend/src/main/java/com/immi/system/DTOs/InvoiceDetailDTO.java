package com.immi.system.DTOs;

import java.math.BigDecimal;

public class InvoiceDetailDTO {
    
    private Long idInvoiceDetail;
    private ServiceDTO service;
    private String reference;
    private BigDecimal quantity;
    private UnitOfMeasureDTO unitOfMeasure;
    private BigDecimal unitPrice;
    private BigDecimal discountAmount;
    private BigDecimal lineTotal;

    public InvoiceDetailDTO() {
    }

    public InvoiceDetailDTO(Long idInvoiceDetail, ServiceDTO service, String reference, BigDecimal quantity, UnitOfMeasureDTO unitOfMeasure, BigDecimal unitPrice, BigDecimal discountAmount, BigDecimal lineTotal) {
        this.idInvoiceDetail = idInvoiceDetail;
        this.service = service;
        this.reference = reference;
        this.quantity = quantity;
        this.unitOfMeasure = unitOfMeasure;
        this.unitPrice = unitPrice;
        this.discountAmount = discountAmount;
        this.lineTotal = lineTotal;
    }

    public Long getIdInvoiceDetail() {
        return idInvoiceDetail;
    }

    public void setIdInvoiceDetail(Long idInvoiceDetail) {
        this.idInvoiceDetail = idInvoiceDetail;
    }

    public ServiceDTO getService() {
        return service;
    }

    public void setService(ServiceDTO service) {
        this.service = service;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public UnitOfMeasureDTO getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(UnitOfMeasureDTO unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getLineTotal() {
        return lineTotal;
    }

    public void setLineTotal(BigDecimal lineTotal) {
        this.lineTotal = lineTotal;
    }
}
