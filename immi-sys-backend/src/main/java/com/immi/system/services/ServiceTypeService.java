package com.immi.system.services;

import com.immi.system.models.ServiceTypeModel;
import com.immi.system.repositories.ServiceTypeRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ServiceTypeService {

    private final ServiceTypeRepository serviceTypeRepository;

    public ServiceTypeService(ServiceTypeRepository serviceTypeRepository) {
        this.serviceTypeRepository = serviceTypeRepository;
    }

    @Transactional
    public ServiceTypeModel createServiceType(ServiceTypeModel serviceType) {
        // Check if the service type already exists by its name (or other unique property)
        if (serviceTypeRepository.findByName(serviceType.getName()) != null) {
            throw new RuntimeException("Service Type already exists");
        }
        // Save and return the created service type
        return serviceTypeRepository.save(serviceType);
    }

    @Transactional
    public ServiceTypeModel updateServiceType(Long id, ServiceTypeModel serviceType) {
        // Find the service type by its ID
        ServiceTypeModel existingServiceType = serviceTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service Type not found"));

        // Ensure that the name is not taken by another service type
        ServiceTypeModel existingByName = serviceTypeRepository.findByName(serviceType.getName());
        if (existingByName != null && !existingByName.getId().equals(id)) {
            throw new RuntimeException("Service Type with this name already exists");
        }

        // Save the updated service type
        return serviceTypeRepository.save(serviceType);
    }

    @Transactional
    public void deleteServiceType(Long id) {
        // Delete the service type by its ID
        serviceTypeRepository.deleteById(id);
    }

    public List<ServiceTypeModel> findAllServiceTypes() {
        return serviceTypeRepository.findAll();
    }

    public ServiceTypeModel getServiceTypeById(Long id) {
        return serviceTypeRepository.findById(id).orElse(null);
    }

    // Method to get the next service type based on the given id
    public ServiceTypeModel getNextServiceType(Long id) {
        Optional<ServiceTypeModel> nextServiceType = serviceTypeRepository.findFirstByIdGreaterThanOrderByIdAsc(id);
        return nextServiceType.orElse(null); // Return null if no next service type found
    }

    // Method to get the previous service type based on the given id
    public ServiceTypeModel getPreviousServiceType(Long id) {
        Optional<ServiceTypeModel> previousServiceType = serviceTypeRepository.findFirstByIdLessThanOrderByIdDesc(id);
        return previousServiceType.orElse(null); // Return null if no previous service type found
    }

    // Method to get the first service type in the list
    public ServiceTypeModel getFirstServiceType() {
        Optional<ServiceTypeModel> firstServiceType = serviceTypeRepository.findFirstByOrderByIdAsc();
        return firstServiceType.orElse(null); // Return null if no first service type found
    }

    // Method to get the last service type in the list
    public ServiceTypeModel getLastServiceType() {
        Optional<ServiceTypeModel> lastServiceType = serviceTypeRepository.findFirstByOrderByIdDesc();
        return lastServiceType.orElse(null); // Return null if no last service type found
    }

    public String saveServiceType(ServiceTypeModel serviceType) {
        if (serviceType.getId() == null || serviceType.getId() == 0) {
            boolean exists = serviceTypeRepository.existsByName(serviceType.getName());
            if (exists) {
                return "This Service Type already exists.";
            } else {
                serviceType.setId(null);
            }
        }
        serviceTypeRepository.save(serviceType);
        return "Service Type saved successfully.";
    }

    public ServiceTypeModel findByName(String name) {
        return serviceTypeRepository.findByName(name);
    }
}
