package com.immi.system.controllers;

import com.immi.system.DTOs.DropDownDTO;
import com.immi.system.DTOs.ServiceRequestDTO;
import com.immi.system.models.ServiceRequestModel;
import com.immi.system.services.ServiceRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/common/service-requests")
public class ServiceRequestController {

    private final ServiceRequestService serviceRequestService;

    public ServiceRequestController(ServiceRequestService serviceRequestService) {
        this.serviceRequestService = serviceRequestService;
    }

    // ===================== Get by ID =====================

    @GetMapping("/{id}")
    public ResponseEntity<ServiceRequestModel> getById(@PathVariable Long id) {
        ServiceRequestModel record = serviceRequestService.findById(id);
        if (record == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(record, HttpStatus.OK);
    }

    // ===================== Record Navigation =====================

    @GetMapping
    public ResponseEntity<?> getRequested(
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

        ServiceRequestModel record = switch (direction) {
            case "Current" -> serviceRequestService.findById(currentId);
            case "Next" -> serviceRequestService.findNext(currentId);
            case "Previous" -> serviceRequestService.findPrevious(currentId);
            case "First" -> serviceRequestService.findFirst();
            case "Last" -> serviceRequestService.findLast();
            default -> serviceRequestService.findById(currentId);
        };
        if (record == null) {
            return ResponseEntity.ok(null);
        }

        return ResponseEntity.ok(record);
    }

    // ===================== Drop List and Full List =====================

    @GetMapping("/list")
    public ResponseEntity<List<ServiceRequestDTO>> getAllListRecords() {
        List<ServiceRequestDTO> dtos = serviceRequestService.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping("/dropdown")
    public List<DropDownDTO> getDropDown() {
        return serviceRequestService.findAll().stream()
                .map(record -> new DropDownDTO(
                        record.getId(),
                        record.getService().getServiceName() + " - " +
                        record.getCustomer().getFirstName() + " " + record.getCustomer().getLastName()
                ))
                .collect(Collectors.toList());
    }

    @GetMapping("/all")
    public ResponseEntity<List<ServiceRequestModel>> getAllFullRecords() {
        return new ResponseEntity<>(serviceRequestService.findAll(), HttpStatus.OK);
    }

    // ===================== Create & Update =====================

    @PostMapping
    public ResponseEntity<?> createRecord(@RequestBody ServiceRequestModel record) {
        try {
            ServiceRequestModel created = serviceRequestService.createRecord(record);
            return new ResponseEntity<>(toDTO(created), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceRequestModel> updateRecord(@PathVariable Long id, @RequestBody ServiceRequestModel record) {
        ServiceRequestModel updated = serviceRequestService.updateRecord(id, record);
        if (updated == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    // ===================== Delete =====================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecord(@PathVariable Long id) {
        serviceRequestService.deleteRecord(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // ===================== DTO Mapper =====================

    private ServiceRequestDTO toDTO(ServiceRequestModel model) {
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
}
