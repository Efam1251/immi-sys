package com.immi.system.repositories;

import com.immi.system.models.InvoiceStatusModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceStatusRepository extends JpaRepository<InvoiceStatusModel, Long> {

    public InvoiceStatusModel findByName(String paid);
    
}
