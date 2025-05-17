package com.immi.system.DTOs;

import java.math.BigDecimal;

public class ServiceDTO {
    
    private Long serviceId;
    private String serviceName;
    private String serviceDescription;
    private BigDecimal unitPrice;
    private DropDownDTO serviceType;

    public ServiceDTO() {
    }

    public ServiceDTO(Long serviceId, String serviceName, String serviceDescription, BigDecimal unitPrice, DropDownDTO serviceType) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.serviceDescription = serviceDescription;
        this.unitPrice = unitPrice;
        this.serviceType = serviceType;
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

    public String getServiceDescription() {
        return serviceDescription;
    }

    public void setServiceDescription(String serviceDescription) {
        this.serviceDescription = serviceDescription;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public DropDownDTO getServiceType() {
        return serviceType;
    }

    public void setServiceType(DropDownDTO serviceType) {
        this.serviceType = serviceType;
    }

    
    @Override
    public String toString() {
        return "ServiceDTO{" +
                "id=" + serviceId +
                ", name='" + serviceName + '\'' +
                ", description='" + serviceDescription + '\'' +
                ", unitPrice=" + unitPrice +
                ", serviceType=" + (serviceType != null ? serviceType.toString() : "null") +
                '}';
    }

}
