package com.immi.system.controllers;

import com.immi.system.DTOs.DocumentHandlingDTO;
import com.immi.system.DTOs.DocumentParamsDTO;
import com.immi.system.DTOs.DropDownDTO;
import com.immi.system.DTOs.SimpleDTO;
import com.immi.system.models.DocumentHandlingModel;
import com.immi.system.services.DocumentService;
import jakarta.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing immigration document records and file storage.
 */
@RestController
@RequestMapping("/api/immigration/document")
public class DocumentController {
    private static final Logger logger = LoggerFactory.getLogger(CountryController.class);

    // ─────────────────────────────────────────────
    // 🧱 1. Dependency Injection: Service Constructor
    // ─────────────────────────────────────────────
    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    // ─────────────────────────────────────────────
    // 📥 2. GET Requests
    // ─────────────────────────────────────────────

    /**
     * Get all available document types for dropdown lists.
     * @return 
     */
    @GetMapping("/documents")
    public List<DropDownDTO> getAllDocuments() {
        return documentService.findAll().stream()
                .map(document -> new DropDownDTO(document.getId(), document.getName()))
                .collect(Collectors.toList());
    }

    /**
     * Get list of uploaded document files based on record type and reference.
     * @param type
     * @param reference
     * @return 
     */
    @GetMapping("/document-list")
    public ResponseEntity<List<DocumentHandlingDTO>> getDocumentsListByReference(
            @RequestParam("type") String type,
            @RequestParam("reference") String reference) {

        List<DocumentHandlingDTO> listDocuments = documentService.fetchDocumentsByReference(type, reference);
        return ResponseEntity.ok(listDocuments);
    }

    /**
     * Open and stream a document file by its file name.
     * @param fileName
     * @return 
     */
    @GetMapping("/openDocument")
    public ResponseEntity<Resource> openDocument(@RequestParam("fileName") String fileName) {
        Path documentsDir = Paths.get("documents");

        try {
            Path filePath = Files.walk(documentsDir)
                    .filter(path -> path.getFileName().toString().equals(fileName))
                    .findFirst()
                    .orElse(null);

            if (filePath == null) {
                return ResponseEntity.notFound().build();
            }

            Resource resource = new FileSystemResource(filePath.toFile());
            String contentType = Files.probeContentType(filePath);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .contentType(MediaType.parseMediaType(contentType != null ? contentType : "application/octet-stream"))
                    .body(resource);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ─────────────────────────────────────────────
    // 📤 3. POST Requests
    // ─────────────────────────────────────────────

    @PostMapping("/save-Document")
    public ResponseEntity<?> createDocument(@RequestBody SimpleDTO dto) {
        logger.info("Creating a new gender: {}", dto);
        try {
            SimpleDTO createdCountry = documentService.createDocument(dto);
            return new ResponseEntity<>(createdCountry, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }
    /**
     * Save a document record and upload the associated file.
     * @param params
     * @return 
     */
    @PostMapping("/document-save-upload")
    public ResponseEntity<?> saveAndUploadDocument(@Valid DocumentParamsDTO params) {
        if (params == null || params.getType() == null || params.getFileName() == null
                || params.getDocumentId() == null || params.getFile() == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Missing required parameters or file."));
        }

        try {
            DocumentHandlingModel documentSaved = documentService.saveDocumentHandling(params);

            if (documentSaved == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Unsupported document type: " + params.getType()));
            }

            return ResponseEntity.status(HttpStatus.CREATED).build();

        } catch (IOException | SecurityException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Upload failed: " + e.getMessage()));
        }
    }

    // ─────────────────────────────────────────────
    // ❌ 4. DELETE Requests
    // ─────────────────────────────────────────────

    /**
     * Delete a document by ID (marks the file as deleted and removes the DB record).
     * @param id
     * @return 
     */
    @DeleteMapping("/document-delete/{id}")
    public ResponseEntity<Map<String, String>> deleteDocumentByType(@PathVariable Long id) {
        Map<String, String> response = new HashMap<>();

        try {
            documentService.deleteDocumentById(id);
            response.put("message", "Document deleted successfully!");
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);

        } catch (Exception e) {
            response.put("message", "Failed to delete document.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
