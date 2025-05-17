package com.immi.system.services;

import com.immi.system.DTOs.SimpleDTO;
import com.immi.system.models.VisaTypeModel;
import com.immi.system.repositories.VisaTypeRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VisaTypeService {

    @Autowired
    private VisaTypeRepository visaTypeRepo;

    public SimpleDTO createVisaType(SimpleDTO dto) {
        if (visaTypeRepo.findByName(dto.getName()) != null) {
            throw new RuntimeException("Visa Type already exists");
        }

        VisaTypeModel visaType = new VisaTypeModel();
        visaType.setName(dto.getName());

        return mapToDTO(visaTypeRepo.save(visaType));
    }

    public List<SimpleDTO> getAllVisaTypes() {
        return visaTypeRepo.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public SimpleDTO getVisaTypeById(Long id) {
        return mapToDTO(visaTypeRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Visa Type not found")));
    }
    
    public VisaTypeModel getVisaTypeByName(String name) {
        return visaTypeRepo.findByName(name);
    }

    public SimpleDTO getNextVisaType(Long id) {
        return mapToDTO(visaTypeRepo.findFirstByIdGreaterThanOrderByIdAsc(id));
    }

    public SimpleDTO getPreviousVisaType(Long id) {
        return mapToDTO(visaTypeRepo.findFirstByIdLessThanOrderByIdDesc(id));
    }

    public SimpleDTO getFirstVisaType() {
        return mapToDTO(visaTypeRepo.findFirstByOrderByIdAsc());
    }

    public SimpleDTO getLastVisaType() {
        return mapToDTO(visaTypeRepo.findFirstByOrderByIdDesc());
    }

    public SimpleDTO updateVisaType(Long id, SimpleDTO dto) {
        VisaTypeModel visaType = visaTypeRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Visa Type not found"));

        visaType.setName(dto.getName());

        return mapToDTO(visaTypeRepo.save(visaType));
    }

    public void deleteVisaType(Long id) {
        visaTypeRepo.deleteById(id);
    }

    private SimpleDTO mapToDTO(VisaTypeModel visaType) {
        SimpleDTO dto = new SimpleDTO();
        dto.setId(visaType.getId());
        dto.setName(visaType.getName());
        return dto;
    }

}