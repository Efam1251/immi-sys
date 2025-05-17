package com.immi.system.services;

import com.immi.system.DTOs.ApplicationDTO;
import com.immi.system.models.UscisProcessEventModel;
import com.immi.system.models.ApplicationModel;
import com.immi.system.repositories.UscisProcessEventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import com.immi.system.repositories.ApplicationRepository;

@Service
public class ApplicationService {

    private final ApplicationRepository uscisProcessRepository;
    private final UscisProcessEventRepository uscisProcessEventRepository;

    public ApplicationService(ApplicationRepository uscisProcessRepository, UscisProcessEventRepository uscisProcessEventRepository) {
        this.uscisProcessRepository = uscisProcessRepository;
        this.uscisProcessEventRepository = uscisProcessEventRepository;
    }

    @Transactional
    public ApplicationModel createRecord(ApplicationModel uscisProcess) {
        if (uscisProcessRepository.existsByReceiptNumber(uscisProcess.getReceiptNumber())) {
            throw new RuntimeException("USCIS process with this receipt number already exists.");
        }
        uscisProcess.setId(null);
        return uscisProcessRepository.save(uscisProcess);
    }

    @Transactional
    public ApplicationModel updateRecord(Long id, ApplicationModel uscisProcess) {
        ApplicationModel existing = uscisProcessRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("USCIS process not found"));

        if (uscisProcessRepository.existsByReceiptNumber(uscisProcess.getReceiptNumber())
                && !existing.getId().equals(id)) {
            throw new RuntimeException("USCIS process with this receipt number already exists.");
        }

        uscisProcess.setId(id);
        return uscisProcessRepository.save(uscisProcess);
    }

    @Transactional
    public void deleteRecord(Long id) {
        uscisProcessRepository.deleteById(id);
    }

    public List<ApplicationDTO> getAllListRecords() {
        return uscisProcessRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<ApplicationModel> findAll() {
        return uscisProcessRepository.findAll();
    }

    public ApplicationModel findById(Long id) {
        return uscisProcessRepository.findById(id).orElse(null);
    }

    public ApplicationModel findNext(Long id) {
        return uscisProcessRepository.findFirstByIdGreaterThanOrderByIdAsc(id).orElse(null);
    }

    public ApplicationModel findPrevious(Long id) {
        return uscisProcessRepository.findFirstByIdLessThanOrderByIdDesc(id).orElse(null);
    }

    public ApplicationModel findFirst() {
        return uscisProcessRepository.findFirstByOrderByIdAsc().orElse(null);
    }

    public ApplicationModel findLast() {
        return uscisProcessRepository.findFirstByOrderByIdDesc().orElse(null);
    }

    public ApplicationModel findByReceiptNumber(String receiptNumber) {
        return uscisProcessRepository.findByReceiptNumber(receiptNumber);
    }
    
    public ApplicationModel getProcess(Long id) {
        return uscisProcessRepository.findById(id).orElse(null);
    }

    public UscisProcessEventModel addEvent(Long processId, UscisProcessEventModel event) {
        return uscisProcessEventRepository.save(event);
    }

    public List<UscisProcessEventModel> getEventsForProcess(Long processId) {
        return uscisProcessEventRepository.findAllByProcessId(processId);
    }


    private ApplicationDTO mapToDTO(ApplicationModel uscisProcess) {
        ApplicationDTO dto = new ApplicationDTO();

        dto.setId(uscisProcess.getId());

        // Assuming CustomerModel has getFullName() or combine first/last name
        dto.setCustomerName(
                uscisProcess.getCustomer() != null
                ? uscisProcess.getCustomer().getFirstName() + " " + uscisProcess.getCustomer().getLastName()
                : null
        );

        // Assuming FormModel has getFormName()
        dto.setFormTypeName(
                uscisProcess.getFormType() != null
                ? uscisProcess.getFormType().getName()
                : null
        );

        dto.setReceiptNumber(uscisProcess.getReceiptNumber());
        dto.setUscisAccountNumber(uscisProcess.getUscisAccountNumber());
        dto.setApplicationDate(uscisProcess.getApplicationDate());
        dto.setApplicationPayment(uscisProcess.getApplicationPayment());
        dto.setStatus(uscisProcess.getStatus().toString());
        dto.setNotes(uscisProcess.getNotes());

        return dto;
    }

    public boolean existEventByEventId(Long id) {
        return uscisProcessEventRepository.existsById(id);
    }

    public void deleteEventByEventId(Long id) {
        uscisProcessEventRepository.deleteById(id);
    }
}
