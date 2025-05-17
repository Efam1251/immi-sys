package com.immi.system.services;

import com.immi.system.DTOs.PetitionDependentDTO;
import com.immi.system.models.DependentTypeModel;
import com.immi.system.models.PetitionDependentModel;
import com.immi.system.repositories.DependentRepository;
import com.immi.system.repositories.DependentTypeRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DependentService {
    
    @Autowired
    private DependentRepository dependentRepository;
    
    @Autowired
    private DependentTypeRepository dependentTypeRepository;
    
    public List<PetitionDependentModel> getAllDependents() {
        return dependentRepository.findAll();
    }

    public void save(PetitionDependentModel dependent) {
        dependentRepository.save(dependent);
    }

    public PetitionDependentModel searchPetitionDependent(Long petitionId, Long dependentId) {
        return dependentRepository.findByPetitionIdAndDependentId(petitionId, dependentId);
    }

    public List<DependentTypeModel> getAllDependentType() {
        return dependentTypeRepository.findAll();
    }

    public List<PetitionDependentDTO> findByDependentPetitionId(Long petitionId) {
        List<PetitionDependentModel> dependents = dependentRepository.findByPetitionId(petitionId);

        return dependents.stream().map(dependent -> {
            PetitionDependentDTO dto = new PetitionDependentDTO();
            dto.setId(dependent.getId());
            dto.setPetitionId(dependent.getPetitionId());
            dto.setDependentId(dependent.getDependentId().getCustomerId());
            dto.setDependentName(dependent.getDependentId().getFirstName()+" "+dependent.getDependentId().getLastName()); // Assuming dependent has a name field
            dto.setDependentTypeId(dependent.getDependentTypeId().getId());
            dto.setDependentTypeName(dependent.getDependentTypeId().getName()); // Assuming dependent type has a name field
            return dto;
        }).collect(Collectors.toList());
    }

    @Transactional
    public boolean deletePetitionDependent(Long petitionId, Long dependentId) {
        // Check if the dependent exists and is associated with the petition
        PetitionDependentModel dependent = dependentRepository.findByPetitionIdAndDependentId(petitionId, dependentId);

        if (dependent != null) {
            // Remove the association between dependent and petition
            dependentRepository.delete(dependent);
            return true;
        }

        return false;  // Return false if dependent is not found or doesn't belong to the petition
    }

}
