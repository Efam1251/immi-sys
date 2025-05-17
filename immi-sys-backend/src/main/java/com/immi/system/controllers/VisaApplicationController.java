package com.immi.system.controllers;

import com.immi.system.DTOs.DropDownDTO;
import com.immi.system.DTOs.VisaApplicationDTO;
import com.immi.system.models.VisaApplicationModel;
import com.immi.system.services.VisaApplicationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/immigration/visaApplications")
public class VisaApplicationController {

    private final VisaApplicationService visaApplicationService;

    public VisaApplicationController(VisaApplicationService visaApplicationService) {
        this.visaApplicationService = visaApplicationService;
    }

    // ===================== Visa Application Fetching =====================
    
    @GetMapping("/{id}")
    public ResponseEntity<VisaApplicationModel> getById(@PathVariable Long id) {
        VisaApplicationModel record = visaApplicationService.findById(id);
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

        VisaApplicationModel record = switch (direction) {
            case "Current" -> visaApplicationService.findById(currentId);
            case "Next" -> visaApplicationService.findNext(currentId);
            case "Previous" -> visaApplicationService.findPrevious(currentId);
            case "First" -> visaApplicationService.findFirst();
            case "Last" -> visaApplicationService.findLast();
            default -> visaApplicationService.findById(currentId); // fallback to current if no direction provided
        };

        if (record == null) {
            return ResponseEntity.ok(null);
        }

        return ResponseEntity.ok(record);
    }

    @GetMapping("/list")
    public ResponseEntity<List<VisaApplicationDTO>> getAllListRecords() {
        List<VisaApplicationDTO> recordDtos = visaApplicationService.getAllListRecords();
        return new ResponseEntity<>(recordDtos, HttpStatus.OK);
    }

    @GetMapping("/dropdown")
    public List<DropDownDTO> getDropDown() {
        return visaApplicationService.findAll().stream()
            .map(record -> new DropDownDTO(record.getId(), record.getReference()))
            .collect(Collectors.toList());
    }

    @GetMapping("/all")
    public List<VisaApplicationModel> getAllRecords() {
        List<VisaApplicationModel> records = visaApplicationService.findAll();
        return records;
    }

    // ===================== Visa Application Creation and Modification =====================

    @PostMapping
    public ResponseEntity<?> createRecord(@RequestBody VisaApplicationModel record) {
        try {            
            VisaApplicationModel createdRecord = visaApplicationService.createRecord(record);
            return new ResponseEntity<>(createdRecord, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<VisaApplicationModel> updateRecord(@PathVariable Long id, @RequestBody VisaApplicationModel record) {
        VisaApplicationModel updatedRecord = visaApplicationService.updateRecord(id, record);
        if (updatedRecord == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedRecord, HttpStatus.OK);
    }

    // ===================== Visa Application Deletion =====================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecord(@PathVariable Long id) {
        visaApplicationService.deleteRecord(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
