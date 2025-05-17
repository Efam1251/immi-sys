package com.immi.system.services;

import com.immi.system.DTOs.ServiceRequestDTO;
import com.immi.system.models.CustomerModel;
import com.immi.system.models.ServiceModel;
import com.immi.system.models.ServiceRequestModel;
import com.immi.system.repositories.ServiceRequestRepository;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceRequestService {

    private final ServiceRequestRepository serviceRequestRepository;

    public ServiceRequestService(ServiceRequestRepository serviceRequestRepository) {
        this.serviceRequestRepository = serviceRequestRepository;
    }

    // ========== CREATE ==========
    @Transactional
    public ServiceRequestModel createRecord(ServiceRequestModel request) {
        request.setId(null); // ensure new record
        
        // Generate unique reference code
        String referenceCode = generateReferenceCode(request.getCustomer(), request.getService(), request.getRequestDate());
        request.setReferenceCode(referenceCode);

        return serviceRequestRepository.save(request);
    }

    // ========== UPDATE ==========
    @Transactional
    public ServiceRequestModel updateRecord(Long id, ServiceRequestModel request) {
        return serviceRequestRepository.save(request);
    }

    // ========== DELETE ==========
    @Transactional
    public void deleteRecord(Long id) {
        serviceRequestRepository.deleteById(id);
    }

    // ========== NAVIGATION ==========
    public ServiceRequestModel findById(Long id) {
        return serviceRequestRepository.findById(id).orElse(null);
    }

    public ServiceRequestModel findNext(Long id) {
        return serviceRequestRepository.findFirstByIdGreaterThanOrderByIdAsc(id).orElse(null);
    }

    public ServiceRequestModel findPrevious(Long id) {
        return serviceRequestRepository.findFirstByIdLessThanOrderByIdDesc(id).orElse(null);
    }

    public ServiceRequestModel findFirst() {
        return serviceRequestRepository.findFirstByOrderByIdAsc().orElse(null);
    }

    public ServiceRequestModel findLast() {
        return serviceRequestRepository.findFirstByOrderByIdDesc().orElse(null);
    }

    // ========== LIST ==========
    public List<ServiceRequestModel> findAll() {
        return serviceRequestRepository.findAll();
    }

    public List<ServiceRequestDTO> getAllListRecords() {
        return serviceRequestRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // ========== DTO Mapping ==========
    private ServiceRequestDTO mapToDTO(ServiceRequestModel model) {
        return new ServiceRequestDTO(
                model.getId(),
                model.getService().getServiceId(),
                model.getService().getServiceName(),
                model.getCustomer().getCustomerId(),
                model.getCustomer().getFirstName() + " " + model.getCustomer().getLastName(),
                model.getRequestDate(),
                model.getNotes(),
                model.getStatus(),
                model.getCreatedBy(),
                model.getReferenceCode(),
                model.getCreatedAt(),
                model.getUpdatedAt()
        );
    }

    private String generateReferenceCode(CustomerModel customer, ServiceModel service, LocalDate requestDate) {
        String dateStr = requestDate.format(DateTimeFormatter.BASIC_ISO_DATE); // YYYYMMDD
        Long serviceCode = service.getServiceId(); // e.g., "TVL"
        Long customerId = customer.getCustomerId();

        // Get how many requests exist already for this customer/service/date
        long count = serviceRequestRepository.countByCustomerAndServiceAndRequestDate(customer, service, requestDate);
        String sequence = String.format("%02d", count + 1); // 01, 02, 03...

        return String.format("%s-%s-%d-%s", dateStr, serviceCode, customerId, sequence);
    }

}
