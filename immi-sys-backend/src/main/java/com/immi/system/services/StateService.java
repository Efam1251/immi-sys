package com.immi.system.services;

import com.immi.system.DTOs.DropDownDTO;
import com.immi.system.DTOs.StateDTO;
import com.immi.system.config.ResourceNotFoundException;
import com.immi.system.models.CountryModel;
import com.immi.system.models.StateModel;
import com.immi.system.repositories.CountryRepository;
import com.immi.system.repositories.StateRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class StateService {
    
    private final StateRepository stateRepo;
    private final CountryRepository countryRepo;

    public StateService(StateRepository stateRepo, CountryRepository countryRepo) {
        this.stateRepo = stateRepo;
        this.countryRepo = countryRepo;
    }
    
    @Transactional
    public StateDTO createState(StateDTO dto) {
        if (stateRepo.findByName(dto.getName()) != null) {
            throw new RuntimeException("State already exists");
        }

        CountryModel country = countryRepo.findById(dto.getCountry().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Country not found"));

        StateModel state = new StateModel();
        state.setName(dto.getName());
        state.setCode(dto.getCode());
        state.setCountry(country);
        
        return mapToDTO(stateRepo.save(state));
    }
    
    @Transactional
    public StateDTO updateState(Long id, StateDTO dto) {
        StateModel state = stateRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("State not found"));

        // Ensure the name is not taken by another record
        StateModel existingState = stateRepo.findByName(dto.getName());
        if (existingState != null && !existingState.getId().equals(id)) {
            throw new RuntimeException("State with this name already exists");
        }

        CountryModel country = countryRepo.findById(dto.getCountry().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Country not found"));

        state.setName(dto.getName());
        state.setCode(dto.getCode());
        state.setCountry(country);

        return mapToDTO(stateRepo.save(state));
    }
    
    @Transactional
    public void deleteState(Long id) {
        stateRepo.deleteById(id);
    }
    
    public List<StateDTO> getAllStates() {
        return stateRepo.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    public StateDTO getStateById(Long id) {
        return stateRepo.findById(id)
            .map(this::mapToDTO)
            .orElseThrow(() -> new ResourceNotFoundException("State not found"));
    }
    
    public StateDTO getNextState(Long id) {
        return stateRepo.findFirstByIdGreaterThanOrderByIdAsc(id)
            .map(this::mapToDTO)
            .orElse(null);
    }
    
    public StateDTO getPreviousState(Long id) {
        return stateRepo.findFirstByIdLessThanOrderByIdDesc(id)
            .map(this::mapToDTO)
            .orElse(null);
    }
    
    public StateDTO getFirstState() {
        return stateRepo.findFirstByOrderByIdAsc()
                .map(this::mapToDTO)
                .orElse(null);
    }
    
    public StateDTO getLastState() {
        return stateRepo.findFirstByOrderByIdDesc()
                .map(this::mapToDTO)
                .orElse(null);
    }

    private StateDTO mapToDTO(StateModel state) {
        StateDTO dto = new StateDTO();
        dto.setId(state.getId());
        dto.setName(state.getName());
        dto.setCode(state.getCode());
        if (state.getCountry() != null) {
            DropDownDTO countryDTO = new DropDownDTO();
            countryDTO.setId(state.getCountry().getId());
            countryDTO.setName(state.getCountry().getName());
            dto.setCountry(countryDTO);
        }
        return dto;
    }
    
}
