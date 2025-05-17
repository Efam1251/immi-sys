package com.immi.system.services;

import com.immi.system.DTOs.VisaApplicationDTO;
import com.immi.system.models.VisaApplicationModel;
import com.immi.system.repositories.VisaApplicationRepository;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VisaApplicationService {

    private final VisaApplicationRepository visaApplicationRepository;

    public VisaApplicationService(VisaApplicationRepository visaApplicationRepository) {
        this.visaApplicationRepository = visaApplicationRepository;
    }

    @Transactional
    public VisaApplicationModel createRecord(VisaApplicationModel visaApplication) {
        // Ensure the visa application does not already exist (for example, based on reference number)
        if (visaApplicationRepository.existsByReference(visaApplication.getReference())) {
            throw new RuntimeException("Visa application with this reference already exists.");
        }
        visaApplication.setId(null);
        // Save and return the created visa application
        return visaApplicationRepository.save(visaApplication);
    }

    @Transactional
    public VisaApplicationModel updateRecord(Long id, VisaApplicationModel visaApplication) {
        // Find the existing visa application by ID
        VisaApplicationModel existingVisaApplication = visaApplicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Visa application not found"));

        // Ensure the reference is not taken by another visa application
        if (visaApplicationRepository.existsByReference(visaApplication.getReference()) && 
            !existingVisaApplication.getId().equals(id)) {
            throw new RuntimeException("Visa application with this reference already exists.");
        }

        // Save the updated visa application and return it
        visaApplication.setId(id);  // Ensure the ID is maintained
        return visaApplicationRepository.save(visaApplication);
    }

    @Transactional
    public void deleteRecord(Long id) {
        // Delete the visa application by ID
        visaApplicationRepository.deleteById(id);
    }

    public List<VisaApplicationDTO> getAllListRecords() {
        return visaApplicationRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<VisaApplicationModel> findAll() {
        return visaApplicationRepository.findAll();
    }

    public VisaApplicationModel findById(Long id) {
        return visaApplicationRepository.findById(id).orElse(null);
    }

    // Method to get the next visa application based on the given ID
    public VisaApplicationModel findNext(Long id) {
        Optional<VisaApplicationModel> nextVisaApplication = visaApplicationRepository.findFirstByIdGreaterThanOrderByIdAsc(id);
        return nextVisaApplication.orElse(null); // Return null if no next visa application found
    }

    // Method to get the previous visa application based on the given ID
    public VisaApplicationModel findPrevious(Long id) {
        Optional<VisaApplicationModel> previousVisaApplication = visaApplicationRepository.findFirstByIdLessThanOrderByIdDesc(id);
        return previousVisaApplication.orElse(null); // Return null if no previous visa application found
    }

    // Method to get the first visa application in the list
    public VisaApplicationModel findFirst() {
        Optional<VisaApplicationModel> firstVisaApplication = visaApplicationRepository.findFirstByOrderByIdAsc();
        return firstVisaApplication.orElse(null); // Return null if no first visa application found
    }

    // Method to get the last visa application in the list
    public VisaApplicationModel findLast() {
        Optional<VisaApplicationModel> lastVisaApplication = visaApplicationRepository.findFirstByOrderByIdDesc();
        return lastVisaApplication.orElse(null); // Return null if no last visa application found
    }

    private VisaApplicationDTO mapToDTO(VisaApplicationModel visaApplication) {
        VisaApplicationDTO dto = new VisaApplicationDTO();
        dto.setId(visaApplication.getId());
        String customerName = visaApplication.getCustomer().getFirstName() + " " + visaApplication.getCustomer().getLastName();
        dto.setCustomerName(customerName);
        dto.setVisaType(visaApplication.getVisaType().getName());
        dto.setCountry(visaApplication.getCountry().getName());
        dto.setReference(visaApplication.getReference());
        dto.setSubmissionDate(visaApplication.getSubmissionDate());
        dto.setStatus(visaApplication.getStatus() != null ? visaApplication.getStatus().name() : null);

        return dto;
    }

}
