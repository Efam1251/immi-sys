package com.immi.system.controllers;

import com.immi.system.DTOs.TaxFilingDTO;
import com.immi.system.models.TaxFilingModel;
import com.immi.system.services.CustomerService;
import com.immi.system.services.TaxFilingService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tax/tax-filings")
public class TaxFilingController {
    
    private final TaxFilingService taxFilingService;

    public TaxFilingController(TaxFilingService taxFilingService, CustomerService customerService) {
        this.taxFilingService = taxFilingService;
    }
    
    // ===================== Tax Filing Fetching =====================

    @GetMapping("/{id}")
    public ResponseEntity<TaxFilingModel> getById(@PathVariable Long id) {
        TaxFilingModel record = taxFilingService.findById(id);
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

        TaxFilingModel record = switch (direction) {
            case "Current" -> taxFilingService.findById(currentId);
            case "Next" -> taxFilingService.findNext(currentId);
            case "Previous" -> taxFilingService.findPrevious(currentId);
            case "First" -> taxFilingService.findFirst();
            case "Last" -> taxFilingService.findLast();
            default -> taxFilingService.findById(currentId);
        };

        return ResponseEntity.ok(record);
    }
    
    @GetMapping("/list")
    public ResponseEntity<List<TaxFilingDTO>> getAllListRecords() {
        List<TaxFilingDTO> dtos = taxFilingService.getAllListRecords();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }
    
    @GetMapping("/all")
    public List<TaxFilingModel> getAllRecords() {
        return taxFilingService.findAll();
    }
    
    // ===================== Tax Filing Creation and Modification =====================
    
    @PostMapping
    public ResponseEntity<?> createRecord(@RequestBody TaxFilingModel record) {
        //logger.info("Creating a new Tax: {}", record);
        try {
            TaxFilingModel created = taxFilingService.createRecord(record);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<TaxFilingModel> updateRecord(@PathVariable Long id, @RequestBody TaxFilingModel record) {
        TaxFilingModel updated = taxFilingService.updateRecord(id, record);
        if (updated == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    // ===================== Tax Filing Deletion =====================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecord(@PathVariable Long id) {
        taxFilingService.deleteRecord(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
