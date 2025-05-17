package com.immi.system.repositories;

import com.immi.system.models.AmenityModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AmenityRepository extends JpaRepository<AmenityModel, Long> {

    public AmenityModel findByName(String name);
    
}
