package com.immi.system.repositories;

import com.immi.system.models.GenderModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenderRepository extends JpaRepository<GenderModel, Long> {

    public GenderModel findByName(String name);

    public GenderModel findFirstByIdGreaterThanOrderByIdAsc(Long id);

    public GenderModel findFirstByIdLessThanOrderByIdDesc(Long id);

    public GenderModel findFirstByOrderByIdAsc();

    public GenderModel findFirstByOrderByIdDesc();
    
}
