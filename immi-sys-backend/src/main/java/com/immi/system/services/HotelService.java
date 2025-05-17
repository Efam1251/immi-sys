package com.immi.system.services;

import com.immi.system.models.AddressModel;
import com.immi.system.models.HotelModel;
import com.immi.system.repositories.HotelRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HotelService {
    
    @Autowired
    private HotelRepository hotelRepository;

    public HotelModel saveHotel(HotelModel hotel) {
        return hotelRepository.save(hotel);
    }

    public List<HotelModel> getAllHotels() {
        return hotelRepository.findAll();
    }

    public HotelModel getHotelById(Long id) {
        return hotelRepository.findById(id).orElse(null);
    }
    
    public HotelModel getNextHotel(Long id) {
        return hotelRepository.findFirstByIdGreaterThanOrderByIdAsc(id);
    }

    public HotelModel getPreviousHotel(Long id) {
        return hotelRepository.findFirstByIdLessThanOrderByIdDesc(id);
    }

    public HotelModel getFirstHotel() {
        return hotelRepository.findFirstByOrderByIdAsc();
    }

    public HotelModel getLastHotel() {
        return hotelRepository.findFirstByOrderByIdDesc();
    }

    public void deleteHotel(Long id) {
        hotelRepository.deleteById(id);
    }

    public Optional<HotelModel> findByAddress(AddressModel address) {
        return hotelRepository.findByAddress(address);
    }
    
}
