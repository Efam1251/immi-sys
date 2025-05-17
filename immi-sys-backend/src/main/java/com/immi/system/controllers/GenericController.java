package com.immi.system.controllers;

import com.immi.system.DTOs.DropDownDTO;
import com.immi.system.models.CategoryModel;
import com.immi.system.models.StatusModel;
import com.immi.system.models.VisaTypeModel;
import com.immi.system.services.GenericService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/generic")
public class GenericController {
    
    // Endpoint to fetch the list of status in JSON format
    @Autowired
    private GenericService categoryService;

    @GetMapping("/category")
    @ResponseBody
    public List<DropDownDTO> getAllCategories() {
        return categoryService.getAllCategories().stream()
            .map(category -> new DropDownDTO(category.getId(), category.getName()))
            .collect(Collectors.toList());
    }

    @PostMapping("/category-save")
    public ResponseEntity<Map<String, String>> saveCategory(@RequestBody CategoryModel category) {
        Map<String, String> response = new HashMap<>();
        // Check if a status with the same name already exists
        CategoryModel existingCategory = categoryService.findCategoryByName(category.getName());

        if (existingCategory != null) {
            // Return a conflict status with a JSON response
            response.put("message", "Status already exists!");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        try {
            // Save the new status if it doesn't exist
            categoryService.saveCategory(category);
            // Return a success message as JSON
            response.put("message", "Status added successfully!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Handle any exceptions that occur during saving
            response.put("message", "An error occurred while saving the status.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
// Endpoint to fetch the list of visatypes in JSON format
    @Autowired
    private GenericService visaTypeService;

    @GetMapping("/visaTypes")
    @ResponseBody
    public List<DropDownDTO> getAllVisaTypes() {
        return visaTypeService.getAllVisaTypes().stream()
            .map(visaType -> new DropDownDTO(visaType.getId(), visaType.getName()))
            .collect(Collectors.toList());
    }

    @PostMapping("/visatype-save")
    public ResponseEntity<Map<String, String>> saveVisatype(@RequestBody VisaTypeModel visatype) {
        Map<String, String> response = new HashMap<>();
        // Check if a visatype with the same name already exists
        VisaTypeModel existingVisatype = visaTypeService.findVisaTypeByName(visatype.getName());

        if (existingVisatype != null) {
            // Return a conflict status with a JSON response
            response.put("message", "Visatype already exists!");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        try {
            // Save the new visatype if it doesn't exist
            visaTypeService.saveVisaType(visatype);
            // Return a success message as JSON
            response.put("message", "Visatype added successfully!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Handle any exceptions that occur during saving
            response.put("message", "An error occurred while saving the visatype.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

// Endpoint to fetch the list of status in JSON format
    @Autowired
    private GenericService statusService;

    @GetMapping("/statuses")
    @ResponseBody
    public List<DropDownDTO> getAllStatuses() {
        return statusService.getAllStatuses().stream()
            .map(status -> new DropDownDTO(status.getId(), status.getName()))
            .collect(Collectors.toList());
    }

    @PostMapping("/status-save")
    public ResponseEntity<Map<String, String>> saveStatus(@RequestBody StatusModel status) {
        Map<String, String> response = new HashMap<>();
        // Check if a status with the same name already exists
        StatusModel existingStatus = statusService.findStatusByName(status.getName());

        if (existingStatus != null) {
            // Return a conflict status with a JSON response
            response.put("message", "Status already exists!");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        try {
            // Save the new status if it doesn't exist
            statusService.saveStatus(status);
            // Return a success message as JSON
            response.put("message", "Status added successfully!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Handle any exceptions that occur during saving
            response.put("message", "An error occurred while saving the status.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
}
