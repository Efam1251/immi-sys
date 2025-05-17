package com.immi.system.repositories;

import com.immi.system.models.PetitionModel;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetitionRepository extends JpaRepository<PetitionModel, Long> {

    // Find the next USCIS process based on idUscis (greater than current)
    Optional<PetitionModel> findFirstByPetitionIdGreaterThanOrderByPetitionIdAsc(Long idUscis);

    // Find the previous USCIS process based on idUscis (less than current)
    Optional<PetitionModel> findFirstByPetitionIdLessThanOrderByPetitionIdDesc(Long idUscis);

    // Find the first USCIS process ordered by idUscis ascending
    Optional<PetitionModel> findFirstByOrderByPetitionIdAsc();

    // Find the last USCIS process ordered by idUscis descending
    Optional<PetitionModel> findFirstByOrderByPetitionIdDesc();

    // Check if a process with a specific receipt number exists
    boolean existsByUscisNumber(String uscisNumber);

    // Find by receipt number
    PetitionModel findByUscisNumber(String uscisNumber);
    
}
