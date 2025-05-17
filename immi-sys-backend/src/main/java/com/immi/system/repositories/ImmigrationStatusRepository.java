package com.immi.system.repositories;

import com.immi.system.models.ImmigrationStatusModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImmigrationStatusRepository extends JpaRepository<ImmigrationStatusModel, Long> {

    public ImmigrationStatusModel findByName(String name);

    public ImmigrationStatusModel findFirstByIdGreaterThanOrderByIdAsc(Long id);

    public ImmigrationStatusModel findFirstByIdLessThanOrderByIdDesc(Long id);

    public ImmigrationStatusModel findFirstByOrderByIdAsc();

    public ImmigrationStatusModel findFirstByOrderByIdDesc();
    
}
