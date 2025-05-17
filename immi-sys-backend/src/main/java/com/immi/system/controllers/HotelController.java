package com.immi.system.controllers;

import com.immi.system.DTOs.HotelDTO;
import com.immi.system.models.AmenityModel;
import com.immi.system.models.HotelModel;
import com.immi.system.services.AmenityService;
import com.immi.system.services.HotelService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping("/travel/forms/hotels")
public class HotelController {

    @Autowired
    private HotelService hotelService;
    
    @Autowired
    private AmenityService amenityService;
    
    @GetMapping("/form")
    public ResponseEntity<?> getHotel(
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

        HotelModel hotel = switch (direction) {
            case "Current" -> hotelService.getHotelById(currentId);
            case "Next" -> hotelService.getNextHotel(currentId);
            case "Previous" -> hotelService.getPreviousHotel(currentId);
            case "First" -> hotelService.getFirstHotel();
            case "Last" -> hotelService.getLastHotel();
            default -> hotelService.getHotelById(currentId); // fallback to current if no direction provided
        };

        if (hotel == null) {
            return ResponseEntity.ok(null);
        }

        return ResponseEntity.ok(hotel);
    }

    // Get all hotels
    @GetMapping("/list")
    public ResponseEntity<List<HotelDTO>> getAllHotels() {
        List<HotelModel> hotels = hotelService.getAllHotels();
        if (hotels == null || hotels.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Collections.emptyList());
        }
        List<HotelDTO> hotelDTOs = hotels.stream()
                                         .map(this::mapToDTO)
                                         .collect(Collectors.toList());
        return ResponseEntity.ok(hotelDTOs);
    }


    // Mapping function to convert HotelModel to HotelDTO
    private HotelDTO mapToDTO(HotelModel hotel) {
        HotelDTO hotelDTO = new HotelDTO();
        hotelDTO.setId(hotel.getId());
        hotelDTO.setName(hotel.getName());
        hotelDTO.setDescription(hotel.getDescription());
        hotelDTO.setAddress(hotel.getAddress().getStreet()+", "+hotel.getAddress().getState().getName()+" "+hotel.getAddress().getZipCode());  // Assuming you want to show just the street as address, change as needed
        return hotelDTO;
    }
    
    @PostMapping("/save")
    public ResponseEntity<Map<String, String>> createHotel(@RequestBody HotelModel hotel) {
        // Basic validation for hotel fields
        if (!isValidHotel(hotel)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message", "Invalid hotel data"));
        }
        
        if (hotel.getAddress() != null) {
            Optional<HotelModel> existingHotelWithAddress = hotelService.findByAddress(hotel.getAddress());
            if (existingHotelWithAddress.isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "Address is already assigned to another hotel."));
            }
        }
        
        // Ensure amenities list is not null before proceeding
        if (hotel.getAmenities() != null && !hotel.getAmenities().isEmpty()) {
            List<AmenityModel> resolvedAmenities = new ArrayList<>();
            
            for (AmenityModel amenity : hotel.getAmenities()) {
                // Fetch the full Amenity entity from the database by ID
                AmenityModel existingAmenity = amenityService.findAmenityById(amenity.getId());
                if (existingAmenity != null) {
                    resolvedAmenities.add(existingAmenity);
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "Amenity with ID " + amenity.getId() + " not found"));
                }
            }
            
            // Attach resolved amenities to the hotel
            hotel.setAmenities(resolvedAmenities);
        }
        
        hotelService.saveHotel(hotel); // Save hotel with amenities
        // Return success message as JSON
        Map<String, String> response = new HashMap<>();
        response.put("message", "Record added successfully.");

        return ResponseEntity.ok(response);  // Send response as JSON
    }
    
    private boolean isValidHotel(HotelModel hotel) {
        return hotel.getName() != null && !hotel.getName().isBlank()
            && hotel.getDescription() != null && !hotel.getDescription().isBlank()
            && hotel.getAddress() != null;
    }
    
    // Update an existing hotel
    @PutMapping("/save/{id}")
    public ResponseEntity<Map<String, String>> updateHotel(@PathVariable Long id, @RequestBody HotelModel hotel) {
        HotelModel existingHotel = hotelService.getHotelById(id);
        if (existingHotel != null) {
            existingHotel.setId(hotel.getId());
            existingHotel.setName(hotel.getName());
            existingHotel.setDescription(hotel.getDescription());
            existingHotel.setAddress(hotel.getAddress());
            existingHotel.setAmenities(hotel.getAmenities());

            hotelService.saveHotel(existingHotel); // Save updated hotel
            // Return success message as JSON
            Map<String, String> response = new HashMap<>();
            response.put("message", "Record updated successfully.");

        return ResponseEntity.ok(response);  // Send response as JSON
        }
        return null;
    }

    // Delete a hotel
    @DeleteMapping("/{id}")
    public void deleteHotel(@PathVariable Long id) {
        hotelService.deleteHotel(id);  // Delete the hotel by ID
    }
}
