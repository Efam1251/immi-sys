package com.immi.system.controllers;

import com.immi.system.DTOs.SimpleDTO;
import com.immi.system.services.VisaTypeService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/immigration/visaTypes")
public class VisaTypeController {

    private static final Logger logger = LoggerFactory.getLogger(VisaTypeController.class);

    private final VisaTypeService visaTypeService;

    public VisaTypeController(VisaTypeService visaTypeService) {
        this.visaTypeService = visaTypeService;
    }

    @GetMapping
    public ResponseEntity<?> getRequestedVisaType(
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

        SimpleDTO visaType = switch (direction) {
            case "Current" -> visaTypeService.getVisaTypeById(currentId);
            case "Next" -> visaTypeService.getNextVisaType(currentId);
            case "Previous" -> visaTypeService.getPreviousVisaType(currentId);
            case "First" -> visaTypeService.getFirstVisaType();
            case "Last" -> visaTypeService.getLastVisaType();
            default -> null;
        };

        return ResponseEntity.ok(visaType);
    }

    @GetMapping("/list")
    public ResponseEntity<List<SimpleDTO>> getAllVisaTypes() {
        List<SimpleDTO> visaTypes = visaTypeService.getAllVisaTypes();
        return new ResponseEntity<>(visaTypes, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SimpleDTO> getVisaTypeById(@PathVariable Long id) {
        SimpleDTO visaType = visaTypeService.getVisaTypeById(id);
        if (visaType == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(visaType, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createVisaType(@RequestBody SimpleDTO dto) {
        logger.info("Creating a new visa type: {}", dto);
        try {
            SimpleDTO createdVisaType = visaTypeService.createVisaType(dto);
            return new ResponseEntity<>(createdVisaType, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<SimpleDTO> updateVisaType(@PathVariable Long id, @RequestBody SimpleDTO dto) {
        SimpleDTO updatedVisaType = visaTypeService.updateVisaType(id, dto);
        if (updatedVisaType == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedVisaType, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVisaType(@PathVariable Long id) {
        visaTypeService.deleteVisaType(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
