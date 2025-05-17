package com.immi.system.repositories;

import com.immi.system.models.MaritalStatusModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaritalStatusRepository extends JpaRepository<MaritalStatusModel, Long> {

    public MaritalStatusModel findByName(String name);

    public MaritalStatusModel findFirstByIdGreaterThanOrderByIdAsc(Long id);

    public MaritalStatusModel findFirstByIdLessThanOrderByIdDesc(Long id);

    public MaritalStatusModel findFirstByOrderByIdAsc();

    public MaritalStatusModel findFirstByOrderByIdDesc();
    
}
