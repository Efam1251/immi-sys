package com.immi.system.repositories;

import com.immi.system.models.InvoiceModel;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface InvoiceRepository extends JpaRepository<InvoiceModel, Long> {

    @Query("SELECT MAX(i.invoiceNumber) FROM InvoiceModel i")
    Long findMaxInvoiceNumber();

    public InvoiceModel findByInvoiceNumber(Long invoiceNumber);

    @Query("SELECT i FROM InvoiceModel i "
            + "LEFT JOIN FETCH i.invoiceDetails d "
            + "LEFT JOIN FETCH d.service s "
            + "LEFT JOIN FETCH i.customer c")
    public List<InvoiceModel> findAllWithDetailsAndCustomer();

}
