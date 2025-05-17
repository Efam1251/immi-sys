package com.immi.system.repositories;

import com.immi.system.models.UscisPetitionEventModel;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UscisPetitionEventRepository extends JpaRepository<UscisPetitionEventModel, Long> {

    List<UscisPetitionEventModel> findAllByPetition_PetitionId(Long petitionId);

}
