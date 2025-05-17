package com.immi.system.controllers;

import com.immi.system.DTOs.DropDownDTO;
import com.immi.system.models.ServiceTypeModel;
import com.immi.system.services.ServiceTypeService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/common/serviceTypes")
public class ServiceTypeController {

    private static final Logger logger = LoggerFactory.getLogger(StateController.class);

    private final ServiceTypeService serviceTypeService;

    public ServiceTypeController(ServiceTypeService serviceTypeService) {
        this.serviceTypeService = serviceTypeService;
    }

    @GetMapping
    public ResponseEntity<?> getRequestedServiceType(
            @RequestParam("id") String id,
            @RequestParam(required = false) String direction) {

        if (id == null || id.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("message", "ID parameter is required"));
        }

        Long currentId;
        try {
            currentId = Long.valueOf(id);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid ID format"));
        }

        ServiceTypeModel serviceType = switch (direction) {
            case "Current" ->
                serviceTypeService.getServiceTypeById(currentId);
            case "Next" ->
                serviceTypeService.getNextServiceType(currentId);
            case "Previous" ->
                serviceTypeService.getPreviousServiceType(currentId);
            case "First" ->
                serviceTypeService.getFirstServiceType();
            case "Last" ->
                serviceTypeService.getLastServiceType();
            default ->
                serviceTypeService.getServiceTypeById(currentId); // fallback to current if no direction provided
        };

        if (serviceType == null) {
            return ResponseEntity.ok(null);
        }

        return ResponseEntity.ok(serviceType);
    }

    @GetMapping("/list")
    public ResponseEntity<List<ServiceTypeModel>> getAllServiceTypes() {
        List<ServiceTypeModel> serviceTypes = serviceTypeService.findAllServiceTypes();
        return new ResponseEntity<>(serviceTypes, HttpStatus.OK);
    }

    @GetMapping("/dropdown")
    @ResponseBody
    public List<DropDownDTO> getAllServiceTypesForDropdown() {
        return serviceTypeService.findAllServiceTypes().stream()
                .map(serviceType -> new DropDownDTO(serviceType.getId(), serviceType.getName()))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceTypeModel> getServiceTypeById(@PathVariable Long id) {
        ServiceTypeModel serviceType = serviceTypeService.getServiceTypeById(id);
        if (serviceType == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(serviceType, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createServiceType(@RequestBody ServiceTypeModel serviceType) {
        logger.info("Creating a new service type: {}", serviceType);
        try {
            ServiceTypeModel createdServiceType = serviceTypeService.createServiceType(serviceType);
            return new ResponseEntity<>(createdServiceType, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceTypeModel> updateServiceType(@PathVariable Long id, @RequestBody ServiceTypeModel serviceType) {
        logger.info("Updating service type with ID: {}", id);
        ServiceTypeModel updatedServiceType = serviceTypeService.updateServiceType(id, serviceType);
        if (updatedServiceType == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedServiceType, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServiceType(@PathVariable Long id) {
        logger.warn("Deleting service type with ID: {}", id);
        serviceTypeService.deleteServiceType(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
