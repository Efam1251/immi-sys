package com.immi.system.controllers;

import com.immi.system.DTOs.SimpleDTO;
import com.immi.system.services.PetitionStatusService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/immigration/petitionstatus")
public class PetitionStatusController {

    private static final Logger logger = LoggerFactory.getLogger(PetitionStatusController.class);

    private final PetitionStatusService petitionStatusService;

    public PetitionStatusController(PetitionStatusService petitionStatusService) {
        this.petitionStatusService = petitionStatusService;
    }

    @GetMapping
    public ResponseEntity<?> getRequestedPetitionStatus(
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
            case "Current" -> petitionStatusService.getPetitionStatusById(currentId);
            case "Next" -> petitionStatusService.getNextPetitionStatus(currentId);
            case "Previous" -> petitionStatusService.getPreviousPetitionStatus(currentId);
            case "First" -> petitionStatusService.getFirstPetitionStatus();
            case "Last" -> petitionStatusService.getLastPetitionStatus();
            default -> null;
        };
        if (status == null) {
            return ResponseEntity.ok(null);
        }
        return ResponseEntity.ok(status);
    }

    @GetMapping("/list")
    public ResponseEntity<List<SimpleDTO>> getAllPetitionStatuses() {
        List<SimpleDTO> statuses = petitionStatusService.getAllPetitionStatuses();
        return new ResponseEntity<>(statuses, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SimpleDTO> getPetitionStatusById(@PathVariable Long id) {
        SimpleDTO status = petitionStatusService.getPetitionStatusById(id);
        if (status == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createPetitionStatus(@RequestBody SimpleDTO dto) {
        logger.info("Creating a new petition status: {}", dto);
        try {
            SimpleDTO createdStatus = petitionStatusService.createPetitionStatus(dto);
            return new ResponseEntity<>(createdStatus, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<SimpleDTO> updatePetitionStatus(@PathVariable Long id, @RequestBody SimpleDTO dto) {
        SimpleDTO updatedStatus = petitionStatusService.updatePetitionStatus(id, dto);
        if (updatedStatus == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedStatus, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePetitionStatus(@PathVariable Long id) {
        petitionStatusService.deletePetitionStatus(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
