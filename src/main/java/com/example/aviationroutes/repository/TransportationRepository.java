package com.example.aviationroutes.repository;

import com.example.aviationroutes.model.Transportation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransportationRepository extends JpaRepository<Transportation, Long> {
    Optional<Transportation> findByOriginLocation_IdAndDestinationLocation_IdAndTransportationType(Long originId, Long destinationId, Transportation.TransportationType type);
}
