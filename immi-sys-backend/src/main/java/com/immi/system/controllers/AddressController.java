package com.immi.system.controllers;

import com.immi.system.DTOs.AddressDTO;
import com.immi.system.DTOs.DropDownDTO;
import com.immi.system.models.AddressModel;
import com.immi.system.models.CountryModel;
import com.immi.system.models.StateModel;
import com.immi.system.services.AddressService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/travel/api/address")
public class AddressController {
    
    // Endpoint to fetch the list of states in JSON format
    @Autowired
    private AddressService stateService;
    
    @Autowired
    private AddressService addressService;

    // Endpoint to fetch the list of addresses in JSON format
    @GetMapping("/addresses")
    @ResponseBody
    public List<AddressModel> getListAddresses() {
        return addressService.findAllAddresses();
    }

    @PostMapping("/address-save")
    public AddressDTO createState(@RequestBody AddressDTO dto) {
        System.out.println("Data:" + dto);
        return addressService.saveAddress(dto);
    }
    
    // Endpoint to fetch the list of states in JSON format
    @GetMapping("/states")
    @ResponseBody
    public List<DropDownDTO> getListStates() {
        return stateService.getAllStates().stream()
            .map(state -> new DropDownDTO(state.getId(), state.getName()))
            .collect(Collectors.toList());
    }

    @PostMapping("/state-save")
    public ResponseEntity<Map<String, String>> saveState(@RequestBody StateModel state) {
        Map<String, String> response = new HashMap<>();
        // Check if a state with the same name already exists
        StateModel existingState = stateService.findStateByName(state.getName());

        if (existingState != null) {
            // Return a conflict status with a JSON response
            response.put("message", "State already exists!");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        try {
            // Save the new state if it doesn't exist
            stateService.saveState(state);
            // Return a success message as JSON
            response.put("message", "State added successfully!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Handle any exceptions that occur during saving
            response.put("message", "An error occurred while saving the state.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    // Endpoint to fetch the list of countries in JSON format
    @Autowired
    private AddressService countryService;
    
    @GetMapping("/countries")
    @ResponseBody
    public List<DropDownDTO> getListCountries() {
        return countryService.getAllCountries().stream()
            .map(country -> new DropDownDTO(country.getId(), country.getName()))
            .collect(Collectors.toList());
    }
    
    @PostMapping("/country-save")
    public ResponseEntity<Map<String, String>> saveCountry(@RequestBody CountryModel country) {
        Map<String, String> response = new HashMap<>();
        // Check if a country with the same name already exists
        CountryModel existingCountry = countryService.findCountryByName(country.getName());

        if (existingCountry != null) {
            // Return a conflict status with a JSON response
            response.put("message", "Country already exists!");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        try {
            // Save the new country if it doesn't exist
            countryService.saveCountry(country);
            // Return a success message as JSON
            response.put("message", "Country added successfully!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Handle any exceptions that occur during saving
            response.put("message", "An error occurred while saving the country.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
}
