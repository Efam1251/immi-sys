package com.immi.system.services;

import com.immi.system.DTOs.SimpleDTO;
import com.immi.system.models.PetitionStatusModel;
import com.immi.system.repositories.PetitionStatusRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class PetitionStatusService {

    private final PetitionStatusRepository petitionStatusRepo;

    public PetitionStatusService(PetitionStatusRepository petitionStatusRepo) {
        this.petitionStatusRepo = petitionStatusRepo;
    }

    public SimpleDTO createPetitionStatus(SimpleDTO dto) {
        if (petitionStatusRepo.findByName(dto.getName()) != null) {
            throw new RuntimeException("Petition Status already exists");
        }

        PetitionStatusModel petitionStatus = new PetitionStatusModel();
        petitionStatus.setName(dto.getName());

        return mapToDTO(petitionStatusRepo.save(petitionStatus));
    }

    public List<SimpleDTO> getAllPetitionStatuses() {
        return petitionStatusRepo.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public SimpleDTO getPetitionStatusById(Long id) {
        return mapToDTO(petitionStatusRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Petition Status not found")));
    }

    public SimpleDTO getNextPetitionStatus(Long id) {
        return mapToDTO(petitionStatusRepo.findFirstByIdGreaterThanOrderByIdAsc(id));
    }

    public SimpleDTO getPreviousPetitionStatus(Long id) {
        return mapToDTO(petitionStatusRepo.findFirstByIdLessThanOrderByIdDesc(id));
    }

    public SimpleDTO getFirstPetitionStatus() {
        return mapToDTO(petitionStatusRepo.findFirstByOrderByIdAsc());
    }

    public SimpleDTO getLastPetitionStatus() {
        return mapToDTO(petitionStatusRepo.findFirstByOrderByIdDesc());
    }

    public SimpleDTO updatePetitionStatus(Long id, SimpleDTO dto) {
        PetitionStatusModel petitionStatus = petitionStatusRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Petition Status not found"));

        petitionStatus.setName(dto.getName());

        return mapToDTO(petitionStatusRepo.save(petitionStatus));
    }

    public void deletePetitionStatus(Long id) {
        petitionStatusRepo.deleteById(id);
    }

    private SimpleDTO mapToDTO(PetitionStatusModel petitionStatus) {
        SimpleDTO dto = new SimpleDTO();
        dto.setId(petitionStatus.getId());
        dto.setName(petitionStatus.getName());
        return dto;
    }
}
