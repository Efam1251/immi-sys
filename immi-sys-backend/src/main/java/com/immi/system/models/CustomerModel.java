package com.immi.system.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Table(name = "customer")
public class CustomerModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")  // Explicitly setting column name
    public Long customerId;

    @Column(name = "first_name", nullable = false, length = 100)
    @NotNull(message = "Customer first name is required.")
    @Size(max = 100, message = "Customer first name cannot exceed 100 characters.")
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    @NotNull(message = "Customer last name is required.")
    @Size(max = 100, message = "Customer last name cannot exceed 100 characters.")
    private String lastName;

    @Column(name = "address", nullable = false, length = 500)
    @NotNull(message = "Customer address is required.")
    @Size(max = 500, message = "Customer address cannot exceed 500 characters.")
    private String address;

    @Column(name = "phone", nullable = false, length = 12)
    @Pattern(regexp = "\\d{3}-\\d{3}-\\d{4}", message = "Phone number must follow the format 999-999-9999.")
    private String phone;

    @Column(name = "email", nullable = false)
    @Email(message = "A valid email is required.")
    @NotNull(message = "Email is required.")
    private String email;

    @Column(name = "date_of_birth", nullable = false)
    @Past(message = "Date of birth must be in the past.")
    private LocalDate dateOfBirth;

    @ManyToOne
    @JoinColumn(name = "gender_id", nullable = true) // Setting the join column name
    private GenderModel gender; // References the Gender entity.

    @ManyToOne
    @JoinColumn(name = "marital_status_id", nullable = true) // Setting the join column name
    private MaritalStatusModel maritalStatus; // References the Marital Status entity.

    @ManyToOne
    @JoinColumn(name = "immigration_status_id", nullable = true) // Setting the join column name
    private ImmigrationStatusModel immigrationStatus; // References the Immigration Status entity.

    @Column(name = "immigration_number", nullable = true, length = 20)
    @Size(max = 20, message = "Immigration number cannot exceed 20 characters.")
    private String immigrationNumber;

    @Pattern(regexp = "^$|\\d{3}-\\d{2}-\\d{4}", message = "Social Security Number must follow the format 999-99-9999.")
    @Column(name = "social_security", nullable = true, unique = true, length = 11)
    private String socialSecurity;

    @Column(name = "local_id", nullable = true, unique = true, length = 20)
    @Size(max = 20, message = "Local ID cannot exceed 20 characters.")
    private String localId;

    @Column(name = "passport_number", nullable = true, length = 20)
    @Size(max = 20, message = "Passport number cannot exceed 20 characters.")
    private String passportNumber;

    @Column(name = "notes", nullable = true, length = 500)
    @Size(max = 500, message = "Notes cannot exceed 500 characters.")
    private String notes; // Field to store additional details.

    public CustomerModel() {
    }

    public CustomerModel(Long customerId, String firstName, String lastName, String address, String phone, String email, LocalDate dateOfBirth, GenderModel gender, MaritalStatusModel maritalStatus, ImmigrationStatusModel immigrationStatus, String immigrationNumber, String socialSecurity, String localId, String passportNumber, String notes) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.maritalStatus = maritalStatus;
        this.immigrationStatus = immigrationStatus;
        this.immigrationNumber = immigrationNumber;
        this.socialSecurity = socialSecurity;
        this.localId = localId;
        this.passportNumber = passportNumber;
        this.notes = notes;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public GenderModel getGender() {
        return gender;
    }

    public void setGender(GenderModel gender) {
        this.gender = gender;
    }

    public MaritalStatusModel getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(MaritalStatusModel maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public ImmigrationStatusModel getImmigrationStatus() {
        return immigrationStatus;
    }

    public void setImmigrationStatus(ImmigrationStatusModel immigrationStatus) {
        this.immigrationStatus = immigrationStatus;
    }

    public String getImmigrationNumber() {
        return immigrationNumber;
    }

    public void setImmigrationNumber(String immigrationNumber) {
        this.immigrationNumber = immigrationNumber;
    }

    public String getSocialSecurity() {
        return socialSecurity;
    }

    public void setSocialSecurity(String socialSecurity) {
        this.socialSecurity = socialSecurity;
    }

    public String getLocalId() {
        return localId;
    }

    public void setLocalId(String localId) {
        this.localId = localId;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    @Override
    public String toString() {
        return "CustomerModel{" +
                "customerId=" + customerId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", gender=" + gender +
                ", maritalStatus=" + maritalStatus +
                ", immigrationStatus=" + immigrationStatus +
                ", immigrationNumber='" + immigrationNumber + '\'' +
                ", socialSecurity='" + socialSecurity + '\'' +
                ", localId='" + localId + '\'' +
                ", passportNumber='" + passportNumber + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }
    
}
