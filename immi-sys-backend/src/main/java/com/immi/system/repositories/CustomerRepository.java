package com.immi.system.repositories;

import com.immi.system.models.CustomerModel;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<CustomerModel, Long> {
    
    // Find a customer by first name and last name
    public Optional<CustomerModel> findByFirstNameAndLastName(String firstName, String lastName);

    // Check if a customer with a given full name, phone, and email exists
    public boolean existsByFirstNameAndLastNameAndPhoneAndEmail(
        String firstName, String lastName, String phone, String email
    );

    // Find the next customer based on customerId (greater than the current customerId)
    public Optional<CustomerModel> findFirstByCustomerIdGreaterThanOrderByCustomerIdAsc(Long customerId);

    // Find the previous customer based on customerId (less than the current customerId)
    public Optional<CustomerModel> findFirstByCustomerIdLessThanOrderByCustomerIdDesc(Long customerId);

    // Find the first customer ordered by customerId ascending
    public Optional<CustomerModel> findFirstByOrderByCustomerIdAsc();

    // Find the last customer ordered by customerId descending
    public Optional<CustomerModel> findFirstByOrderByCustomerIdDesc();

    public boolean existsByPhoneAndEmail(String phone, String email);

    public CustomerModel findByPhoneAndEmail(String phone, String email);

}
