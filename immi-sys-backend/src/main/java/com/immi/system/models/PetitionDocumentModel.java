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
@Table(name = "petitiondocument")
public class PetitionDocumentModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")  // Explicitly set the column name for id
    private Long id;

    @Column(name = "petition_id")  // Explicitly set the column name for petitionId
    private Long petitionId;

    @ManyToOne
    @JoinColumn(name = "document_id", nullable = false)  // Explicitly set the column name for document
    private DocumentModel document; // Link to Document Model

    @Column(name = "file_name")  // Explicitly set the column name for fileName
    private String fileName;

    public PetitionDocumentModel() {
    }

    public PetitionDocumentModel(Long id, Long petitionId, DocumentModel document, String fileName) {
        this.id = id;
        this.petitionId = petitionId;
        this.document = document;
        this.fileName = fileName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPetitionId() {
        return petitionId;
    }

    public void setPetitionId(Long petitionId) {
        this.petitionId = petitionId;
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
