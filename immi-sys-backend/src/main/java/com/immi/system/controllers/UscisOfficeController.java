package com.immi.system.controllers;

import com.immi.system.DTOs.SimpleDTO;
import com.immi.system.services.UscisOfficeService;
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
@RequestMapping("/api/immigration/uscisoffices")
public class UscisOfficeController {

    private static final Logger logger = LoggerFactory.getLogger(UscisOfficeController.class);

    private final UscisOfficeService uscisOfficeService;

    public UscisOfficeController(UscisOfficeService uscisOfficeService) {
        this.uscisOfficeService = uscisOfficeService;
    }

    @GetMapping
    public ResponseEntity<?> getRequestedUscisOffice(
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
        SimpleDTO office = switch (direction) {
            case "Current" -> uscisOfficeService.getUscisOfficeById(currentId);
            case "Next" -> uscisOfficeService.getNextUscisOffice(currentId);
            case "Previous" -> uscisOfficeService.getPreviousUscisOffice(currentId);
            case "First" -> uscisOfficeService.getFirstUscisOffice();
            case "Last" -> uscisOfficeService.getLastUscisOffice();
            default -> null;
        };
        if (office == null) {
            return ResponseEntity.ok(null);
        }
        return ResponseEntity.ok(office);
    }

    @GetMapping("/list")
    public ResponseEntity<List<SimpleDTO>> getAllUscisOffices() {
        List<SimpleDTO> offices = uscisOfficeService.getAllUscisOffices();
        return new ResponseEntity<>(offices, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SimpleDTO> getUscisOfficeById(@PathVariable Long id) {
        SimpleDTO office = uscisOfficeService.getUscisOfficeById(id);
        if (office == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(office, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createUscisOffice(@RequestBody SimpleDTO dto) {
        logger.info("Creating a new USCIS office: {}", dto);
        try {
            SimpleDTO createdOffice = uscisOfficeService.createUscisOffice(dto);
            return new ResponseEntity<>(createdOffice, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<SimpleDTO> updateUscisOffice(@PathVariable Long id, @RequestBody SimpleDTO dto) {
        SimpleDTO updatedOffice = uscisOfficeService.updateUscisOffice(id, dto);
        if (updatedOffice == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedOffice, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUscisOffice(@PathVariable Long id) {
        uscisOfficeService.deleteUscisOffice(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
