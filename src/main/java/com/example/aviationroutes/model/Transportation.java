package com.example.aviationroutes.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.lang.NonNull;

@Entity
@Table(name = "transportations")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Transportation {

    public enum TransportationType {
        FLIGHT,
        BUS,
        SUBWAY,
        UBER
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Origin location is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "origin_location_id")
    private Location originLocation;

    @NotNull(message = "Destination location is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "destination_location_id")
    private Location destinationLocation;

    @NotNull(message = "Transportation type is required")
    @Enumerated(EnumType.STRING)
    private TransportationType transportationType;


    public Transportation(Long id, @NotNull Location originLocation, @NotNull Location destinationLocation, @NotNull TransportationType transportationType) {
        this.id = id;
        this.originLocation = originLocation;
        this.destinationLocation = destinationLocation;
        this.transportationType = transportationType;
    }

    public Transportation() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NonNull
    public Location getOriginLocation() {
        return originLocation;
    }

    public void setOriginLocation(@NonNull Location originLocation) {
        this.originLocation = originLocation;
    }

    @NonNull
    public Location getDestinationLocation() {
        return destinationLocation;
    }

    public void setDestinationLocation(@NonNull Location destinationLocation) {
        this.destinationLocation = destinationLocation;
    }

    public TransportationType getTransportationType() {
        return transportationType;
    }

    public void setTransportationType(@NonNull TransportationType transportationType) {
        this.transportationType = transportationType;
    }
}
