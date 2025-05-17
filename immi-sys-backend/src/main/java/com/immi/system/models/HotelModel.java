package com.immi.system.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.List;

@Entity
@Table(name = "hotel")
public class HotelModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, name = "name")
    private String name;
    
    @Column(nullable = false, name = "description")
    private String description;
    
    @OneToOne
    @JoinColumn(name = "address_id", nullable = false)  // Foreign Key
    private AddressModel address;
    
    @ManyToMany
    @JoinTable(
            name = "hotel_amenities",
            joinColumns = @JoinColumn(name = "hotel_id"),
            inverseJoinColumns = @JoinColumn(name = "amenity_id")
    )
    private List<AmenityModel> amenities;
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long hotelId) {
        this.id = hotelId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public AddressModel getAddress() {
        return address;
    }
    
    public void setAddress(AddressModel address) {
        this.address = address;
    }

    public List<AmenityModel> getAmenities() {
        return amenities;
    }

    public void setAmenities(List<AmenityModel> amenities) {
        this.amenities = amenities;
    }

}
