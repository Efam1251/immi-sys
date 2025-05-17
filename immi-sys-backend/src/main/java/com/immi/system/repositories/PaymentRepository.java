package com.immi.system.repositories;

import com.immi.system.models.PaymentModel;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PaymentRepository extends JpaRepository<PaymentModel, Long> {

    // Sum payments by invoice number (assuming Payment entity has an invoice reference)
    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM PaymentModel p WHERE p.invoiceNumber = :invoiceNumber AND p.cancelled = false")
    BigDecimal sumPaymentsByInvoiceNumber(@Param("invoiceNumber") Long invoiceNumber);

    
    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM PaymentModel p WHERE p.invoiceNumber = :invoiceNumber AND p.cancelled = false")
    BigDecimal getTotalPaymentsForInvoice(@Param("invoiceNumber") Long invoiceNumber);


    public List<PaymentModel> findByInvoiceNumberAndCancelledFalse(Long invoiceNumber);
    
}
