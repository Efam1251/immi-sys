package com.immi.system.services;

import com.immi.system.DTOs.SimpleDTO;
import com.immi.system.models.ImmigrationStatusModel;
import com.immi.system.repositories.ImmigrationStatusRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImmigrationStatusService {
    
    @Autowired
    private ImmigrationStatusRepository immigrationStatusRepo;
    
    public SimpleDTO createImmigrationStatus(SimpleDTO dto) {
        if (immigrationStatusRepo.findByName(dto.getName()) != null) {
            throw new RuntimeException("Immigration Status already exists");
        }
        
        ImmigrationStatusModel status = new ImmigrationStatusModel();
        status.setName(dto.getName());
        
        return mapToDTO(immigrationStatusRepo.save(status));
    }
    
    public List<SimpleDTO> getAllImmigrationStatuses() {
        return immigrationStatusRepo.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    public SimpleDTO getImmigrationStatusById(Long id) {
        return mapToDTO(immigrationStatusRepo.findById(id).orElseThrow(() -> new RuntimeException("Immigration Status not found")));
    }
    
    public SimpleDTO getNextImmigrationStatus(Long id) {
        return mapToDTO(immigrationStatusRepo.findFirstByIdGreaterThanOrderByIdAsc(id));
    }
    
    public SimpleDTO getPreviousImmigrationStatus(Long id) {
        return mapToDTO(immigrationStatusRepo.findFirstByIdLessThanOrderByIdDesc(id));
    }

    public SimpleDTO getFirstImmigrationStatus() {
        return mapToDTO(immigrationStatusRepo.findFirstByOrderByIdAsc());
    }

    public SimpleDTO getLastImmigrationStatus() {
        return mapToDTO(immigrationStatusRepo.findFirstByOrderByIdDesc());
    }
    
    public SimpleDTO updateImmigrationStatus(Long id, SimpleDTO dto) {
        ImmigrationStatusModel status = immigrationStatusRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Immigration Status not found"));
        
        status.setName(dto.getName());
        
        return mapToDTO(immigrationStatusRepo.save(status));
    }
    
    public void deleteImmigrationStatus(Long id) {
        immigrationStatusRepo.deleteById(id);
    }
    
    private SimpleDTO mapToDTO(ImmigrationStatusModel status) {
        SimpleDTO dto = new SimpleDTO();
        dto.setId(status.getId());
        dto.setName(status.getName());
        return dto;
    }
}
