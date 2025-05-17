package com.immi.system.repositories;

import com.immi.system.models.CategoryModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<CategoryModel, Long> {

    public CategoryModel findByName(String name);

    public CategoryModel findFirstByIdGreaterThanOrderByIdAsc(Long id);

    public CategoryModel findFirstByIdLessThanOrderByIdDesc(Long id);

    public CategoryModel findFirstByOrderByIdAsc();

    public CategoryModel findFirstByOrderByIdDesc();

}
