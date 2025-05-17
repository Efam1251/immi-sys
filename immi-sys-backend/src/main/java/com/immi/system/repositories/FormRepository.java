package com.immi.system.repositories;

import com.immi.system.models.FormModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FormRepository extends JpaRepository<FormModel, Long> {

    // Find a form by name
    Optional<FormModel> findByName(String name);

    // Check if a form with the given name and location exists
    boolean existsByNameAndLocation(String name, String location);

    // Find the next form based on ID (greater than current)
    Optional<FormModel> findFirstByIdGreaterThanOrderByIdAsc(Long id);

    // Find the previous form based on ID (less than current)
    Optional<FormModel> findFirstByIdLessThanOrderByIdDesc(Long id);

    // Get the first form ordered by ID ascending
    Optional<FormModel> findFirstByOrderByIdAsc();

    // Get the last form ordered by ID descending
    Optional<FormModel> findFirstByOrderByIdDesc();
}
