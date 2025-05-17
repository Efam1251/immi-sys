package com.immi.system.services;

import com.immi.system.DTOs.TaxFilingDTO;
import com.immi.system.models.TaxFilingModel;
import com.immi.system.repositories.TaxFilingRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class TaxFilingService {

    
    private final TaxFilingRepository taxFilingRepository;

    public TaxFilingService(TaxFilingRepository taxFilingRepository) {
        this.taxFilingRepository = taxFilingRepository;
    }

    @Transactional
    public TaxFilingModel createRecord(TaxFilingModel taxFiling) {
        Long customerId = taxFiling.getCustomer().getCustomerId();
        Integer taxYear = taxFiling.getTaxYear();

        if (taxFilingRepository.existsByCustomer_CustomerIdAndTaxYear(customerId, taxYear)) {
            throw new RuntimeException("Tax filing for this customer and tax year already exists.");
        }
        taxFiling.setTaxFilingId(null); // Let Hibernate generate it

        return taxFilingRepository.save(taxFiling);
    }
    
    @Transactional
    public TaxFilingModel updateRecord(Long id, TaxFilingModel taxFiling) {
        TaxFilingModel existing = taxFilingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tax filing not found."));

        // Check for duplicate record for the same customer and year
        Optional<TaxFilingModel> duplicate = taxFilingRepository.findByCustomerAndTaxYear(
                taxFiling.getCustomer(), taxFiling.getTaxYear());

        // If a different record (not this one) already exists with the same customer + year, reject it
        if (duplicate.isPresent() && !duplicate.get().getTaxFilingId().equals(id)) {
            throw new RuntimeException("A tax filing already exists for this customer and year.");
        }

        // Copy only allowed fields (optional but safer than trusting full object from client)
        existing.setDate(taxFiling.getDate());
        existing.setCustomer(taxFiling.getCustomer()); // Only if editable
        existing.setTaxYear(taxFiling.getTaxYear());   // Only if editable
        existing.setFilingDate(taxFiling.getFilingDate());
        existing.setFilingStatus(taxFiling.getFilingStatus());
        existing.setNumberOfDependents(taxFiling.getNumberOfDependents());
        existing.setTotalIncome(taxFiling.getTotalIncome());
        existing.setTaxOutcomeAmount(taxFiling.getTaxOutcomeAmount());
        existing.setPreparedBy(taxFiling.getPreparedBy());
        existing.setStatus(taxFiling.getStatus());
        existing.setNotes(taxFiling.getNotes());

        return taxFilingRepository.save(existing);
    }

    @Transactional
    public void deleteRecord(Long id) {
        taxFilingRepository.deleteById(id);
    }

    public List<TaxFilingDTO> getAllListRecords() {
        return taxFilingRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<TaxFilingModel> findAll() {
        return taxFilingRepository.findAll();
    }

    public TaxFilingModel findById(Long id) {
        return taxFilingRepository.findById(id).orElse(null);
    }

    public TaxFilingModel findNext(Long id) {
        return taxFilingRepository.findFirstByTaxFilingIdGreaterThanOrderByTaxFilingIdAsc(id).orElse(null);
    }

    public TaxFilingModel findPrevious(Long id) {
        return taxFilingRepository.findFirstByTaxFilingIdLessThanOrderByTaxFilingIdDesc(id).orElse(null);
    }

    public TaxFilingModel findFirst() {
        return taxFilingRepository.findFirstByOrderByTaxFilingIdAsc().orElse(null);
    }

    public TaxFilingModel findLast() {
        return taxFilingRepository.findFirstByOrderByTaxFilingIdDesc().orElse(null);
    }

    private TaxFilingDTO mapToDTO(TaxFilingModel taxFiling) {
        TaxFilingDTO dto = new TaxFilingDTO();
        dto.setTaxFilingId(taxFiling.getTaxFilingId());
        String clientName = taxFiling.getCustomer().getFirstName() + " " + taxFiling.getCustomer().getLastName();
        dto.setCustomerName(clientName);
        dto.setTaxYear(taxFiling.getTaxYear());
        dto.setFilingDate(taxFiling.getFilingDate());
        dto.setTotalIncome(taxFiling.getTotalIncome());
        dto.setTaxOutcomeAmount(taxFiling.getTaxOutcomeAmount());
        dto.setStatus(taxFiling.getStatus() != null ? taxFiling.getStatus().name() : null);
        return dto;
    }

}
