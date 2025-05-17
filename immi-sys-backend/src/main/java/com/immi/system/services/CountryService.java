package com.immi.system.services;

import com.immi.system.DTOs.SimpleDTO;
import com.immi.system.models.CountryModel;
import com.immi.system.repositories.CountryRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CountryService {
    
    @Autowired
    private CountryRepository countryRepo;
    
    public SimpleDTO createCountry(SimpleDTO dto) {
        if (countryRepo.findByName(dto.getName()) != null) {
            throw new RuntimeException("Country already exists");
        }
        
        CountryModel country = new CountryModel();
        country.setName(dto.getName());
        
        return mapToDTO(countryRepo.save(country));
    }
    
    public List<SimpleDTO> getAllCountries() {
        return countryRepo.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    public SimpleDTO getCountryById(Long id) {
        return mapToDTO(countryRepo.findById(id).orElseThrow(() -> new RuntimeException("Country not found")));
    }
    
    public SimpleDTO getNextCountry(Long id) {
        return mapToDTO(countryRepo.findFirstByIdGreaterThanOrderByIdAsc(id));
    }
    
    public SimpleDTO getPreviousCountry(Long id) {
        return mapToDTO(countryRepo.findFirstByIdLessThanOrderByIdDesc(id));
    }

    public SimpleDTO getFirstCountry() {
        return mapToDTO(countryRepo.findFirstByOrderByIdAsc());
    }

    public SimpleDTO getLastCountry() {
        return mapToDTO(countryRepo.findFirstByOrderByIdDesc());
    }
    
    public SimpleDTO updateCountry(Long id, SimpleDTO dto) {
        CountryModel country = countryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Country not found"));
        
        country.setName(dto.getName());
        
        return mapToDTO(countryRepo.save(country));
    }
    
    public void deleteCountry(Long id) {
        countryRepo.deleteById(id);
    }
    
    private SimpleDTO mapToDTO(CountryModel country) {
        SimpleDTO dto = new SimpleDTO();
        dto.setId(country.getId());
        dto.setName(country.getName());
        return dto;
    }
}
