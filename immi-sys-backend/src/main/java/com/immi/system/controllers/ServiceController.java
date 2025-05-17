package com.immi.system.controllers;

import com.immi.system.DTOs.DropDownDTO;
import com.immi.system.DTOs.ServiceDTO;
import com.immi.system.models.ServiceModel;
import com.immi.system.services.ServiceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/common/services")
public class ServiceController {

    private static final Logger logger = LoggerFactory.getLogger(StateController.class);

    private final ServiceService serviceService;

    public ServiceController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    // ===================== Service Fetching =====================
    
    @GetMapping("/{id}")
    public ResponseEntity<ServiceModel> getServiceById(@PathVariable Long id) {
        ServiceModel service = serviceService.getServiceById(id);
        if (service == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(service, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getRequestedService(
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

        ServiceModel service = switch (direction) {
            case "Current" -> serviceService.getServiceById(currentId);
            case "Next" -> serviceService.getNextService(currentId);
            case "Previous" -> serviceService.getPreviousService(currentId);
            case "First" -> serviceService.getFirstService();
            case "Last" -> serviceService.getLastService();
            default -> serviceService.getServiceById(currentId); // fallback to current if no direction provided
        };

        if (service == null) {
            return ResponseEntity.ok(null);
        }

        return ResponseEntity.ok(service);
    }
    
    @GetMapping("/list")
    public ResponseEntity<List<ServiceDTO>> getAllServices() {
        List<ServiceDTO> serviceDTOs = serviceService.getAllServices();

        // Print each service in the list
        //serviceDTOs.forEach(service -> System.out.println(service));

        return new ResponseEntity<>(serviceDTOs, HttpStatus.OK);
    }

    @GetMapping("/dropdown")
    @ResponseBody
    public List<DropDownDTO> getDropDownServices() {
        return serviceService.findAll().stream()
            .map(service -> new DropDownDTO(service.getServiceId(), service.getServiceName()))
            .collect(Collectors.toList());
    }
    
    @GetMapping("/all")
    public List<ServiceModel> getAllRecords() {
        List<ServiceModel> records = serviceService.findAll();
        return records;
    }

    // ===================== Service Creation and Modification =====================

    @PostMapping
    public ResponseEntity<?> createService(@RequestBody ServiceModel service) {
        logger.info("Creating a new service: {}", service);
        try {
            ServiceModel createdService = serviceService.createService(service);
            return new ResponseEntity<>(createdService, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceModel> updateService(@PathVariable Long id, @RequestBody ServiceModel service) {
        logger.info("Updating service with ID: {}", id);
        ServiceModel updatedService = serviceService.updateService(id, service);
        if (updatedService == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedService, HttpStatus.OK);
    }

    // ===================== Service Deletion =====================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        logger.warn("Deleting service with ID: {}", id);
        serviceService.deleteService(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
