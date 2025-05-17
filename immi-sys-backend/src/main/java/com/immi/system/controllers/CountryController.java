package com.immi.system.controllers;

import com.immi.system.DTOs.SimpleDTO;
import com.immi.system.services.CountryService;
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
@RequestMapping("/api/common/location/countries")
public class CountryController {

    private static final Logger logger = LoggerFactory.getLogger(CountryController.class);

    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }
    
    @GetMapping
    public ResponseEntity<?> getRequestedCountry(
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
            case "Current" -> countryService.getCountryById(currentId);
            case "Next" -> countryService.getNextCountry(currentId);
            case "Previous" -> countryService.getPreviousCountry(currentId);
            case "First" -> countryService.getFirstCountry();
            case "Last" -> countryService.getLastCountry();
            default -> null;
        };
        if (country == null) {
            return ResponseEntity.ok(null);
        }
        return ResponseEntity.ok(country);
    }
    
    @GetMapping("/list")
    public ResponseEntity<List<SimpleDTO>> getAllCountries() {
        List<SimpleDTO> countries = countryService.getAllCountries();
        return new ResponseEntity<>(countries, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SimpleDTO> getCountryById(@PathVariable Long id) {
        SimpleDTO country = countryService.getCountryById(id);
        if (country == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(country, HttpStatus.OK);
    }
    
    @PostMapping
    public ResponseEntity<?> createCountry(@RequestBody SimpleDTO dto) {
        logger.info("Creating a new country: {}", dto);
        try {
            SimpleDTO createdCountry = countryService.createCountry(dto);
            return new ResponseEntity<>(createdCountry, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<SimpleDTO> updateCountry(@PathVariable Long id, @RequestBody SimpleDTO dto) {
        SimpleDTO updatedCountry = countryService.updateCountry(id, dto);
        if (updatedCountry == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedCountry, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCountry(@PathVariable Long id) {
        countryService.deleteCountry(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
