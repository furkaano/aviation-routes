package com.example.aviationroutes.service;

import com.example.aviationroutes.exception.DuplicateEntryException;
import com.example.aviationroutes.model.Location;
import com.example.aviationroutes.repository.LocationRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LocationService {

    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository){
        this.locationRepository = locationRepository;
    }

    public Location createLocation(Location location){
        Optional<Location> existing = locationRepository.findByNameAndCountryAndCityAndLocationCode(
                location.getName(), location.getCountry(), location.getCity(), location.getLocationCode()
        );
        if(existing.isPresent()){
            throw new DuplicateEntryException("Location already exists.");
        }
        return locationRepository.save(location);
    }

    public Location updateLocation(Long id, Location location){
        Optional<Location> existingLoc = locationRepository.findById(id);
        if(existingLoc.isPresent()){
            Optional<Location> duplicate = locationRepository.findByNameAndCountryAndCityAndLocationCode(
                    location.getName(), location.getCountry(), location.getCity(), location.getLocationCode());
            if (duplicate.isPresent() && !duplicate.get().getId().equals(id)) {
                throw new DuplicateEntryException("A location with the same details already exists.");
            }

            Location exist = existingLoc.get();
            exist.setName(location.getName());
            exist.setCountry(location.getCountry());
            exist.setCity(location.getCity());
            exist.setLocationCode(location.getLocationCode());
            return locationRepository.save(exist);
        }
        throw new RuntimeException("Location not found with id: " + id);
    }

    public void deleteLocation(Long id){
        if(locationRepository.existsById(id)){
            locationRepository.deleteById(id);
            return;
        }
        throw new RuntimeException("Location not found with id: " + id);
    }

    public Optional<Location> getLocationById(Long id){
        if(locationRepository.existsById(id)){
            return locationRepository.findById(id);
        }
        throw new RuntimeException("Location not found with id: " + id);
    }

    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    public List<Location> saveAllLocations(List<Location> locations) {
        return locationRepository.saveAll(locations);
    }
}
