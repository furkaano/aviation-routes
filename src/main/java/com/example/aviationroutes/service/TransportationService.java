package com.example.aviationroutes.service;

import com.example.aviationroutes.model.Transportation;
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
        return transportationRepository.save(transportation);
    }

    public Transportation updateTransportation(Long id, Transportation transportation){
        Optional<Transportation> existingTrans= transportationRepository.findById(id);
        if(existingTrans.isPresent()){
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
}
