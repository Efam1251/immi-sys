package com.immi.system.repositories;

import com.immi.system.models.StatusModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<StatusModel, Long> {

    public StatusModel findByName(String name);
    
}
