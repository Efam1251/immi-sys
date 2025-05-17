package com.immi.system.repositories;

import com.immi.system.models.UscisOfficeModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UscisOfficeRepository extends JpaRepository<UscisOfficeModel, Long> {

    public UscisOfficeModel findByName(String name);

    public UscisOfficeModel findFirstByIdGreaterThanOrderByIdAsc(Long id);

    public UscisOfficeModel findFirstByIdLessThanOrderByIdDesc(Long id);

    public UscisOfficeModel findFirstByOrderByIdAsc();

    public UscisOfficeModel findFirstByOrderByIdDesc();

}
