package com.immi.system.services;

import com.immi.system.DTOs.CustomerDTO;
import com.immi.system.models.CustomerModel;
import com.immi.system.repositories.CustomerRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional
    public CustomerModel createRecord(CustomerModel customer) {
        // Check if the customer already exists by their phone number or email
        if (customerRepository.existsByFirstNameAndLastNameAndPhoneAndEmail(customer.getFirstName(), customer.getLastName(), customer.getPhone(), customer.getEmail())) {
            throw new RuntimeException("Customer with this phone number or email already exists.");
        }
        customer.setCustomerId(null);
        // Save and return the created customer
        return customerRepository.save(customer);
    }

    @Transactional
    public CustomerModel updateRecord(Long id, CustomerModel customer) {
        // Find the customer by their ID
        CustomerModel existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // Ensure that the phone number or email is not taken by another customer
        if (customerRepository.existsByPhoneAndEmail(customer.getPhone(), customer.getEmail()) && 
            !existingCustomer.getCustomerId().equals(id)) {
            throw new RuntimeException("Customer with this phone number or email already exists.");
        }

        // Save the updated customer and return it
        customer.setCustomerId(id);  // Ensure the ID is maintained
        return customerRepository.save(customer);
    }

    @Transactional
    public void deleteRecord(Long id) {
        // Delete the customer by their ID
        customerRepository.deleteById(id);
    }

    public List<CustomerDTO> getAllListRecords() {
        return customerRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<CustomerModel> findAll() {
        return customerRepository.findAll();
    }

    public CustomerModel findById(Long id) {
        return customerRepository.findById(id).orElse(null);
    }

    // Method to get the next customer based on the given id
    public CustomerModel findNext(Long id) {
        Optional<CustomerModel> nextCustomer = customerRepository.findFirstByCustomerIdGreaterThanOrderByCustomerIdAsc(id);
        return nextCustomer.orElse(null); // Return null if no next customer found
    }

    // Method to get the previous customer based on the given id
    public CustomerModel findPrevoius(Long id) {
        Optional<CustomerModel> previousCustomer = customerRepository.findFirstByCustomerIdLessThanOrderByCustomerIdDesc(id);
        return previousCustomer.orElse(null); // Return null if no previous customer found
    }

    // Method to get the first customer in the list
    public CustomerModel findFirst() {
        Optional<CustomerModel> firstCustomer = customerRepository.findFirstByOrderByCustomerIdAsc();
        return firstCustomer.orElse(null); // Return null if no first customer found
    }

    // Method to get the last customer in the list
    public CustomerModel findLast() {
        Optional<CustomerModel> lastCustomer = customerRepository.findFirstByOrderByCustomerIdDesc();
        return lastCustomer.orElse(null); // Return null if no last customer found
    }

    public CustomerModel findByPhoneAndEmail(String phone, String email) {
        return customerRepository.findByPhoneAndEmail(phone, email);
    }

    private CustomerDTO mapToDTO(CustomerModel customer) {
        CustomerDTO dto = new CustomerDTO();
        dto.setCustomerId(customer.getCustomerId());  // Set the customer ID
        dto.setFirstName(customer.getFirstName());  // Set the first name
        dto.setLastName(customer.getLastName());  // Set the last name
        dto.setPhone(customer.getPhone());  // Set the phone number
        dto.setEmail(customer.getEmail());  // Set the email
        dto.setPassportNumber(customer.getPassportNumber());  // Set the email

        return dto;
    }
}
