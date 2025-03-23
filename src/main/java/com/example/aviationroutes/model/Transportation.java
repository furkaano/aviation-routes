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
}
