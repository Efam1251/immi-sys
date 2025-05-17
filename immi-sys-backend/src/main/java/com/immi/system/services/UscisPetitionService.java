package com.immi.system.services;

import com.immi.system.DTOs.PetitionDTO;
import com.immi.system.models.PetitionModel;
import com.immi.system.models.UscisPetitionModel;
import com.immi.system.models.UscisPetitionEventModel;
import com.immi.system.repositories.UscisPetitionRepository;
import com.immi.system.repositories.UscisPetitionEventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UscisPetitionService {

    private final UscisPetitionRepository uscisPetitionRepository;
    private final UscisPetitionEventRepository uscisPetitionEventRepository;

    public UscisPetitionService(UscisPetitionRepository uscisPetitionRepository,
                                UscisPetitionEventRepository uscisPetitionEventRepository) {
        this.uscisPetitionRepository = uscisPetitionRepository;
        this.uscisPetitionEventRepository = uscisPetitionEventRepository;
    }

    // ====== CRUD Operations ======

    @Transactional
    public UscisPetitionModel createRecord(UscisPetitionModel petition) {
        if (uscisPetitionRepository.existsByUscisNumber(petition.getUscisNumber())) {
            throw new RuntimeException("Petition with this number already exists.");
        }
        petition.setPetitionId(null);
        return uscisPetitionRepository.save(petition);
    }

    @Transactional
    public UscisPetitionModel updateRecord(Long id, UscisPetitionModel petition) {
        UscisPetitionModel existing = uscisPetitionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Petition not found"));

        if (uscisPetitionRepository.existsByUscisNumber(petition.getUscisNumber())
                && !existing.getPetitionId().equals(id)) {
            throw new RuntimeException("Petition with this number already exists.");
        }

        petition.setPetitionId(id);
        return uscisPetitionRepository.save(petition);
    }

    @Transactional
    public void deleteRecord(Long id) {
        uscisPetitionRepository.deleteById(id);
    }

    // ====== Navigation Methods ======

    public UscisPetitionModel getPetition(Long id) {
        return uscisPetitionRepository.findById(id).orElse(null);
    }

    public UscisPetitionModel findNext(Long id) {
        return uscisPetitionRepository.findFirstByPetitionIdGreaterThanOrderByPetitionIdAsc(id).orElse(null);
    }

    public UscisPetitionModel findPrevious(Long id) {
        return uscisPetitionRepository.findFirstByPetitionIdLessThanOrderByPetitionIdDesc(id).orElse(null);
    }

    public UscisPetitionModel findFirst() {
        return uscisPetitionRepository.findFirstByOrderByPetitionIdAsc().orElse(null);
    }

    public UscisPetitionModel findLast() {
        return uscisPetitionRepository.findFirstByOrderByPetitionIdDesc().orElse(null);
    }

    // ====== Lists and Dropdowns ======
    public List<PetitionDTO> getAllListRecords() {
        List<UscisPetitionModel> petitions = uscisPetitionRepository.findAll();
        return petitions.stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }

    public List<UscisPetitionModel> findAll() {
        return uscisPetitionRepository.findAll();
    }

    // ====== Events ======

    public List<UscisPetitionEventModel> getEventsForPetition(Long petitionId) {
        return uscisPetitionEventRepository.findAllByPetition_PetitionId(petitionId);
    }

    public UscisPetitionEventModel addEvent(Long petitionId, UscisPetitionEventModel event) {
        return uscisPetitionEventRepository.save(event);
    }

    public void deleteEventByEventId(Long eventId) {
        uscisPetitionEventRepository.deleteById(eventId);
    }

    public boolean existEventByEventId(Long eventId) {
        return uscisPetitionEventRepository.existsById(eventId);
    }
    
    private PetitionDTO convertToDto(UscisPetitionModel petition) {
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
