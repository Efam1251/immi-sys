package com.immi.system.repositories;

import com.immi.system.models.UscisProcessDocumentModel;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UscisProcessDocumentRepository extends JpaRepository<UscisProcessDocumentModel, Long> {

    public UscisProcessDocumentModel findByUscisProcessIdAndDocumentIdAndFileName(Long uscisProcessId, Long id, String fileName);

    public List<UscisProcessDocumentModel> findByFileNameContaining(String reference);
    
}
