package com.immi.system.repositories;

import com.immi.system.models.StateModel;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StateRepository extends JpaRepository<StateModel, Long> {
    
    public StateModel findByName(String name);

    public Optional<StateModel> findFirstByIdGreaterThanOrderByIdAsc(Long id);
    
    public Optional<StateModel> findFirstByIdLessThanOrderByIdDesc(Long id);

    public Optional<StateModel> findFirstByOrderByIdAsc();
    
    public Optional<StateModel> findFirstByOrderByIdDesc();

    
}
