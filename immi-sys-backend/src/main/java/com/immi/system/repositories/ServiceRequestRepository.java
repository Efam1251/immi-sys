package com.immi.system.repositories;

import com.immi.system.models.CustomerModel;
import com.immi.system.models.ServiceModel;
import com.immi.system.models.ServiceRequestModel;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceRequestRepository extends JpaRepository<ServiceRequestModel, Long> {

    Optional<ServiceRequestModel> findFirstByIdGreaterThanOrderByIdAsc(Long id);

    Optional<ServiceRequestModel> findFirstByIdLessThanOrderByIdDesc(Long id);

    Optional<ServiceRequestModel> findFirstByOrderByIdAsc();

    Optional<ServiceRequestModel> findFirstByOrderByIdDesc();
    
    long countByCustomerAndServiceAndRequestDate(CustomerModel customer, ServiceModel service, LocalDate requestDate);
    
}
