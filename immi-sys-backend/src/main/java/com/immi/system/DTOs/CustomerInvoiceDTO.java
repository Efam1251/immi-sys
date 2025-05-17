package com.immi.system.DTOs;

public class CustomerInvoiceDTO {
    
    private Long id;
    private String name;
    private String billingAddress;
    private String customerEmail;
    private String customerPhone;

    public CustomerInvoiceDTO() {
    }

    public CustomerInvoiceDTO(Long id, String name, String billingAddress, String customerEmail, String customerPhone) {
        this.id = id;
        this.name = name;
        this.billingAddress = billingAddress;
        this.customerEmail = customerEmail;
        this.customerPhone = customerPhone;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }
    
}
