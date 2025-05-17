package com.immi.system.services;

import com.immi.system.DTOs.PetitionDTO;
import com.immi.system.models.PetitionModel;
import com.immi.system.repositories.PetitionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PetitionService {

    private final PetitionRepository petitionRepository;

    public PetitionService(PetitionRepository petitionRepository) {
        this.petitionRepository = petitionRepository;
    }

    @Transactional
    public PetitionModel createRecord(PetitionModel petition) {
        // Check if a petition with the same USCIS number already exists
        if (petitionRepository.existsByUscisNumber(petition.getUscisNumber())) {
            throw new RuntimeException("Petition with this USCIS number already exists.");
        }
        petition.setPetitionId(null);
        // Save and return the created petition
        return petitionRepository.save(petition);
    }

    @Transactional
    public PetitionModel updateRecord(Long id, PetitionModel petition) {
        // Find the petition by their ID
        PetitionModel existingPetition = petitionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Petition not found"));

        // Ensure that the USCIS number is not taken by another petition
        if (petitionRepository.existsByUscisNumber(petition.getUscisNumber()) && 
            !existingPetition.getPetitionId().equals(id)) {
            throw new RuntimeException("Petition with this USCIS number already exists.");
        }

        // Save the updated petition and return it
        petition.setPetitionId(id);  // Ensure the ID is maintained
        return petitionRepository.save(petition);
    }

    @Transactional
    public void deleteRecord(Long id) {
        // Delete the petition by its ID
        petitionRepository.deleteById(id);
    }

    public List<PetitionDTO> getAllListRecords() {
        return petitionRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<PetitionModel> findAll() {
        return petitionRepository.findAll();
    }

    public PetitionModel findById(Long id) {
        return petitionRepository.findById(id).orElse(null);
    }

    // Method to get the next petition based on the given id
    public PetitionModel findNext(Long id) {
        Optional<PetitionModel> nextPetition = petitionRepository.findFirstByPetitionIdGreaterThanOrderByPetitionIdAsc(id);
        return nextPetition.orElse(null); // Return null if no next petition found
    }

    // Method to get the previous petition based on the given id
    public PetitionModel findPrevious(Long id) {
        Optional<PetitionModel> previousPetition = petitionRepository.findFirstByPetitionIdLessThanOrderByPetitionIdDesc(id);
        return previousPetition.orElse(null); // Return null if no previous petition found
    }

    // Method to get the first petition in the list
    public PetitionModel findFirst() {
        Optional<PetitionModel> firstPetition = petitionRepository.findFirstByOrderByPetitionIdAsc();
        return firstPetition.orElse(null); // Return null if no first petition found
    }

    // Method to get the last petition in the list
    public PetitionModel findLast() {
        Optional<PetitionModel> lastPetition = petitionRepository.findFirstByOrderByPetitionIdDesc();
        return lastPetition.orElse(null); // Return null if no last petition found
    }

    public PetitionModel findByUscisNumber(String uscisNumber) {
        return petitionRepository.findByUscisNumber(uscisNumber);
    }

    private PetitionDTO mapToDTO(PetitionModel petition) {
        PetitionDTO dto = new PetitionDTO();
        dto.setId(petition.getPetitionId());
        dto.setPetitioner(petition.getPetitioner().getFirstName()+" "+petition.getPetitioner().getLastName());
        dto.setBeneficiary(petition.getBeneficiary().getFirstName()+" "+petition.getBeneficiary().getLastName());
        dto.setUscisNumber(petition.getUscisNumber());
        dto.setCategory(petition.getCategory().getName());
        dto.setPriorityDate(petition.getPriorityDate());

        return dto;
    }
}
