package com.immi.system.services;

import com.immi.system.DTOs.CitizenshipProcessDTO;
import com.immi.system.models.CitizenshipProcessModel;
import com.immi.system.repositories.CitizenshipProcessRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class CitizenshipProcessService {

    private final CitizenshipProcessRepository citizenshipRepository;

    public CitizenshipProcessService(CitizenshipProcessRepository citizenshipRepository) {
        this.citizenshipRepository = citizenshipRepository;
    }

    @Transactional
    public CitizenshipProcessModel createRecord(CitizenshipProcessModel record) {
        if (citizenshipRepository.existsByReceiptNumber(record.getReceiptNumber())) {
            throw new RuntimeException("Citizenship process with this receipt number already exists.");
        }

        record.setCitizenshipId(null); // Ensure auto-generation
        return citizenshipRepository.save(record);
    }

    @Transactional
    public CitizenshipProcessModel updateRecord(Long id, CitizenshipProcessModel updatedRecord) {
        CitizenshipProcessModel existing = citizenshipRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Citizenship process not found"));

        if (citizenshipRepository.existsByReceiptNumber(updatedRecord.getReceiptNumber()) &&
            !existing.getCitizenshipId().equals(id)) {
            throw new RuntimeException("Another record with this receipt number already exists.");
        }

        updatedRecord.setCitizenshipId(id); // Maintain the same ID
        return citizenshipRepository.save(updatedRecord);
    }

    @Transactional
    public void deleteRecord(Long id) {
        citizenshipRepository.deleteById(id);
    }

    public CitizenshipProcessModel findById(Long id) {
        return citizenshipRepository.findById(id).orElse(null);
    }

    public List<CitizenshipProcessModel> findAll() {
        return citizenshipRepository.findAll();
    }

    public List<CitizenshipProcessDTO> getAllListRecords() {
        return citizenshipRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public CitizenshipProcessModel findNext(Long id) {
        return citizenshipRepository.findFirstByCitizenshipIdGreaterThanOrderByCitizenshipIdAsc(id)
                .orElse(null);
    }

    public CitizenshipProcessModel findPrevious(Long id) {
        return citizenshipRepository.findFirstByCitizenshipIdLessThanOrderByCitizenshipIdDesc(id)
                .orElse(null);
    }

    public CitizenshipProcessModel findFirst() {
        return citizenshipRepository.findFirstByOrderByCitizenshipIdAsc()
                .orElse(null);
    }

    public CitizenshipProcessModel findLast() {
        return citizenshipRepository.findFirstByOrderByCitizenshipIdDesc()
                .orElse(null);
    }

    public CitizenshipProcessModel findByReceiptNumber(String receiptNumber) {
        return citizenshipRepository.findByReceiptNumber(receiptNumber);
    }

    private CitizenshipProcessDTO mapToDTO(CitizenshipProcessModel model) {
        CitizenshipProcessDTO dto = new CitizenshipProcessDTO();

        dto.setCitizenshipId(model.getCitizenshipId());

        if (model.getCustomer() != null) {
            dto.setCustomerId(model.getCustomer().getCustomerId()); // make sure getCustomerId() exists
            dto.setCustomerName(model.getCustomer().getFirstName()+" "+model.getCustomer().getLastName());
        }

        dto.setReceiptNumber(model.getReceiptNumber());
        dto.setUscisAccountNumber(model.getUscisAccountNumber());
        dto.setApplicationDate(model.getApplicationDate());
        dto.setApplicationPayment(model.getApplicationPayment());
        dto.setBiometricAppointmentDate(model.getBiometricAppointmentDate());
        dto.setInterviewDate(model.getInterviewDate());
        dto.setOathCeremonyDate(model.getOathCeremonyDate());
        dto.setLocation(model.getLocation());
        dto.setCurrentStatus(model.getCurrentStatus());
        dto.setCitizenshipNotes(model.getCitizenshipNotes());

        return dto;
    }

}
