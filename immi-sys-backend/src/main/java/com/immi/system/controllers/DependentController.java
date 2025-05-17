package com.immi.system.controllers;

import com.immi.system.DTOs.PetitionDependentDTO;
import com.immi.system.models.CustomerModel;
import com.immi.system.models.DependentTypeModel;
import com.immi.system.models.PetitionDependentModel;
import com.immi.system.services.CustomerService;
import com.immi.system.services.DependentService;
import com.immi.system.services.DependentTypeService;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/immigration/dependent")
public class DependentController {
    
    private final DependentService dependentService;
    private final DependentTypeService dependentTypeService;
    private final CustomerService customerService;

    // Constructor injection
    public DependentController(DependentService dependentService, 
                               DependentTypeService dependentTypeService, 
                               CustomerService customerService) {
        this.dependentService = dependentService;
        this.dependentTypeService = dependentTypeService;
        this.customerService = customerService;
    }
    
    @GetMapping("/dependents")
    public List<PetitionDependentModel> getAllPetitionDependents() {
        return dependentService.getAllDependents();
    }
    
    @GetMapping("/dependentTypes")
    public List<DependentTypeModel> getAllDependentTypes() {
        return dependentService.getAllDependentType();
    }
    
    @PostMapping("/save")
    public ResponseEntity<String> savePetitionDependent(@RequestBody Map<String, Long> requestData) {
        // Extract values from the received request
        Long petitionId = requestData.get("petitionId");
        Long dependentId = requestData.get("dependentId");
        Long dependentTypeId = requestData.get("dependentTypeId");
        
        // Validate received data
        if (petitionId == null || dependentId == null || dependentTypeId == null) {
            return ResponseEntity.badRequest().body("All fields are required.");
        }

        // Fetch the dependent (CustomerModel) based on the dependentId
        CustomerModel dependent = customerService.findById(dependentId);
        if (dependent == null) {
            return ResponseEntity.badRequest().body("Dependent not found.");
        }
        
        // Fetch the dependent type (DependentTypeModel) based on the dependentTypeId
        DependentTypeModel dependentType = dependentTypeService.findById(dependentTypeId);
        if (dependentType == null) {
            return ResponseEntity.badRequest().body("Dependent type not found.");
        }
        
        // Create the PetitionDependentModel object
        PetitionDependentModel petitionDependent = new PetitionDependentModel();
        petitionDependent.setPetitionId(petitionId);  // Set the petition
        petitionDependent.setDependentId(dependent); // Set the dependent (CustomerModel)
        petitionDependent.setDependentTypeId(dependentType); // Set the dependent type (DependentTypeModel)

        // Save the petition dependent relationship
        dependentService.save(petitionDependent);

        return ResponseEntity.ok("Dependent added successfully!");
    }
    
    @GetMapping("/petitionDependent-list")
    public ResponseEntity<List<PetitionDependentDTO>> getDependentsByPetition(@RequestParam("petitionId") Long petitionId) {
        List<PetitionDependentDTO> dependents = dependentService.findByDependentPetitionId(petitionId);
        return ResponseEntity.ok(dependents);
    }
    
    @DeleteMapping("/delete/{petitionId}/{dependentId}")
    public ResponseEntity<String> deleteDependent(@PathVariable Long petitionId, @PathVariable Long dependentId) {
        boolean deleted = dependentService.deletePetitionDependent(petitionId, dependentId);
        if (deleted) {
            return ResponseEntity.ok("Dependent association with petition deleted successfully");
        } else {
            return ResponseEntity.badRequest().body("Failed to delete dependent or dependent does not belong to the petition");
        }
    }

}
