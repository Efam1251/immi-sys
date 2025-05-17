package com.immi.system.controllers;

import com.immi.system.DTOs.CitizenshipProcessDTO;
import com.immi.system.DTOs.DropDownDTO;
import com.immi.system.models.CitizenshipProcessModel;
import com.immi.system.services.CitizenshipProcessService;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping("/api/immigration/citizenship")
public class CitizenshipProcessController {

    private static final Logger logger = LoggerFactory.getLogger(CitizenshipProcessController.class);

    private final CitizenshipProcessService citizenshipService;

    public CitizenshipProcessController(CitizenshipProcessService citizenshipService) {
        this.citizenshipService = citizenshipService;
    }

    // ===================== Fetching =====================

    @GetMapping("/{id}")
    public ResponseEntity<CitizenshipProcessModel> getById(@PathVariable Long id) {
        CitizenshipProcessModel record = citizenshipService.findById(id);
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

        CitizenshipProcessModel record = switch (direction) {
            case "Current" -> citizenshipService.findById(currentId);
            case "Next" -> citizenshipService.findNext(currentId);
            case "Previous" -> citizenshipService.findPrevious(currentId);
            case "First" -> citizenshipService.findFirst();
            case "Last" -> citizenshipService.findLast();
            default -> citizenshipService.findById(currentId);
        };

        return ResponseEntity.ok(record);
    }

    @GetMapping("/list")
    public ResponseEntity<List<CitizenshipProcessDTO>> getAllListRecords() {
        List<CitizenshipProcessDTO> records = citizenshipService.getAllListRecords();
        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    @GetMapping("/dropdown")
    public List<DropDownDTO> getDropDown() {
        return citizenshipService.findAll().stream()
            .map(record -> new DropDownDTO(record.getCitizenshipId(), "Citizenship #" + record.getCitizenshipId()))
            .collect(Collectors.toList());
    }

    @GetMapping("/all")
    public List<CitizenshipProcessModel> getAllRecords() {
        return citizenshipService.findAll();
    }

    // ===================== Creation & Update =====================

    @PostMapping
    public ResponseEntity<?> createRecord(@RequestBody CitizenshipProcessModel record) {
        logger.info("Creating Citizenship Record: {}", record);
        try {
            CitizenshipProcessModel created = citizenshipService.createRecord(record);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CitizenshipProcessModel> updateRecord(
            @PathVariable Long id,
            @RequestBody CitizenshipProcessModel record) {
        CitizenshipProcessModel updated = citizenshipService.updateRecord(id, record);
        if (updated == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    // ===================== Deletion =====================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecord(@PathVariable Long id) {
        citizenshipService.deleteRecord(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
