package com.immi.system.controllers;

import com.immi.system.DTOs.SimpleDTO;
import com.immi.system.services.MaritalStatusService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/common/marital-statuses")
public class MaritalStatusController {
    
    private static final Logger logger = LoggerFactory.getLogger(MaritalStatusController.class);

    private final MaritalStatusService maritalStatusService;
    
    public MaritalStatusController(MaritalStatusService maritalStatusService) {
        this.maritalStatusService = maritalStatusService;
    }
    
    @GetMapping
    public ResponseEntity<?> getRequestedMaritalStatus(
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
        SimpleDTO maritalStatus = switch (direction) {
            case "Current" -> maritalStatusService.getMaritalStatusById(currentId);
            case "Next" -> maritalStatusService.getNextMaritalStatus(currentId);
            case "Previous" -> maritalStatusService.getPreviousMaritalStatus(currentId);
            case "First" -> maritalStatusService.getFirstMaritalStatus();
            case "Last" -> maritalStatusService.getLastMaritalStatus();
            default -> null;
        };
        if (maritalStatus == null) {
            return ResponseEntity.ok(null);
        }
        return ResponseEntity.ok(maritalStatus);
    }
    
    @GetMapping("/list")
    public ResponseEntity<List<SimpleDTO>> getAllMaritalStatuses() {
        List<SimpleDTO> maritalStatuses = maritalStatusService.getAllMaritalStatuses();
        return new ResponseEntity<>(maritalStatuses, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SimpleDTO> getMaritalStatusById(@PathVariable Long id) {
        SimpleDTO maritalStatus = maritalStatusService.getMaritalStatusById(id);
        if (maritalStatus == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(maritalStatus, HttpStatus.OK);
    }
    
    @PostMapping
    public ResponseEntity<?> createMaritalStatus(@RequestBody SimpleDTO dto) {
        logger.info("Creating a new marital status: {}", dto);
        try {
            SimpleDTO createdMaritalStatus = maritalStatusService.createMaritalStatus(dto);
            return new ResponseEntity<>(createdMaritalStatus, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<SimpleDTO> updateMaritalStatus(@PathVariable Long id, @RequestBody SimpleDTO dto) {
        SimpleDTO updatedMaritalStatus = maritalStatusService.updateMaritalStatus(id, dto);
        if (updatedMaritalStatus == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedMaritalStatus, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaritalStatus(@PathVariable Long id) {
        maritalStatusService.deleteMaritalStatus(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
