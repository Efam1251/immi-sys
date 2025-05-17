package com.immi.system.repositories;

import com.immi.system.models.VisaTypeModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisaTypeRepository extends JpaRepository<VisaTypeModel, Long> {

    public VisaTypeModel findByName(String name);

    public VisaTypeModel findFirstByIdGreaterThanOrderByIdAsc(Long id);

    public VisaTypeModel findFirstByIdLessThanOrderByIdDesc(Long id);

    public VisaTypeModel findFirstByOrderByIdAsc();

    public VisaTypeModel findFirstByOrderByIdDesc();
    
}
