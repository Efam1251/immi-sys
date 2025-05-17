package com.immi.system.controllers;

import com.immi.system.models.AmenityModel;
import com.immi.system.services.AmenityService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/travel/api/amenity")
public class AmenityController {

    @Autowired
    private AmenityService amenityService;

    // Endpoint to get all amenities for dropdown
    @GetMapping("/amenities")
    @ResponseBody
    public List<AmenityModel> getAllAmenities() {
        return amenityService.getAllAmenities();
    }

    // Endpoint to save a new amenity
    @PostMapping("/save")
    public ResponseEntity<Map<String, String>> saveAmenity(@RequestBody AmenityModel amenity) {
        // Check if an amenity with the same name already exists
        AmenityModel existingAmenity = amenityService.findAmenityByName(amenity.getName());

        if (existingAmenity != null) {
            // Return a conflict status with a JSON response
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Amenity already exists!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        // Save the new amenity if it doesn't exist
        amenityService.save(amenity);

        // Return a success message as JSON
        Map<String, String> successResponse = new HashMap<>();
        successResponse.put("message", "Amenity added successfully!");
        return ResponseEntity.ok(successResponse);
    }
}
