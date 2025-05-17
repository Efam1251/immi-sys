package com.immi.system.controllers;

import com.immi.system.DTOs.SimpleDTO;
import com.immi.system.services.ImmigrationStatusService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/api/common/immigration-statuses")
public class ImmigrationStatusController {
    
    private static final Logger logger = LoggerFactory.getLogger(ImmigrationStatusController.class);

    @Autowired
    private ImmigrationStatusService immigrationStatusService;
    
    @GetMapping
    public ResponseEntity<?> getRequestedImmigrationStatus(
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
        SimpleDTO status = switch (direction) {
            case "Current" -> immigrationStatusService.getImmigrationStatusById(currentId);
            case "Next" -> immigrationStatusService.getNextImmigrationStatus(currentId);
            case "Previous" -> immigrationStatusService.getPreviousImmigrationStatus(currentId);
            case "First" -> immigrationStatusService.getFirstImmigrationStatus();
            case "Last" -> immigrationStatusService.getLastImmigrationStatus();
            default -> null;
        };
        if (status == null) {
            return ResponseEntity.ok(null);
        }
        return ResponseEntity.ok(status);
    }
    
    @GetMapping("/list")
    public ResponseEntity<List<SimpleDTO>> getAllImmigrationStatuses() {
        List<SimpleDTO> statuses = immigrationStatusService.getAllImmigrationStatuses();
        return new ResponseEntity<>(statuses, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SimpleDTO> getImmigrationStatusById(@PathVariable Long id) {
        SimpleDTO status = immigrationStatusService.getImmigrationStatusById(id);
        if (status == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(status, HttpStatus.OK);
    }
    
    @PostMapping
    public ResponseEntity<?> createImmigrationStatus(@RequestBody SimpleDTO dto) {
        logger.info("Creating a new immigration status: {}", dto);
        try {
            SimpleDTO createdStatus = immigrationStatusService.createImmigrationStatus(dto);
            return new ResponseEntity<>(createdStatus, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<SimpleDTO> updateImmigrationStatus(@PathVariable Long id, @RequestBody SimpleDTO dto) {
        SimpleDTO updatedStatus = immigrationStatusService.updateImmigrationStatus(id, dto);
        if (updatedStatus == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedStatus, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImmigrationStatus(@PathVariable Long id) {
        immigrationStatusService.deleteImmigrationStatus(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
