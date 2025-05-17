package com.immi.system.repositories;

import com.immi.system.models.PetitionStatusModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetitionStatusRepository extends JpaRepository<PetitionStatusModel, Long> {

    PetitionStatusModel findByName(String name);

    PetitionStatusModel findFirstByIdGreaterThanOrderByIdAsc(Long id);

    PetitionStatusModel findFirstByIdLessThanOrderByIdDesc(Long id);

    PetitionStatusModel findFirstByOrderByIdAsc();

    PetitionStatusModel findFirstByOrderByIdDesc();
}
