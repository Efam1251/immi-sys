package com.immi.system.controllers;

import com.immi.system.DTOs.SimpleDTO;
import com.immi.system.services.CategoryService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/common/categories")
public class CategoryController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<?> getRequestedCategory(
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
        SimpleDTO category = switch (direction) {
            case "Current" -> categoryService.getCategoryById(currentId);
            case "Next" -> categoryService.getNextCategory(currentId);
            case "Previous" -> categoryService.getPreviousCategory(currentId);
            case "First" -> categoryService.getFirstCategory();
            case "Last" -> categoryService.getLastCategory();
            default -> null;
        };
        if (category == null) {
            return ResponseEntity.ok(null);
        }
        return ResponseEntity.ok(category);
    }

    @GetMapping("/list")
    public ResponseEntity<List<SimpleDTO>> getAllCategories() {
        List<SimpleDTO> categories = categoryService.getAllCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SimpleDTO> getCategoryById(@PathVariable Long id) {
        SimpleDTO category = categoryService.getCategoryById(id);
        if (category == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody SimpleDTO dto) {
        logger.info("Creating a new category: {}", dto);
        try {
            SimpleDTO createdCategory = categoryService.createCategory(dto);
            return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<SimpleDTO> updateCategory(@PathVariable Long id, @RequestBody SimpleDTO dto) {
        SimpleDTO updatedCategory = categoryService.updateCategory(id, dto);
        if (updatedCategory == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
