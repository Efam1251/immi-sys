package com.immi.system.repositories;

import com.immi.system.models.VisaDocumentModel;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisaDocumentRepository extends JpaRepository<VisaDocumentModel, Long> {

    public List<VisaDocumentModel> findByFileNameContaining(String caseNumber);

    public VisaDocumentModel findByVisaIdAndDocumentIdAndFileName(Long visaId, Long documentId, String fileName);
    
}
