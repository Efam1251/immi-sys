package com.immi.system.repositories;

import com.immi.system.models.TaxDocumentModel;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaxDocumentRepository extends JpaRepository<TaxDocumentModel, Long> {
    
    public List<TaxDocumentModel> findByTaxFilingId(Long taxFilingId);
    
    public TaxDocumentModel findByTaxFilingIdAndDocumentIdAndFileName(Long taxFilingId, Long documentId, String fileName);

    public List<TaxDocumentModel> findByFileNameContaining(String reference);
}
