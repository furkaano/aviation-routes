package com.example.aviationroutes.model;

import jakarta.persistence.*;
import org.springframework.lang.NonNull;

@Entity
@Table(name = "transportations")
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

    @NonNull
    @ManyToOne
    @JoinColumn(name = "origin_location_id")
    private Location originLocationId;

    @NonNull
    private String originLocationName;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "destination_location_id")
    private Location destinationLocationId;

    @NonNull
    private String destinationLocationName;

    @Enumerated(EnumType.STRING)
    private TransportationType transportationType;

    public Transportation(Long id, @NonNull Location originLocationId, @NonNull String originLocationName, @NonNull Location destinationLocationId, @NonNull String destinationLocationName, TransportationType transportationType) {
        this.id = id;
        this.originLocationId = originLocationId;
        this.originLocationName = originLocationName;
        this.destinationLocationId = destinationLocationId;
        this.destinationLocationName = destinationLocationName;
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
    public Location getOriginLocationId() {
        return originLocationId;
    }

    public void setOriginLocationId(@NonNull Location originLocationId) {
        this.originLocationId = originLocationId;
    }

    @NonNull
    public String getOriginLocationName() {
        return originLocationName;
    }

    public void setOriginLocationName(@NonNull String originLocationName) {
        this.originLocationName = originLocationName;
    }

    @NonNull
    public Location getDestinationLocationId() {
        return destinationLocationId;
    }

    public void setDestinationLocationId(@NonNull Location destinationLocationId) {
        this.destinationLocationId = destinationLocationId;
    }

    @NonNull
    public String getDestinationLocationName() {
        return destinationLocationName;
    }

    public void setDestinationLocationName(@NonNull String destinationLocationName) {
        this.destinationLocationName = destinationLocationName;
    }

    public TransportationType getTransportationType() {
        return transportationType;
    }

    public void setTransportationType(TransportationType transportationType) {
        this.transportationType = transportationType;
    }
}
