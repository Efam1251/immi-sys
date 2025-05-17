package com.immi.system.repositories;

import com.immi.system.models.UnitOfMeasureModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UnitOfMeasureRepository extends JpaRepository<UnitOfMeasureModel, Long> {

    UnitOfMeasureModel findByName(String name);

    UnitOfMeasureModel findFirstByIdGreaterThanOrderByIdAsc(Long id);

    UnitOfMeasureModel findFirstByIdLessThanOrderByIdDesc(Long id);

    UnitOfMeasureModel findFirstByOrderByIdAsc();

    UnitOfMeasureModel findFirstByOrderByIdDesc();
}
