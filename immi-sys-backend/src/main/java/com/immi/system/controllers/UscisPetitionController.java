package com.immi.system.controllers;

import com.immi.system.DTOs.PetitionDTO;
import com.immi.system.models.UscisPetitionModel;
import com.immi.system.services.ProcessEventService;
import com.immi.system.services.UscisPetitionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/immigration/uscis-petitions")
public class UscisPetitionController {

    private static final Logger logger = LoggerFactory.getLogger(UscisPetitionController.class);

    private final UscisPetitionService uscisPetitionService;
    private final ProcessEventService eventProcessService;

    public UscisPetitionController(UscisPetitionService uscisPetitionService,
            ProcessEventService eventProcessService) {
        this.uscisPetitionService = uscisPetitionService;
        this.eventProcessService = eventProcessService;
    }

    // ===================== Petition Fetching =====================
    @GetMapping("/{id}")
    public ResponseEntity<UscisPetitionModel> getById(@PathVariable Long id) {
        UscisPetitionModel record = uscisPetitionService.getPetition(id);
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

        UscisPetitionModel record = switch (direction) {
            case "Current" -> uscisPetitionService.getPetition(currentId);
            case "Next" -> uscisPetitionService.findNext(currentId);
            case "Previous" -> uscisPetitionService.findPrevious(currentId);
            case "First" -> uscisPetitionService.findFirst();
            case "Last" -> uscisPetitionService.findLast();
            default -> uscisPetitionService.getPetition(currentId);
        };

        return ResponseEntity.ok(record);
    }

    @GetMapping("/list")
    public ResponseEntity<List<PetitionDTO>> getAllPetitionsListRecords() {
        List<PetitionDTO> record = uscisPetitionService.getAllListRecords();
        return new ResponseEntity<>(record, HttpStatus.OK);
    }

    @GetMapping("/all")
    public List<UscisPetitionModel> getAllRecords() {
        return uscisPetitionService.findAll();
    }

    // ===================== Petition Creation & Update =====================
    @PostMapping
    public ResponseEntity<?> createRecord(@RequestBody UscisPetitionModel record) {
        logger.info("Creating new USCIS Petition: {}", record);
        try {
            UscisPetitionModel createdRecord = uscisPetitionService.createRecord(record);
            return new ResponseEntity<>(createdRecord, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UscisPetitionModel> updateRecord(@PathVariable Long id,
            @RequestBody UscisPetitionModel record) {
        UscisPetitionModel updatedRecord = uscisPetitionService.updateRecord(id, record);
        if (updatedRecord == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedRecord, HttpStatus.OK);
    }

    // ===================== Petition Deletion =====================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecord(@PathVariable Long id) {
        uscisPetitionService.deleteRecord(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    
}
