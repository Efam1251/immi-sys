package com.immi.system.models;

import com.immi.system.enums.StatusEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "petition")
public class PetitionModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "petition_id")  // Explicitly setting column name
    private Long petitionId;

    @Column(name = "process_date", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Process date is required.")
    private LocalDate processDate;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    @NotNull(message = "Client is required.")
    private CustomerModel client;

    @ManyToOne
    @JoinColumn(name = "petitioner_id", nullable = false)
    @NotNull(message = "Petitioner is required.")
    private CustomerModel petitioner;

    @ManyToOne
    @JoinColumn(name = "beneficiary_id", nullable = false)
    @NotNull(message = "Beneficiary is required.")
    private CustomerModel beneficiary;

    @Column(name = "priority_date", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Priority date is required.")
    private LocalDate priorityDate;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    @NotNull(message = "Category is required.")
    private CategoryModel category;

    @Column(name = "uscis_number", length = 50, nullable = false)
    @NotEmpty(message = "USCIS number is required.")
    private String uscisNumber;

    @ManyToOne
    @JoinColumn(name = "uscis_office_id")
    private UscisOfficeModel uscisOffice;

    @Column(name = "uscis_payment", precision = 15, scale = 2)
    private BigDecimal uscisPayment;

    @Column(name = "uscis_payment_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate uscisPaymentDate;

    @Column(name = "uscis_approval_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate uscisApprovalDate;

    @Column(name = "nvc_number", length = 50)
    private String nvcNumber;

    @Column(name = "invoice_number", length = 50)
    private String invoiceNumber;

    @Column(name = "nvc_payment", precision = 15, scale = 2)
    private BigDecimal nvcPayment;

    @Column(name = "nvc_payment_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate nvcPaymentDate;

    @Column(name = "ds260_form")
    private Boolean ds260Form = false;

    @Column(name = "petitioner_affidavit")
    private Boolean petitionerAffidavit = false;

    @Column(name = "sponsor_affidavit")
    private Boolean sponsorAffidavit = false;

    @Column(name = "nvc_approval_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate nvcApprovalDate;

    @Column(name = "consular_appointment_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate consularAppointmentDate;

    @Column(name = "vaccine_appointment_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate vaccineAppointmentDate;

    @Column(name = "petition_status")
    @Enumerated(EnumType.STRING)
    private StatusEnum petitionStatus;
    
    @Column(name = "petition_notes")
    @Lob
    private String petitionNotes;

    public PetitionModel() {
    }

    public PetitionModel(Long petitionId, LocalDate processDate, CustomerModel client, CustomerModel petitioner, CustomerModel beneficiary, LocalDate priorityDate, CategoryModel category, String uscisNumber, UscisOfficeModel uscisOffice, BigDecimal uscisPayment, LocalDate uscisPaymentDate, LocalDate uscisApprovalDate, String nvcNumber, String invoiceNumber, BigDecimal nvcPayment, LocalDate nvcPaymentDate, LocalDate nvcApprovalDate, LocalDate consularAppointmentDate, LocalDate vaccineAppointmentDate, StatusEnum petitionStatus, String petitionNotes) {
        this.petitionId = petitionId;
        this.processDate = processDate;
        this.client = client;
        this.petitioner = petitioner;
        this.beneficiary = beneficiary;
        this.priorityDate = priorityDate;
        this.category = category;
        this.uscisNumber = uscisNumber;
        this.uscisOffice = uscisOffice;
        this.uscisPayment = uscisPayment;
        this.uscisPaymentDate = uscisPaymentDate;
        this.uscisApprovalDate = uscisApprovalDate;
        this.nvcNumber = nvcNumber;
        this.invoiceNumber = invoiceNumber;
        this.nvcPayment = nvcPayment;
        this.nvcPaymentDate = nvcPaymentDate;
        this.nvcApprovalDate = nvcApprovalDate;
        this.consularAppointmentDate = consularAppointmentDate;
        this.vaccineAppointmentDate = vaccineAppointmentDate;
        this.petitionStatus = petitionStatus;
        this.petitionNotes = petitionNotes;
    }

    public Long getPetitionId() {
        return petitionId;
    }

    public void setPetitionId(Long petitionId) {
        this.petitionId = petitionId;
    }

    public LocalDate getProcessDate() {
        return processDate;
    }

    public void setProcessDate(LocalDate processDate) {
        this.processDate = processDate;
    }

    public CustomerModel getClient() {
        return client;
    }

    public void setClient(CustomerModel client) {
        this.client = client;
    }

    public CustomerModel getPetitioner() {
        return petitioner;
    }

    public void setPetitioner(CustomerModel petitioner) {
        this.petitioner = petitioner;
    }

    public CustomerModel getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(CustomerModel beneficiary) {
        this.beneficiary = beneficiary;
    }

    public LocalDate getPriorityDate() {
        return priorityDate;
    }

    public void setPriorityDate(LocalDate priorityDate) {
        this.priorityDate = priorityDate;
    }

    public CategoryModel getCategory() {
        return category;
    }

    public void setCategory(CategoryModel category) {
        this.category = category;
    }

    public String getUscisNumber() {
        return uscisNumber;
    }

    public void setUscisNumber(String uscisNumber) {
        this.uscisNumber = uscisNumber;
    }

    public UscisOfficeModel getUscisOffice() {
        return uscisOffice;
    }

    public void setUscisOffice(UscisOfficeModel uscisOffice) {
        this.uscisOffice = uscisOffice;
    }

    public BigDecimal getUscisPayment() {
        return uscisPayment;
    }

    public void setUscisPayment(BigDecimal uscisPayment) {
        this.uscisPayment = uscisPayment;
    }

    public LocalDate getUscisPaymentDate() {
        return uscisPaymentDate;
    }

    public void setUscisPaymentDate(LocalDate uscisPaymentDate) {
        this.uscisPaymentDate = uscisPaymentDate;
    }

    public LocalDate getUscisApprovalDate() {
        return uscisApprovalDate;
    }

    public void setUscisApprovalDate(LocalDate uscisApprovalDate) {
        this.uscisApprovalDate = uscisApprovalDate;
    }

    public String getNvcNumber() {
        return nvcNumber;
    }

    public void setNvcNumber(String nvcNumber) {
        this.nvcNumber = nvcNumber;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public BigDecimal getNvcPayment() {
        return nvcPayment;
    }

    public void setNvcPayment(BigDecimal nvcPayment) {
        this.nvcPayment = nvcPayment;
    }

    public LocalDate getNvcPaymentDate() {
        return nvcPaymentDate;
    }

    public void setNvcPaymentDate(LocalDate nvcPaymentDate) {
        this.nvcPaymentDate = nvcPaymentDate;
    }

    public Boolean getDs260Form() {
        return ds260Form;
    }

    public void setDs260Form(Boolean ds260Form) {
        this.ds260Form = ds260Form;
    }

    public Boolean getPetitionerAffidavit() {
        return petitionerAffidavit;
    }

    public void setPetitionerAffidavit(Boolean petitionerAffidavit) {
        this.petitionerAffidavit = petitionerAffidavit;
    }

    public Boolean getSponsorAffidavit() {
        return sponsorAffidavit;
    }

    public void setSponsorAffidavit(Boolean sponsorAffidavit) {
        this.sponsorAffidavit = sponsorAffidavit;
    }

    public LocalDate getNvcApprovalDate() {
        return nvcApprovalDate;
    }

    public void setNvcApprovalDate(LocalDate nvcApprovalDate) {
        this.nvcApprovalDate = nvcApprovalDate;
    }

    public LocalDate getConsularAppointmentDate() {
        return consularAppointmentDate;
    }

    public void setConsularAppointmentDate(LocalDate consularAppointmentDate) {
        this.consularAppointmentDate = consularAppointmentDate;
    }

    public LocalDate getVaccineAppointmentDate() {
        return vaccineAppointmentDate;
    }

    public void setVaccineAppointmentDate(LocalDate vaccineAppointmentDate) {
        this.vaccineAppointmentDate = vaccineAppointmentDate;
    }

    public StatusEnum getPetitionStatus() {
        return petitionStatus;
    }

    public void setPetitionStatus(StatusEnum petitionStatus) {
        this.petitionStatus = petitionStatus;
    }

    public String getPetitionNotes() {
        return petitionNotes;
    }

    public void setPetitionNotes(String petitionNotes) {
        this.petitionNotes = petitionNotes;
    }

    @Override
    public String toString() {
        return "PetitionModel{" +
                "petitionId=" + petitionId +
                ", processDate=" + processDate +
                ", client=" + client +
                ", petitioner=" + petitioner +
                ", beneficiary=" + beneficiary +
                ", priorityDate=" + priorityDate +
                ", category=" + category +
                ", uscisNumber='" + uscisNumber + '\'' +
                ", uscisOffice=" + uscisOffice +
                ", uscisPayment=" + uscisPayment +
                ", uscisPaymentDate=" + uscisPaymentDate +
                ", uscisApprovalDate=" + uscisApprovalDate +
                ", nvcNumber='" + nvcNumber + '\'' +
                ", invoiceNumber='" + invoiceNumber + '\'' +
                ", nvcPayment=" + nvcPayment +
                ", nvcPaymentDate=" + nvcPaymentDate +
                ", ds260Form=" + ds260Form +
                ", petitionerAffidavit=" + petitionerAffidavit +
                ", sponsorAffidavit=" + sponsorAffidavit +
                ", nvcApprovalDate=" + nvcApprovalDate +
                ", consularAppointmentDate=" + consularAppointmentDate +
                ", vaccineAppointmentDate=" + vaccineAppointmentDate +
                ", petitionStatus=" + petitionStatus +
                ", petitionNotes='" + petitionNotes + '\'' +
                '}';
    }

}
