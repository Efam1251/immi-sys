package com.immi.system.controllers;

import com.immi.system.DTOs.DropDownDTO;
import com.immi.system.DTOs.ApplicationDTO;
import com.immi.system.models.ApplicationModel;
import com.immi.system.services.ApplicationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/immigration/application")
public class ApplicationController {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationController.class);

    private final ApplicationService uscisProcessService;

    public ApplicationController(ApplicationService uscisProcessService) {
        this.uscisProcessService = uscisProcessService;
    }

    // ===================== USCIS Process Fetching =====================
    @GetMapping("/{id}")
    public ResponseEntity<ApplicationModel> getById(@PathVariable Long id) {
        ApplicationModel record = uscisProcessService.getProcess(id);
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

        ApplicationModel record = switch (direction) {
            case "Current" ->
                uscisProcessService.getProcess(currentId);
            case "Next" ->
                uscisProcessService.findNext(currentId);
            case "Previous" ->
                uscisProcessService.findPrevious(currentId);
            case "First" ->
                uscisProcessService.findFirst();
            case "Last" ->
                uscisProcessService.findLast();
            default ->
                uscisProcessService.getProcess(currentId);
        };

        return ResponseEntity.ok(record);
    }

    @GetMapping("/list")
    public ResponseEntity<List<ApplicationDTO>> getAllListRecords() {
        List<ApplicationDTO> records = uscisProcessService.getAllListRecords();
        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    @GetMapping("/dropdown")
    public List<DropDownDTO> getDropDown() {
        return uscisProcessService.findAll().stream()
                .map(p -> new DropDownDTO(p.getId(), "Process " + p.getReceiptNumber()))
                .collect(Collectors.toList());
    }

    @GetMapping("/all")
    public List<ApplicationModel> getAllRecords() {
        return uscisProcessService.findAll();
    }

    // ===================== USCIS Process Creation & Update =====================
    @PostMapping
    public ResponseEntity<?> createRecord(@RequestBody ApplicationModel record) {
        logger.info("Creating new USCIS Process: {}", record);
        try {
            ApplicationModel createdRecord = uscisProcessService.createRecord(record);
            return new ResponseEntity<>(createdRecord, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApplicationModel> updateRecord(@PathVariable Long id,
            @RequestBody ApplicationModel record) {
        ApplicationModel updatedRecord = uscisProcessService.updateRecord(id, record);
        if (updatedRecord == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedRecord, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecord(@PathVariable Long id) {
        uscisProcessService.deleteRecord(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
