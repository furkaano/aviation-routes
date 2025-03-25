package com.example.aviationroutes.controller;

import com.example.aviationroutes.model.Transportation;
import com.example.aviationroutes.service.TransportationService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/transportations")
@CrossOrigin(origins = "http://localhost:3000")
public class TransportationController {

    private final TransportationService transportationService;

    public TransportationController(TransportationService transportationService) {
        this.transportationService = transportationService;
    }

    @Operation(summary = "Create a transportation",
            description = "Create a transportation with originId, destinationId, transportationType")
    @PostMapping
    public ResponseEntity<Transportation> createTransportation(@RequestBody Transportation transportation) {
        Transportation saved = transportationService.createTransportation(transportation);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @Operation(summary = "Create multiple transportations at once",
            description = "Create multiple transportations with originId, destinationId, transportationType at once")
    @PostMapping("/bulkInsert")
    public ResponseEntity<List<Transportation>> createLocationsBulk(@RequestBody List<Transportation> transportations) {
        List<Transportation> savedTransportations = transportationService.saveAllTransportations(transportations);
        return new ResponseEntity<>(savedTransportations, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all transportations at once",
            description = "Get all transportations from database at once")
    @GetMapping
    public ResponseEntity<List<Transportation>> getAllTransportations() {
        List<Transportation> list = transportationService.getAllTransportations();
        return ResponseEntity.ok(list);
    }

    @Operation(summary = "Get transportation by given id",
            description = "Get transportation by given id on database")
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Transportation>> getTransportation(@PathVariable Long id) {
        Optional<Transportation> t = transportationService.getTransportationById(id);
        return ResponseEntity.ok(t);
    }

    @Operation(summary = "Update transportation by given id",
            description = "Updates transportation by given id on database")
    @PutMapping("/{id}")
    public ResponseEntity<Transportation> updateTransportation(@PathVariable Long id,
                                                               @RequestBody Transportation transportation) {
        Transportation updated = transportationService.updateTransportation(id, transportation);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Delete transportation by given id",
            description = "Delete transportation by given id on database")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransportation(@PathVariable Long id) {
        transportationService.deleteTransportation(id);
        return ResponseEntity.noContent().build();
    }
}
