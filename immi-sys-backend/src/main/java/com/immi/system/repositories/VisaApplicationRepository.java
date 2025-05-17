package com.immi.system.repositories;

import com.immi.system.models.VisaApplicationModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VisaApplicationRepository extends JpaRepository<VisaApplicationModel, Long> {

    // Check if a visa application with a given reference exists
    public boolean existsByReference(String reference);

    // Find the next visa application based on id (greater than the current id)
    public Optional<VisaApplicationModel> findFirstByIdGreaterThanOrderByIdAsc(Long id);

    // Find the previous visa application based on id (less than the current id)
    public Optional<VisaApplicationModel> findFirstByIdLessThanOrderByIdDesc(Long id);

    // Find the first visa application ordered by id ascending
    public Optional<VisaApplicationModel> findFirstByOrderByIdAsc();

    // Find the last visa application ordered by id descending
    public Optional<VisaApplicationModel> findFirstByOrderByIdDesc();

    // Find a visa application by reference number
    public Optional<VisaApplicationModel> findByReference(String reference);

    // Find all visa applications related to a specific customer
    @Override
    public Optional<VisaApplicationModel> findById(Long id);

}
