package com.immi.system.repositories;

import com.immi.system.models.PetitionDependentModel;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DependentRepository extends JpaRepository<PetitionDependentModel, Long> {

    @Query("SELECT pd FROM PetitionDependentModel pd WHERE pd.petitionId = :petitionId AND pd.dependentId.id = :dependentId")
    PetitionDependentModel findByPetitionIdAndDependentId(@Param("petitionId") Long petitionId, @Param("dependentId") Long dependentId);

    public List<PetitionDependentModel> findByPetitionId(Long petitionId);
}
