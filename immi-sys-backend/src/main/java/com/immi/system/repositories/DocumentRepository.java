package com.immi.system.repositories;

import com.immi.system.models.DocumentModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<DocumentModel, Long> {

    public DocumentModel findByName(String name);
    
}
