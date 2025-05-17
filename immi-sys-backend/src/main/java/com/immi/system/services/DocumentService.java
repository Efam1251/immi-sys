package com.immi.system.services;

import com.immi.system.DTOs.DocumentHandlingDTO;
import com.immi.system.DTOs.DocumentParamsDTO;
import com.immi.system.DTOs.SimpleDTO;
import com.immi.system.models.DocumentHandlingModel;
import com.immi.system.models.DocumentModel;
import com.immi.system.repositories.DocumentRepository;
import com.immi.system.repositories.DocumentHandlingRepository;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class DocumentService {

    private final DocumentHandlingRepository documentHandlingRepository;
    private final DocumentRepository documentRepository;

    public DocumentService(DocumentHandlingRepository documentHandlingRepository,
                           DocumentRepository documentRepository) {
        this.documentHandlingRepository = documentHandlingRepository;
        this.documentRepository = documentRepository;
    }

    // ===============================
    // DOCUMENT TYPE MASTER FUNCTIONS
    // ===============================

    /**
     * Fetches all available document types from the document master table.
     * @return 
     */
    public List<DocumentModel> findAll() {
        return documentRepository.findAll();
    }
    
    public SimpleDTO createDocument(SimpleDTO dto) {
        if (documentRepository.findByName(dto.getName()) != null) {
            throw new RuntimeException("Document already exists");
        }
        
        DocumentModel document = new DocumentModel();
        document.setName(dto.getName());
        
        return mapToDTO(documentRepository.save(document));
    }
    
    private SimpleDTO mapToDTO(DocumentModel document) {
        SimpleDTO dto = new SimpleDTO();
        dto.setId(document.getId());
        dto.setName(document.getName());
        return dto;
    }

    // ===============================
    // DOCUMENT FILE UPLOAD & STORAGE
    // ===============================

    /**
     * Saves metadata and uploads the actual file for a document.
     * If a document with the same name and record already exists, it overwrites the file but not the DB record.
     * @param model
     * @return 
     * @throws java.io.IOException
     */
    @Transactional
    public DocumentHandlingModel saveDocumentHandling(DocumentParamsDTO model) throws IOException {
        // Input validation
        if (model == null || model.getType() == null || model.getFileName() == null ||
            model.getDocumentId() == null || model.getFile() == null) {
            throw new IllegalArgumentException("Invalid input data");
        }

        // Check for existing document
        DocumentHandlingModel existingDocument = documentHandlingRepository
                .findByRecordTypeAndRecordIdAndDocumentIdAndFileName(
                        model.getType(),
                        model.getRecordId(),
                        model.getDocumentId().getId(),
                        model.getFileName()
                );

        if (existingDocument != null) {
            copyFile(model); // Overwrite file only
            return existingDocument;
        }

        // Create new record
        DocumentHandlingModel documentHandling = new DocumentHandlingModel();
        documentHandling.setRecordType(model.getType());
        documentHandling.setRecordId(model.getRecordId());
        documentHandling.setDocument(model.getDocumentId());
        documentHandling.setFileName(model.getFileName());

        // First store the file, then save metadata
        copyFile(model);
        return documentHandlingRepository.save(documentHandling);
    }

    /**
     * Copies the uploaded file to the appropriate folder on disk.
     * @param model
     * @throws java.io.IOException
     */
    public void copyFile(DocumentParamsDTO model) throws IOException {
        Path saveDir = Paths.get("./documents", model.getType());

        if (Files.notExists(saveDir)) {
            Files.createDirectories(saveDir);
        }

        Path savePath = saveDir.resolve(model.getFileName());

        try (InputStream in = model.getFile().getInputStream()) {
            Files.copy(in, savePath, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    // ===============================
    // DOCUMENT RETRIEVAL & QUERY
    // ===============================

    /**
     * Fetches document metadata by type and file name (partial match).
     * @param type
     * @param reference
     * @return 
     */
    public List<DocumentHandlingDTO> fetchDocumentsByReference(String type, String reference) {
        List<DocumentHandlingModel> results =
                documentHandlingRepository.findByRecordTypeAndFileNameContaining(type, reference);

        return results.stream().map(doc -> {
            DocumentHandlingDTO dto = new DocumentHandlingDTO();
            dto.setId(doc.getId());
            dto.setRecordType(doc.getRecordType());
            dto.setRecordId(doc.getRecordId());
            dto.setDocument(doc.getDocument());
            dto.setFileName(doc.getFileName());
            return dto;
        }).collect(Collectors.toList());
    }

    // ===============================
    // DOCUMENT DELETION (SOFT DELETE)
    // ===============================

    /**
     * Deletes a document from the database and renames the physical file as archived (soft delete).
     * @param id
     */
    public void deleteDocumentById(Long id) {
        DocumentHandlingModel document = documentHandlingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found!"));

        renameDeletedFile(document.getFileName(), document.getRecordType());
        documentHandlingRepository.deleteById(id);
    }

    /**
     * Renames the document file with "-deleted" suffix to archive it instead of hard-deleting.
     * @param originalFileName
     * @param originFolder
     */
    public void renameDeletedFile(String originalFileName, String originFolder) {
        if (originalFileName == null || originalFileName.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid file name for document.");
        }

        Logger logger = Logger.getLogger(this.getClass().getName());

        String safeFolder = originFolder.replaceAll("[^a-zA-Z0-9_-]", "");
        Path baseDir = Paths.get("./documents").resolve(safeFolder).normalize();

        String newFileName = originalFileName.replaceFirst("(\\.[^.]+)$", "-deleted$1");
        Path sourcePath = baseDir.resolve(originalFileName);
        Path targetPath = baseDir.resolve(newFileName);

        try {
            if (Files.exists(sourcePath)) {
                Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                logger.info(String.format("[Audit] File renamed: %s ➝ %s",
                        sourcePath.toAbsolutePath(), targetPath.toAbsolutePath()));
            } else {
                logger.log(Level.WARNING, "[Audit] File not found for deletion: {0}", sourcePath.toAbsolutePath());
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "[Audit] Rename failed for: {0}, Error: {1}",
                    new Object[]{originalFileName, e.getMessage()});
            throw new RuntimeException("Failed to rename file: " + originalFileName, e);
        }

        logger.info(String.format("[Audit] Document record marked as deleted for: %s in folder: %s",
                originalFileName, safeFolder));
    }
}
