package com.immi.system.controllers;

import com.immi.system.DTOs.DropDownDTO;
import com.immi.system.DTOs.FormDTO;
import com.immi.system.models.FormModel;
import com.immi.system.services.FormService;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@RequestMapping("/api/common/forms")
public class FormController {

    private static final Logger logger = LoggerFactory.getLogger(FormController.class);

    private final FormService formService;

    public FormController(FormService formService) {
        this.formService = formService;
    }

    // ===================== Form Fetching =====================
    @GetMapping("/{id}")
    public ResponseEntity<FormModel> getById(@PathVariable Long id) {
        FormModel record = formService.findById(id);
        if (record == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(record, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getRequested(
            @RequestParam("id") String id,
            @RequestParam(required = false) String direction) {

        if (id == null || id.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("message", "ID parameter is required"));
        }
        Long currentId;
        try {
            currentId = Long.valueOf(id);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid ID format"));
        }

        FormModel record = switch (direction) {
            case "Current" -> formService.findById(currentId);
            case "Next" -> formService.findNext(currentId);
            case "Previous" -> formService.findPrevious(currentId);
            case "First" -> formService.findFirst();
            case "Last" -> formService.findLast();
            default -> formService.findById(currentId); // fallback to current if no direction provided
        };

        if (record == null) {
            return ResponseEntity.ok(null);
        }

        return ResponseEntity.ok(record);
    }

    @GetMapping("/list")
    public ResponseEntity<List<FormDTO>> getAllListRecords() {
        List<FormDTO> recordDtos = formService.getAllListRecords();
        return new ResponseEntity<>(recordDtos, HttpStatus.OK);
    }

    @GetMapping("/dropdown")
    public List<DropDownDTO> getDropDown() {
        return formService.findAll().stream()
            .map(record -> new DropDownDTO(record.getId(), record.getName()))
            .collect(Collectors.toList());
    }

    @GetMapping("/all")
    public List<FormModel> getAllRecords() {
        return formService.findAll();
    }

    // ===================== Form Creation and Modification =====================
    // --- 1. Create (POST) ---
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createRecordWithFile(
        @RequestPart("record") FormModel record,
        @RequestPart(value = "file", required = false) MultipartFile file
    ) throws IOException {
        return handleSave(record, file);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createRecordWithoutFile(
        @RequestBody FormModel record
    ) throws IOException {
        return handleSave(record, null);
    }

    // --- 2. Update (PUT) ---
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateRecordWithFile(
        @PathVariable Long id,
        @RequestPart("record") FormModel record,
        @RequestPart(value = "file", required = false) MultipartFile file
    ) throws IOException {
        record.setId(id);  // ensure the ID is set
        return handleSave(record, file);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateRecordWithoutFile(
        @PathVariable Long id,
        @RequestBody FormModel record
    ) throws IOException {
        record.setId(id);
        return handleSave(record, null);
    }

    // --- Shared method ---
    private ResponseEntity<?> handleSave(FormModel record, MultipartFile file) throws IOException {
        try {
            logger.info("Received record: {}", record);
            if (file != null) {
                logger.info("Received file: name={}, originalFilename={}, size={} bytes",
                            file.getName(), file.getOriginalFilename(), file.getSize());
            } else {
                logger.info("No file received.");
            }
            
            System.out.println("Received record: " + record);
            if (file != null) {
                System.out.println("File name: " + file.getName());
                System.out.println("Original filename: " + file.getOriginalFilename());
                System.out.println("File size: " + file.getSize());
            } else {
                System.out.println("No file received");
            }


            
            
            FormModel savedRecord = formService.saveRecord(record, file);
            return new ResponseEntity<>(savedRecord,
                (record.getId() == null || record.getId() <= 0)
                    ? HttpStatus.CREATED
                    : HttpStatus.OK);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    // ===================== Form Deletion =====================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecord(@PathVariable Long id) {
        formService.deleteRecord(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}