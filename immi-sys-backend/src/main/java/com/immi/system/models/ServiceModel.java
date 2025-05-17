package com.immi.system.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table(name = "service")
public class ServiceModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "serviceId")  // Explicitly set the column name for serviceId
    private Long serviceId;

    @NotNull(message = "Name is required")
    @Column(name = "serviceName", nullable = false)  // Explicitly set the column name for serviceName
    private String serviceName;

    @NotNull(message = "Description is required")
    @Column(name = "serviceDescription", nullable = false)  // Explicitly set the column name for serviceDescription
    private String serviceDescription;

    @NotNull(message = "Price is required")
    @Column(name = "unitPrice", nullable = false)  // Explicitly set the column name for unitPrice
    private BigDecimal unitPrice;

    @ManyToOne
    @JoinColumn(name = "service_type_id", nullable = true) // Setting the join column name
    private ServiceTypeModel serviceType; // References the ServiceType entity
    
    @NotNull(message = "Income service fee is required")
    @Column(name = "isIncomeService", nullable = true) // Added field to distinguish income vs fee services
    private String isIncomeService; // TRUE for income services, FALSE for fee payment services

    // Getters and Setters
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

    public ServiceTypeModel getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceTypeModel serviceType) {
        this.serviceType = serviceType;
    }

    public String getIsIncomeService() {
        return isIncomeService;
    }

    public void setIsIncomeService(String isIncomeService) {
        this.isIncomeService = isIncomeService;
    }
}
