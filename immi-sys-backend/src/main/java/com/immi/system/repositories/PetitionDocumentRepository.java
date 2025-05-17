package com.immi.system.repositories;

import com.immi.system.models.PetitionDocumentModel;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PetitionDocumentRepository extends JpaRepository<PetitionDocumentModel, Long> {

    // Search for documents where fileName contains the caseNumber
    List<PetitionDocumentModel> findByFileNameContaining(String caseNumber);

    public PetitionDocumentModel findByPetitionIdAndDocumentIdAndFileName(Long petitionId, Long documentId, String fileName);
    
}
