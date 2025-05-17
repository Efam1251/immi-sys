package com.immi.system.DTOs;

import com.immi.system.models.DocumentModel;

public class DocumentHandlingDTO {
    
    private Long id;
    private String recordType;
    private Long recordId;
    private DocumentModel document;
    private String fileName;

    public DocumentHandlingDTO() {
    }

    public DocumentHandlingDTO(Long id, String recordType, Long recordId, DocumentModel document, String fileName) {
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
