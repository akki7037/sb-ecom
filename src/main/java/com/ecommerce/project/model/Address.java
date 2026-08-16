package com.ecommerce.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@Table(name = "addressed")
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long addressId;

    @NotBlank
    @Size(min = 6, message = "please provide at least 6 characters")
    private String street;

    @NotBlank
    @Size(min = 6, message = "please provide at least 6 characters")
    private String buildingName;

    @NotBlank
    @Size(min = 2, message = "please provide at least 2 characters")
    private String city;
    @NotBlank
    @Size(min = 2, message = "please provide at least 2 characters")
    private String state;
    @NotBlank
    @Size(min = 3, message = "please provide at least 3 characters")
    private String country;
    @NotBlank
    @Size(min = 6, message = "please provide at least 6 characters")
    private String pincode;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

    public Address(String street, String city, String state, String country, String pincode, String buildingName) {
        this.street = street;
        this.buildingName = buildingName;
        this.city = city;
        this.state = state;
        this.country = country;
        this.pincode = pincode;
    }
}
