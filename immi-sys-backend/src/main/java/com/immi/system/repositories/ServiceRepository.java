package com.immi.system.repositories;

import com.immi.system.models.ServiceModel;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ServiceRepository extends JpaRepository<ServiceModel, Long> {

    public boolean existsByServiceName(String name);
    
    public ServiceModel findByServiceName(String serviceName); // Matching the field name

    public Optional<ServiceModel> findFirstByServiceIdGreaterThanOrderByServiceIdAsc(Long serviceId);
    
    public Optional<ServiceModel> findFirstByServiceIdLessThanOrderByServiceIdDesc(Long serviceId);

    public Optional<ServiceModel> findFirstByOrderByServiceIdAsc();
    
    public Optional<ServiceModel> findFirstByOrderByServiceIdDesc();
    
}
