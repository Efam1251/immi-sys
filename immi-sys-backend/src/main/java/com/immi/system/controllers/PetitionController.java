package com.immi.system.controllers;

import com.immi.system.DTOs.DropDownDTO;
import com.immi.system.DTOs.PetitionDTO;
import com.immi.system.models.PetitionModel;
import com.immi.system.services.PetitionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/immigration/petitions")
public class PetitionController {
    
    private static final Logger logger = LoggerFactory.getLogger(StateController.class);

    private final PetitionService petitionService;

    public PetitionController(PetitionService petitionService) {
        this.petitionService = petitionService;
    }

    // ===================== Petition Fetching =====================
    
    @GetMapping("/{id}")
    public ResponseEntity<PetitionModel> getById(@PathVariable Long id) {
        PetitionModel record = petitionService.findById(id);
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

        PetitionModel record = switch (direction) {
            case "Current" -> petitionService.findById(currentId);
            case "Next" -> petitionService.findNext(currentId);
            case "Previous" -> petitionService.findPrevious(currentId);
            case "First" -> petitionService.findFirst();
            case "Last" -> petitionService.findLast();
            default -> petitionService.findById(currentId); // fallback to current if no direction provided
        };
        if (record == null) {
            return ResponseEntity.ok(null);
        }

        return ResponseEntity.ok(record);
    }

    @GetMapping("/list")
    public ResponseEntity<List<PetitionDTO>> getAllListRecords() {
        List<PetitionDTO> record = petitionService.getAllListRecords();
        return new ResponseEntity<>(record, HttpStatus.OK);
    }

    @GetMapping("/dropdown")
    public List<DropDownDTO> getDropDown() {
        return petitionService.findAll().stream()
            .map(record -> new DropDownDTO(record.getPetitionId(), "Petition " + record.getPetitionId()))
            .collect(Collectors.toList());
    }
    
    @GetMapping("/all")
    public List<PetitionModel> getAllRecords() {
        List<PetitionModel> records = petitionService.findAll();
        return records;
    }

    // ===================== Petition Creation and Modification =====================

    @PostMapping
    public ResponseEntity<?> createRecord(@RequestBody PetitionModel record) {
        logger.info("Creating a new Tax: {}", record);
        try {
            PetitionModel createdRecord = petitionService.createRecord(record);
            return new ResponseEntity<>(createdRecord, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<PetitionModel> updateRecord(@PathVariable Long id, @RequestBody PetitionModel record) {
        PetitionModel updatedRecord = petitionService.updateRecord(id, record);
        if (updatedRecord == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedRecord, HttpStatus.OK);
    }

    // ===================== Petition Deletion =====================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecord(@PathVariable Long id) {
        petitionService.deleteRecord(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
