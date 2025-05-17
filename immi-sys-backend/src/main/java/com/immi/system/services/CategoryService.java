package com.immi.system.services;

import com.immi.system.DTOs.SimpleDTO;
import com.immi.system.models.CategoryModel;
import com.immi.system.repositories.CategoryRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepo;

    public CategoryService(CategoryRepository categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    public SimpleDTO createCategory(SimpleDTO dto) {
        if (categoryRepo.findByName(dto.getName()) != null) {
            throw new RuntimeException("Category already exists");
        }

        CategoryModel category = new CategoryModel();
        category.setName(dto.getName());

        return mapToDTO(categoryRepo.save(category));
    }

    public List<SimpleDTO> getAllCategories() {
        return categoryRepo.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public SimpleDTO getCategoryById(Long id) {
        return mapToDTO(categoryRepo.findById(id).orElseThrow(() -> new RuntimeException("Category not found")));
    }

    public SimpleDTO getNextCategory(Long id) {
        return mapToDTO(categoryRepo.findFirstByIdGreaterThanOrderByIdAsc(id));
    }

    public SimpleDTO getPreviousCategory(Long id) {
        return mapToDTO(categoryRepo.findFirstByIdLessThanOrderByIdDesc(id));
    }

    public SimpleDTO getFirstCategory() {
        return mapToDTO(categoryRepo.findFirstByOrderByIdAsc());
    }

    public SimpleDTO getLastCategory() {
        return mapToDTO(categoryRepo.findFirstByOrderByIdDesc());
    }

    public SimpleDTO updateCategory(Long id, SimpleDTO dto) {
        CategoryModel category = categoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        category.setName(dto.getName());

        return mapToDTO(categoryRepo.save(category));
    }

    public void deleteCategory(Long id) {
        categoryRepo.deleteById(id);
    }

    private SimpleDTO mapToDTO(CategoryModel category) {
        SimpleDTO dto = new SimpleDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        return dto;
    }
}
