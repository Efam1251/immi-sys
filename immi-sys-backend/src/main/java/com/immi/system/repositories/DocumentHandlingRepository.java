package com.immi.system.repositories;

import com.immi.system.models.DocumentHandlingModel;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentHandlingRepository extends JpaRepository<DocumentHandlingModel, Long> {
    
    List<DocumentHandlingModel> findByRecordTypeAndFileNameContaining(String recordType, String reference);

    public DocumentHandlingModel findByRecordTypeAndRecordIdAndDocumentIdAndFileName(String type, Long recordId, Long id, String fileName);

    public DocumentHandlingModel findByRecordTypeAndRecordId(String type, Long id);

}
