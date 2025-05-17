package com.immi.system.services;

import com.immi.system.DTOs.AddressDTO;
import com.immi.system.DTOs.StateDTO;
import com.immi.system.models.AddressModel;
import com.immi.system.models.CountryModel;
import com.immi.system.models.StateModel;
import com.immi.system.repositories.AddressRepository;
import com.immi.system.repositories.CountryRepository;
import com.immi.system.repositories.StateRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for managing Address, Country, and State related operations.
 * Provides CRUD operations and some custom queries related to addresses, countries, and states.
 * This service interacts with the corresponding repositories to fetch, save, and manage data in the database.
 */
@Service
public class AddressService {
    
    // Address Service - Handling address-related operations
    @Autowired
    private AddressRepository addressRepository;
    
    @Autowired
    private StateRepository stateRepo;
    
    @Autowired
    private CountryRepository countryRepo;
    
    /** Fetches all addresses from the database.
     * @return List of AddressModel containing all the addresses in the database. */
    public List<AddressModel> findAllAddresses() {
        return addressRepository.findAll();
    }
    /** Saves or updates an address in the database.
     * @param dto
     * @return  */
    public AddressDTO saveAddress(AddressDTO dto) {
        
        StateModel state = stateRepo.findById(dto.getStateId())
                .orElseThrow(() -> new RuntimeException("Country not found"));
        
        CountryModel country = countryRepo.findById(dto.getCountryId())
                .orElseThrow(() -> new RuntimeException("Country not found"));
        
        AddressModel address = new AddressModel();
        address.setStreet(dto.getStreet());
        address.setCity(dto.getCity());
        address.setState(state);
        address.setZipCode(dto.getZipCode());
        address.setCountry(country);

        return mapToDTO(addressRepository.save(address));
    }
    
    /****************************************************/
    
    // Country Service - Handling country-related operations
    @Autowired
    private CountryRepository countryRepository;
    
    /** Fetches all countries from the database.
     * @return List of CountryModel containing all the countries in the database. */
    public List<CountryModel> getAllCountries() {
        return countryRepository.findAll();
    }
    /** Saves or updates a country in the database.
     * @param country the CountryModel object to be saved or updated. */
    public void saveCountry(CountryModel country) {
        countryRepository.save(country);
    }
    /** Finds a country by its name from the database.
     * @param name the name of the country to search for.
     * @return the CountryModel object if found, otherwise null. */
    public CountryModel findCountryByName(String name) {
        return countryRepository.findByName(name);
    }
    
    /****************************************************/
    
    // State Service - Handling state-related operations
    
    @Autowired
    private StateRepository stateRepository;
    
    /** Fetches all states from the database.
     * @return List of StateModel containing all the states in the database. */
    public List<StateModel> getAllStates() {
        return stateRepository.findAll();
    }
    /** Finds a state by its name from the database.
     * @param name the name of the state to search for.
     * @return the StateModel object if found, otherwise null. */
    public StateModel findStateByName(String name) {
        return stateRepository.findByName(name);
    }
    /** Saves or updates a state in the database.
     * @param state the StateModel object to be saved or updated. */
    public void saveState(StateModel state) {
        stateRepository.save(state);
    }
    
    private AddressDTO mapToDTO(AddressModel address) {
        AddressDTO dto = new AddressDTO();
        dto.setId(address.getId());
        dto.setStreet(address.getStreet());
        dto.setCity(address.getCity());
        dto.setStateId(address.getState().getId());
        dto.setZipCode(address.getZipCode());
        dto.setCountryId(address.getCountry().getId());
        return dto;
    }
    
}
