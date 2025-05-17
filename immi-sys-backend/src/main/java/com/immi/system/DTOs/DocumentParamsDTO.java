package com.immi.system.DTOs;

import com.immi.system.models.DocumentModel;
import org.springframework.web.multipart.MultipartFile;


public class DocumentParamsDTO {
    private String type;
    private Long recordId;
    private DocumentModel documentId;
    private String fileName;
    private MultipartFile file;
    
    public DocumentParamsDTO() {
    }

    public DocumentParamsDTO(String type, Long recordId, DocumentModel documentId, String fileName, MultipartFile file) {
        this.type = type;
        this.recordId = recordId;
        this.documentId = documentId;
        this.fileName = fileName;
        this.file = file;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public DocumentModel getDocumentId() {
        return documentId;
    }

    public void setDocumentId(DocumentModel documentId) {
        this.documentId = documentId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

}
