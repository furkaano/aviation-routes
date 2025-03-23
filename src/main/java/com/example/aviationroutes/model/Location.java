package com.example.aviationroutes.model;

import jakarta.persistence.*;
import org.springframework.lang.NonNull;

@Entity
@Table(name = "locations")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String name;

    @NonNull
    private String country;

    @NonNull
    private String city;

    @NonNull
    private String locationCode;

    public Location(Long id, @NonNull String name, @NonNull String country, @NonNull String city, @NonNull String locationCode) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.city = city;
        this.locationCode = locationCode;
    }

    public Location() {

    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getCountry() {
        return country;
    }

    public void setCountry(@NonNull String country) {
        this.country = country;
    }

    @NonNull
    public String getCity() {
        return city;
    }

    public void setCity(@NonNull String city) {
        this.city = city;
    }

    @NonNull
    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(@NonNull String locationCode) {
        this.locationCode = locationCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
