package com.immi.system.repositories;

import com.immi.system.models.CountryModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<CountryModel, Long> {

    public CountryModel findByName(String name);

    public CountryModel findFirstByIdGreaterThanOrderByIdAsc(Long id);

    public CountryModel findFirstByIdLessThanOrderByIdDesc(Long id);

    public CountryModel findFirstByOrderByIdAsc();

    public CountryModel findFirstByOrderByIdDesc();
    
}
