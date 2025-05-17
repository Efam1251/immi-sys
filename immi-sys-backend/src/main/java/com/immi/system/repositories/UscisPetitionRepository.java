package com.immi.system.repositories;

import com.immi.system.models.UscisPetitionModel;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UscisPetitionRepository extends JpaRepository<UscisPetitionModel, Long> {

    Optional<UscisPetitionModel> findFirstByPetitionIdGreaterThanOrderByPetitionIdAsc(Long id);

    Optional<UscisPetitionModel> findFirstByPetitionIdLessThanOrderByPetitionIdDesc(Long id);

    Optional<UscisPetitionModel> findFirstByOrderByPetitionIdAsc();

    Optional<UscisPetitionModel> findFirstByOrderByPetitionIdDesc();

    boolean existsByUscisNumber(String uscisNumber);
}
