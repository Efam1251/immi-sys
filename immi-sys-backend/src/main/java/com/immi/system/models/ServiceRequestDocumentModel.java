package com.immi.system.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "service_request_document")
public class ServiceRequestDocumentModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "application_Id")
    private Long serviceRequestId;

    @ManyToOne
    @JoinColumn(name = "document_id", nullable = false)  // Explicitly set the column name for document
    private DocumentModel document; // Link to Document Model

    @Column(name = "file_name")  // Explicitly set the column name for fileName
    private String fileName;

    public ServiceRequestDocumentModel() {
    }

    public ServiceRequestDocumentModel(Long id, Long serviceRequestId, DocumentModel document, String fileName) {
        this.id = id;
        this.serviceRequestId = serviceRequestId;
        this.document = document;
        this.fileName = fileName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getServiceRequestId() {
        return serviceRequestId;
    }

    public void setServiceRequestId(Long serviceRequestId) {
        this.serviceRequestId = serviceRequestId;
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