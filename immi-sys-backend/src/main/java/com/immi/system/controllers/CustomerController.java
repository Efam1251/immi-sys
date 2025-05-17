package com.immi.system.controllers;

import com.immi.system.DTOs.DropDownDTO;
import com.immi.system.DTOs.CustomerDTO;
import com.immi.system.models.CustomerModel;
import com.immi.system.services.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/common/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // ===================== Customer Fetching =====================
    
    @GetMapping("/{id}")
    public ResponseEntity<CustomerModel> getById(@PathVariable Long id) {
        CustomerModel record = customerService.findById(id);
        if (record == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(record, HttpStatus.OK);
    }

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

        CustomerModel record = switch (direction) {
            case "Current" -> customerService.findById(currentId);
            case "Next" -> customerService.findNext(currentId);
            case "Previous" -> customerService.findPrevoius(currentId);
            case "First" -> customerService.findFirst();
            case "Last" -> customerService.findLast();
            default -> customerService.findById(currentId); // fallback to current if no direction provided
        };

        if (record == null) {
            return ResponseEntity.ok(null);
        }

        return ResponseEntity.ok(record);
    }

    @GetMapping("/list")
    public ResponseEntity<List<CustomerDTO>> getAllListRecords() {
        List<CustomerDTO> recordDtos = customerService.getAllListRecords();
        
        return new ResponseEntity<>(recordDtos, HttpStatus.OK);
    }

    @GetMapping("/dropdown")
    public List<DropDownDTO> getDropDown() {
        return customerService.findAll().stream()
            .map(record -> new DropDownDTO(record.getCustomerId(), record.getFirstName() + " " + record.getLastName()))
            .collect(Collectors.toList());
    }
    
    @GetMapping("/all")
    public ResponseEntity<List<CustomerModel>> getAllFullRecords() {
        List<CustomerModel> recordDtos = customerService.findAll();
        
        return new ResponseEntity<>(recordDtos, HttpStatus.OK);
    }
    
    @GetMapping("/droplist")
    public List<CustomerDTO> getDropList() {
        return customerService.findAll().stream()
            .map(record -> new CustomerDTO(
                    record.getCustomerId(),
                    record.getFirstName(),
                    record.getLastName(),
                    record.getPhone(),
                    record.getEmail(),
                    record.getPassportNumber()
                )
            )
            .collect(Collectors.toList());
    }

    // ===================== Customer Creation and Modification =====================

    @PostMapping
    public ResponseEntity<?> createRecord(@RequestBody CustomerModel record) {
        try {
            CustomerModel createdRecord = customerService.createRecord(record);
            return new ResponseEntity<>(createdRecord, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerModel> updateRecord(@PathVariable Long id, @RequestBody CustomerModel record) {
        CustomerModel updatedRecord = customerService.updateRecord(id, record);
        if (updatedRecord == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedRecord, HttpStatus.OK);
    }

    // ===================== Customer Deletion =====================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecord(@PathVariable Long id) {
        customerService.deleteRecord(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
