package com.immi.system.repositories;

import com.immi.system.models.CitizenshipDocumentModel;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CitizenshipDocumentRepository extends JpaRepository<CitizenshipDocumentModel, Long> {
    
    public List<CitizenshipDocumentModel> findByFileNameContaining(String reference);

    public CitizenshipDocumentModel findByCitizenshipProcessIdAndDocumentIdAndFileName(Long visaId, Long documentId, String fileName);
    
}
