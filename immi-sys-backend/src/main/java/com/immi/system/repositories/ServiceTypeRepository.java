package com.immi.system.repositories;

import com.immi.system.models.ServiceTypeModel;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceTypeRepository extends JpaRepository<ServiceTypeModel, Long> {
    
    ServiceTypeModel findByName(String name);

    public Optional<ServiceTypeModel> findFirstByIdGreaterThanOrderByIdAsc(Long id);

    public Optional<ServiceTypeModel> findFirstByIdLessThanOrderByIdDesc(Long id);

    public Optional<ServiceTypeModel> findFirstByOrderByIdAsc();

    public Optional<ServiceTypeModel> findFirstByOrderByIdDesc();

    public boolean existsByName(String name);
    
}
