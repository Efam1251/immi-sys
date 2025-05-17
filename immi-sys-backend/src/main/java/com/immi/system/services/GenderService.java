package com.immi.system.services;

import com.immi.system.DTOs.SimpleDTO;
import com.immi.system.models.GenderModel;
import com.immi.system.repositories.GenderRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GenderService {
    
    @Autowired
    private GenderRepository genderRepo;
    
    public SimpleDTO createGender(SimpleDTO dto) {
        if (genderRepo.findByName(dto.getName()) != null) {
            throw new RuntimeException("Gender already exists");
        }
        
        GenderModel gender = new GenderModel();
        gender.setName(dto.getName());
        
        return mapToDTO(genderRepo.save(gender));
    }
    
    public List<SimpleDTO> getAllGenders() {
        return genderRepo.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    public SimpleDTO getGenderById(Long id) {
        return mapToDTO(genderRepo.findById(id).orElseThrow(() -> new RuntimeException("Gender not found")));
    }
    
    public SimpleDTO getNextGender(Long id) {
        return mapToDTO(genderRepo.findFirstByIdGreaterThanOrderByIdAsc(id));
    }
    
    public SimpleDTO getPreviousGender(Long id) {
        return mapToDTO(genderRepo.findFirstByIdLessThanOrderByIdDesc(id));
    }

    public SimpleDTO getFirstGender() {
        return mapToDTO(genderRepo.findFirstByOrderByIdAsc());
    }

    public SimpleDTO getLastGender() {
        return mapToDTO(genderRepo.findFirstByOrderByIdDesc());
    }
    
    public SimpleDTO updateGender(Long id, SimpleDTO dto) {
        GenderModel gender = genderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Gender not found"));
        
        gender.setName(dto.getName());
        
        return mapToDTO(genderRepo.save(gender));
    }
    
    public void deleteGender(Long id) {
        genderRepo.deleteById(id);
    }
    
    private SimpleDTO mapToDTO(GenderModel gender) {
        SimpleDTO dto = new SimpleDTO();
        dto.setId(gender.getId());
        dto.setName(gender.getName());
        return dto;
    }
}