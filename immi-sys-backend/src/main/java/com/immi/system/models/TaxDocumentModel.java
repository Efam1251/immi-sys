package com.immi.system.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "tax_filing_document")
public class TaxDocumentModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")  // Primary key
    private Long id;

    @Column(name = "tax_filing_id", nullable = false)
    private Long taxFilingId;  // FK reference (stored as raw ID, or can be replaced with @ManyToOne if preferred)

    @ManyToOne(optional = false)
    @JoinColumn(name = "document_id", nullable = false)  // FK to document type
    private DocumentModel document;

    @Column(name = "file_name", nullable = false, length = 255)
    @NotBlank(message = "File name is required.")
    @Size(max = 255, message = "File name cannot exceed 255 characters.")
    private String fileName;

    public TaxDocumentModel() {
    }

    public TaxDocumentModel(Long id, Long taxFilingId, DocumentModel document, String fileName) {
        this.id = id;
        this.taxFilingId = taxFilingId;
        this.document = document;
        this.fileName = fileName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTaxFilingId() {
        return taxFilingId;
    }

    public void setTaxFilingId(Long taxFilingId) {
        this.taxFilingId = taxFilingId;
    }

    public DocumentModel getDocument() {
        return document;
    }

    public void setDocument(DocumentModel document) {
        this.document = document;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

}
