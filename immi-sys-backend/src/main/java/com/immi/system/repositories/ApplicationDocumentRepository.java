package com.immi.system.repositories;

import com.immi.system.models.ApplicationDocumentModel;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationDocumentRepository extends JpaRepository<ApplicationDocumentModel, Long> {
    
    public ApplicationDocumentModel findByApplicationIdAndDocumentIdAndFileName(Long applicationId, Long id, String fileName);

    public List<ApplicationDocumentModel> findByFileNameContaining(String reference);
    
}
