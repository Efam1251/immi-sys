package com.immi.system.repositories;

import com.immi.system.models.CitizenshipProcessModel;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CitizenshipProcessRepository extends JpaRepository<CitizenshipProcessModel, Long> {

    // Find the next record based on citizenshipId (greater than current)
    Optional<CitizenshipProcessModel> findFirstByCitizenshipIdGreaterThanOrderByCitizenshipIdAsc(Long citizenshipId);

    // Find the previous record based on citizenshipId (less than current)
    Optional<CitizenshipProcessModel> findFirstByCitizenshipIdLessThanOrderByCitizenshipIdDesc(Long citizenshipId);

    // Find the first record by ascending ID
    Optional<CitizenshipProcessModel> findFirstByOrderByCitizenshipIdAsc();

    // Find the last record by descending ID
    Optional<CitizenshipProcessModel> findFirstByOrderByCitizenshipIdDesc();

    // Check if a record with the same receipt number exists
    boolean existsByReceiptNumber(String receiptNumber);

    // Find a record by receipt number
    CitizenshipProcessModel findByReceiptNumber(String receiptNumber);
}
