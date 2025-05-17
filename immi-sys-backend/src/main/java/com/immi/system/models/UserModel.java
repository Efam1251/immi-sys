package com.immi.system.models;

import com.immi.system.enums.RoleEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "user")
public class UserModel implements UserDetails {
    
    // ID is auto-generated using an identity strategy for simplicity
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // First name should not be blank and should have a minimum length of 2
    @NotBlank(message = "First name is required.")
    @Size(min = 2, message = "First name must have at least 2 characters.")
    @Column(nullable = false)
    private String firstName;

    // Last name should not be blank and should have a minimum length of 2
    @NotBlank(message = "Last name is required.")
    @Size(min = 2, message = "Last name must have at least 2 characters.")
    @Column(nullable = false)
    private String lastName;

    // Email should be valid and unique
    @Email(message = "Email should be valid.")
    @NotBlank(message = "Email is required.")
    @Column(nullable = false, unique = true)
    private String email;

    // Password should be at least 8 characters, ensuring security.
    @NotBlank(message = "Password is required.")
    @Size(min = 8, message = "Password must have at least 8 characters.")
    @Column(nullable = false)
    private String password;

    // Role can be ADMIN or USER, ensuring enum values are strictly adhered to.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleEnum role;

    // Whether the user is active (able to login). Default is true.
    @Column(nullable = false)
    private boolean active = true;

    public UserModel() {
    }

    public UserModel(Long id, String firstName, String lastName, String email, String password, RoleEnum role, boolean active) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RoleEnum getRole() {
        return role;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Grant authority based on the role (for example, "ROLE_USER" or "ROLE_ADMIN")
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername() {
        return email; // You can use email as the username
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Assuming accounts don't expire
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Assuming accounts aren't locked
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Assuming credentials don't expire
    }

    @Override
    public boolean isEnabled() {
        return active; // Account is active if the user is enabled
    }
}
