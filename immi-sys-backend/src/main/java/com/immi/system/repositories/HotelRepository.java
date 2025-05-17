package com.immi.system.repositories;

import com.immi.system.models.AddressModel;
import com.immi.system.models.HotelModel;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelRepository extends JpaRepository<HotelModel, Long> {

    public HotelModel findFirstByIdGreaterThanOrderByIdAsc(Long id);

    public HotelModel findFirstByIdLessThanOrderByIdDesc(Long id);

    public HotelModel findFirstByOrderByIdAsc();

    public HotelModel findFirstByOrderByIdDesc();
    
    // Method to find hotel by address
    Optional<HotelModel> findByAddress(AddressModel address);
}
