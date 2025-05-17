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
@Table(name = "document_handling")
public class DocumentHandlingModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "record_type")  // Explicitly set the column name for fileName
    private String recordType;

    @Column(name = "record_Id")
    private Long recordId;

    @ManyToOne
    @JoinColumn(name = "document_id", nullable = false)  // Explicitly set the column name for document
    private DocumentModel document; // Link to Document Model

    @Column(name = "file_name")  // Explicitly set the column name for fileName
    private String fileName;

    public DocumentHandlingModel() {
    }

    public DocumentHandlingModel(Long id, String recordType, Long recordId, DocumentModel document, String fileName) {
        this.id = id;
        this.recordType = recordType;
        this.recordId = recordId;
        this.document = document;
        this.fileName = fileName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
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
