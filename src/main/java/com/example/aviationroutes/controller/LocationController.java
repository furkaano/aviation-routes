package com.example.aviationroutes.controller;

import com.example.aviationroutes.model.Location;
import com.example.aviationroutes.service.LocationService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/locations")
@CrossOrigin(origins = "http://localhost:3000")
public class LocationController {
    
    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @Operation(summary = "Create a new location",
                description = "Create a new Location with name, country, city, locationCode")
    @PostMapping
    public ResponseEntity<Location> createLocation(@Valid @RequestBody Location location) {
        Location saved = locationService.createLocation(location);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @Operation(summary = "Create multiple location at once",
                description = "Create multiple locations with name, country, city, locationCode at once")
    @PostMapping("/bulkInsert")
    public ResponseEntity<List<Location>> createLocationsBulk(@Valid @RequestBody List<Location> locations) {
        List<Location> savedLocations = locationService.saveAllLocations(locations);
        return new ResponseEntity<>(savedLocations, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all locations at once",
                description = "Get all locations from database at once")
    @GetMapping
    public ResponseEntity<List<Location>> getAllLocations() {
        List<Location> locations = locationService.getAllLocations();
        return ResponseEntity.ok(locations);
    }

    @Operation(summary = "Get location by given id",
                description = "Get location by given id from database")
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Location>> getLocationById(@PathVariable Long id) throws RuntimeException {
        Optional<Location> location = locationService.getLocationById(id);
        return ResponseEntity.ok(location);
    }

    @Operation(summary = "Update location by given id",
                description = "Update location by given id on database")
    @PutMapping("/{id}")
    public ResponseEntity<Location> updateLocation(@PathVariable Long id,
                                                   @RequestBody Location location) {
        Location updated = locationService.updateLocation(id, location);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Delete location by given id",
                description = "Delete location by given id on database")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Long id) {
        locationService.deleteLocation(id);
        return ResponseEntity.noContent().build();
    }
}
