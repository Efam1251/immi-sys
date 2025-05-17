package com.immi.system.repositories;

import com.immi.system.models.ApplicationModel;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends JpaRepository<ApplicationModel, Long> {

    Optional<ApplicationModel> findFirstByIdGreaterThanOrderByIdAsc(Long id);

    Optional<ApplicationModel> findFirstByIdLessThanOrderByIdDesc(Long id);

    Optional<ApplicationModel> findFirstByOrderByIdAsc();

    Optional<ApplicationModel> findFirstByOrderByIdDesc();

    ApplicationModel findByReceiptNumber(String receiptNumber);

    public boolean existsByReceiptNumber(String receiptNumber);

}
