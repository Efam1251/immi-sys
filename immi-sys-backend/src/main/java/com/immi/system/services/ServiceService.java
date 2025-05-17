package com.immi.system.services;

import com.immi.system.DTOs.DropDownDTO;
import com.immi.system.DTOs.ServiceDTO;
import com.immi.system.models.ServiceModel;
import com.immi.system.repositories.ServiceRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ServiceService {
    
    private final ServiceRepository serviceRepository;

    public ServiceService(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }
    
    @Transactional
    public ServiceModel createService(ServiceModel service) {
        // Check if the service already exists by its name (or other unique property)
        if (serviceRepository.findByServiceName(service.getServiceName()) != null) {
            throw new RuntimeException("Service already exists");
        }
        service.setServiceId(null);
        // Save and return the created service as DTO
        return serviceRepository.save(service);
    }

    @Transactional
    public ServiceModel updateService(Long id, ServiceModel service) {
        // Find the service by its ID
        ServiceModel existByID = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found"));

        // Ensure that the name is not taken by another service
        ServiceModel existingService = serviceRepository.findByServiceName(service.getServiceName());
        if (existingService != null && !existingService.getServiceId().equals(id)) {
            throw new RuntimeException("Service with this name already exists");
        }

        // Save the updated service and return as DTO
        return serviceRepository.save(service);
    }

    @Transactional
    public void deleteService(Long id) {
        // Delete the service by its ID
        serviceRepository.deleteById(id);
    }

    public List<ServiceDTO> getAllServices() {
        return serviceRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    public List<ServiceModel> findAll() {
        return serviceRepository.findAll();
    }

    public ServiceModel getServiceById(Long id) {
        return serviceRepository.findById(id).orElse(null);
    }
    
    // Method to get the next service based on the given id
    public ServiceModel getNextService(Long id) {
        Optional<ServiceModel> nextService = serviceRepository.findFirstByServiceIdGreaterThanOrderByServiceIdAsc(id);
        return nextService.orElse(null); // Return null if no next service found
    }

    // Method to get the previous service based on the given id
    public ServiceModel getPreviousService(Long id) {
        Optional<ServiceModel> previousService = serviceRepository.findFirstByServiceIdLessThanOrderByServiceIdDesc(id);
        return previousService.orElse(null); // Return null if no previous service found
    }

    // Method to get the first service in the list
    public ServiceModel getFirstService() {
        Optional<ServiceModel> firstService = serviceRepository.findFirstByOrderByServiceIdAsc();
        return firstService.orElse(null); // Return null if no first service found
    }

    // Method to get the last service in the list
    public ServiceModel getLastService() {
        Optional<ServiceModel> lastService = serviceRepository.findFirstByOrderByServiceIdDesc();
        return lastService.orElse(null); // Return null if no last service found
    }
    public String saveService(ServiceModel service) {
        if (service.getServiceId() == null || service.getServiceId() == 0) {
            boolean exists = serviceRepository.existsByServiceName(service.getServiceName());
            if (exists) {
                return "This Service already exists.";
            } else {
                service.setServiceId(null);
            }
        }
        serviceRepository.save(service);
        return "Service saved successfully.";
    }

    public ServiceModel findByServiceName(String serviceName) {
        return serviceRepository.findByServiceName(serviceName);
    }
    
    private ServiceDTO mapToDTO(ServiceModel service) {
        ServiceDTO dto = new ServiceDTO();
        dto.setServiceId(service.getServiceId());  // Set the service ID
        dto.setServiceName(service.getServiceName());  // Set the service name
        dto.setServiceDescription(service.getServiceDescription());  // Set the description
        dto.setUnitPrice(service.getUnitPrice());  // Set the unit price

        // Check if ServiceType is present (assuming it exists in ServiceModel)
        if (service.getServiceType() != null) {
            DropDownDTO serviceTypeDTO = new DropDownDTO();
            serviceTypeDTO.setId(service.getServiceType().getId());  // Set ServiceType ID
            serviceTypeDTO.setName(service.getServiceType().getName());  // Set ServiceType name
            dto.setServiceType(serviceTypeDTO);  // Set the service type in the DTO
        }

        return dto;
    }
    
}
