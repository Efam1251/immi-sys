package com.immi.system.repositories;

import com.immi.system.models.ServiceRequestDocumentModel;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRequestDocumentRepository extends JpaRepository<ServiceRequestDocumentModel, Long> {
    
    public ServiceRequestDocumentModel findByServiceRequestIdAndDocumentIdAndFileName(Long serviceRequestId, Long id, String fileName);

    public List<ServiceRequestDocumentModel> findByFileNameContaining(String reference);
}
