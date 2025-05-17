package com.immi.system.DTOs;

import com.immi.system.enums.StatusEnum;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ServiceRequestDTO {
    private Long id;
    private Long serviceId;
    private String serviceName;
    private Long customerId;
    private String customerFullName;
    private LocalDate requestDate;
    private String notes;
    private StatusEnum status;
    private String createdBy;
    private String referenceCode;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ServiceRequestDTO() {
    }

    public ServiceRequestDTO(Long id, Long serviceId, String serviceName,
                             Long customerId, String customerFullName,
                             LocalDate requestDate, String notes,
                             StatusEnum status, String createdBy,
                             String referenceCode,
                             LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.customerId = customerId;
        this.customerFullName = customerFullName;
        this.requestDate = requestDate;
        this.notes = notes;
        this.status = status;
        this.createdBy = createdBy;
        this.referenceCode = referenceCode;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerFullName() {
        return customerFullName;
    }

    public void setCustomerFullName(String customerFullName) {
        this.customerFullName = customerFullName;
    }

    public LocalDate getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDate requestDate) {
        this.requestDate = requestDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getReferenceCode() {
        return referenceCode;
    }

    public void setReferenceCode(String referenceCode) {
        this.referenceCode = referenceCode;
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

}
