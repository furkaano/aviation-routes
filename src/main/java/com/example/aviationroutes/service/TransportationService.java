package com.example.aviationroutes.service;

import com.example.aviationroutes.exception.DuplicateEntryException;
import com.example.aviationroutes.model.Transportation;
import com.example.aviationroutes.model.Transportation.TransportationType;
import com.example.aviationroutes.repository.TransportationRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TransportationService {

    private final TransportationRepository transportationRepository;

    public TransportationService(TransportationRepository transportationRepository){
        this.transportationRepository = transportationRepository;
    }

    public Transportation createTransportation(Transportation transportation){
        Long originId = transportation.getOriginLocation().getId();
        Long destinationId = transportation.getDestinationLocation().getId();
        TransportationType type = transportation.getTransportationType();

        if (originId.equals(destinationId)) {
            throw new RuntimeException("Origin and destination cannot be the same.");
        }

        Optional<Transportation> existing = transportationRepository.findByOriginLocation_IdAndDestinationLocation_IdAndTransportationType(originId, destinationId, type);
        if(existing.isPresent()){
            throw new DuplicateEntryException("Transportation from origin " + originId +
                    " to destination " + destinationId +
                    " with type " + type + " already exists.");
        }
        return transportationRepository.save(transportation);
    }

    public Transportation updateTransportation(Long id, Transportation transportation){
        if (transportation.getOriginLocation().getId().equals(transportation.getDestinationLocation().getId())) {
            throw new RuntimeException("Origin and destination cannot be the same.");
        }

        Optional<Transportation> existingTrans= transportationRepository.findById(id);
        if(existingTrans.isPresent()){
            Optional<Transportation> duplicate = transportationRepository
                    .findByOriginLocation_IdAndDestinationLocation_IdAndTransportationType(
                            transportation.getOriginLocation().getId(),
                            transportation.getDestinationLocation().getId(),
                            transportation.getTransportationType());

            if (duplicate.isPresent() && !duplicate.get().getId().equals(id)) {
                throw new DuplicateEntryException("A transportation with the same origin, destination, and type already exists.");
            }

            Transportation exist = existingTrans.get();
            exist.setOriginLocation(transportation.getOriginLocation());
            exist.setDestinationLocation(transportation.getDestinationLocation());
            exist.setTransportationType(transportation.getTransportationType());
            return transportationRepository.save(exist);
        }
        throw new RuntimeException("Transportation not found with id: " + id);
    }

    public void deleteTransportation(Long id){
        if(transportationRepository.existsById(id)){
            transportationRepository.deleteById(id);
            return;
        }
        throw new RuntimeException("Transportation not found with id: " + id);
    }

    public Optional<Transportation> getTransportationById(Long id){
        if(transportationRepository.existsById(id)){
            return transportationRepository.findById(id);
        }
        throw new RuntimeException("Transportation not found with id: " + id);
    }

    public List<Transportation> getAllTransportations() {
        return transportationRepository.findAll();
    }

    public List<Transportation> saveAllTransportations(List<Transportation> transportations) {
        return transportationRepository.saveAll(transportations);
    }
}
