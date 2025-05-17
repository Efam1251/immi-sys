package com.immi.system.services;

import com.immi.system.models.AmenityModel;
import com.immi.system.repositories.AmenityRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AmenityService {

    @Autowired
    private AmenityRepository amenityRepository;

    // Fetch all amenities
    public List<AmenityModel> getAllAmenities() {
        return amenityRepository.findAll();
    }

    // Save a new or update an existing amenity
    public void save(AmenityModel amenity) {
        amenityRepository.save(amenity);
    }

    // Find an amenity by its name (optional, if needed)
    public AmenityModel findAmenityByName(String name) {
        return amenityRepository.findByName(name);
    }

    // Find an amenity by its ID
    public AmenityModel findAmenityById(Long id) {
        return amenityRepository.findById(id).orElse(null);
    }

    // Delete an amenity by its ID
    public void deleteAmenity(Long id) {
        amenityRepository.deleteById(id);
    }
}
