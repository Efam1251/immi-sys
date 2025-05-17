package com.immi.system.repositories;

import com.immi.system.models.CustomerModel;
import com.immi.system.models.TaxFilingModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaxFilingRepository extends JpaRepository<TaxFilingModel, Long> {

    // Find the next tax filing based on ID (greater than the current ID)
    Optional<TaxFilingModel> findFirstByTaxFilingIdGreaterThanOrderByTaxFilingIdAsc(Long id);

    // Find the previous tax filing based on ID (less than the current ID)
    Optional<TaxFilingModel> findFirstByTaxFilingIdLessThanOrderByTaxFilingIdDesc(Long id);

    // Find the first tax filing ordered by ID ascending
    Optional<TaxFilingModel> findFirstByOrderByTaxFilingIdAsc();

    // Find the last tax filing ordered by ID descending
    Optional<TaxFilingModel> findFirstByOrderByTaxFilingIdDesc();

    // Override the default findById for clarity
    @Override
    Optional<TaxFilingModel> findById(Long id);

    public Optional<TaxFilingModel> findByCustomerAndTaxYear(CustomerModel customer, Integer taxYear);

    boolean existsByCustomer_CustomerIdAndTaxYear(Long customerId, Integer taxYear);

}
