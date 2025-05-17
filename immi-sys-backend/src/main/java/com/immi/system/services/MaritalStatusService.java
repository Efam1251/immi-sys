package com.immi.system.services;

import com.immi.system.DTOs.SimpleDTO;
import com.immi.system.models.MaritalStatusModel;
import com.immi.system.repositories.MaritalStatusRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class MaritalStatusService {
    
    private final MaritalStatusRepository maritalStatusRepo;
    
    public MaritalStatusService(MaritalStatusRepository maritalStatusRepo) {
        this.maritalStatusRepo = maritalStatusRepo;
    }
    
    public SimpleDTO createMaritalStatus(SimpleDTO dto) {
        if (maritalStatusRepo.findByName(dto.getName()) != null) {
            throw new RuntimeException("Marital Status already exists");
        }
        
        MaritalStatusModel maritalStatus = new MaritalStatusModel();
        maritalStatus.setName(dto.getName());
        
        return mapToDTO(maritalStatusRepo.save(maritalStatus));
    }
    
    public List<SimpleDTO> getAllMaritalStatuses() {
        return maritalStatusRepo.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    public SimpleDTO getMaritalStatusById(Long id) {
        return mapToDTO(maritalStatusRepo.findById(id).orElseThrow(() -> new RuntimeException("Marital Status not found")));
    }
    
    public SimpleDTO getNextMaritalStatus(Long id) {
        return mapToDTO(maritalStatusRepo.findFirstByIdGreaterThanOrderByIdAsc(id));
    }
    
    public SimpleDTO getPreviousMaritalStatus(Long id) {
        return mapToDTO(maritalStatusRepo.findFirstByIdLessThanOrderByIdDesc(id));
    }

    public SimpleDTO getFirstMaritalStatus() {
        return mapToDTO(maritalStatusRepo.findFirstByOrderByIdAsc());
    }

    public SimpleDTO getLastMaritalStatus() {
        return mapToDTO(maritalStatusRepo.findFirstByOrderByIdDesc());
    }
    
    public SimpleDTO updateMaritalStatus(Long id, SimpleDTO dto) {
        MaritalStatusModel maritalStatus = maritalStatusRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Marital Status not found"));
        
        maritalStatus.setName(dto.getName());
        
        return mapToDTO(maritalStatusRepo.save(maritalStatus));
    }
    
    public void deleteMaritalStatus(Long id) {
        maritalStatusRepo.deleteById(id);
    }
    
    private SimpleDTO mapToDTO(MaritalStatusModel maritalStatus) {
        SimpleDTO dto = new SimpleDTO();
        dto.setId(maritalStatus.getId());
        dto.setName(maritalStatus.getName());
        return dto;
    }
}
