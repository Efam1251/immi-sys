package com.immi.system.repositories;

import com.immi.system.models.DependentTypeModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DependentTypeRepository extends JpaRepository<DependentTypeModel, Long> {
    
}
