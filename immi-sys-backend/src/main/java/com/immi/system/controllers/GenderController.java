package com.immi.system.controllers;

import com.immi.system.DTOs.SimpleDTO;
import com.immi.system.services.GenderService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
@RequestMapping("/api/common/genders")
public class GenderController {
    
    private static final Logger logger = LoggerFactory.getLogger(CountryController.class);
            
    private final GenderService genderService;

    public GenderController(GenderService genderService) {
        this.genderService = genderService;
    }
    
    @GetMapping
    public ResponseEntity<?> getRequestedGender(
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
        SimpleDTO country = switch (direction) {
            case "Current" -> genderService.getGenderById(currentId);
            case "Next" -> genderService.getNextGender(currentId);
            case "Previous" -> genderService.getPreviousGender(currentId);
            case "First" -> genderService.getFirstGender();
            case "Last" -> genderService.getLastGender();
            default -> null;
        };
        if (country == null) {
            return ResponseEntity.ok(null);
        }
        return ResponseEntity.ok(country);
    }
    @GetMapping("/list")
    public ResponseEntity<List<SimpleDTO>> getAllGenders() {
        List<SimpleDTO> genders = genderService.getAllGenders();
        return new ResponseEntity<>(genders, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SimpleDTO> getGenderById(@PathVariable Long id) {
        SimpleDTO country = genderService.getGenderById(id);
        if (country == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(country, HttpStatus.OK);
    }
    
    @PostMapping
    public ResponseEntity<?> createGender(@RequestBody SimpleDTO dto) {
        logger.info("Creating a new gender: {}", dto);
        try {
            SimpleDTO createdCountry = genderService.createGender(dto);
            return new ResponseEntity<>(createdCountry, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<SimpleDTO> updateGender(@PathVariable Long id, @RequestBody SimpleDTO dto) {
        SimpleDTO updatedGender = genderService.updateGender(id, dto);
        if (updatedGender == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedGender, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGender(@PathVariable Long id) {
        genderService.deleteGender(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
