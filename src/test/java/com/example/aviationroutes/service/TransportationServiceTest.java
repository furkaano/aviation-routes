package com.example.aviationroutes.service;

import com.example.aviationroutes.model.Location;
import com.example.aviationroutes.model.Transportation;
import com.example.aviationroutes.model.Transportation.TransportationType;
import com.example.aviationroutes.repository.TransportationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TransportationServiceTest {
    @Mock
    private TransportationRepository transportationRepository;

    @InjectMocks
    private TransportationService transportationService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTransportation(){
        Location origin = new Location();
        origin.setId(10L);

        Location destination = new Location();
        destination.setId(20L);

        Transportation transportation = new Transportation();
        transportation.setOriginLocation(origin);
        transportation.setDestinationLocation(destination);
        transportation.setTransportationType(TransportationType.BUS);

        when(transportationRepository.save(any(Transportation.class)))
                .thenAnswer(invocation -> {
                    Transportation saved = invocation.getArgument(0);
                    saved.setId(1L);
                    return saved;
                });

        Transportation result = transportationService.createTransportation(transportation);

        assertNotNull(result.getId());
        assertEquals(TransportationType.BUS, result.getTransportationType());
        verify(transportationRepository, times(1)).save(any(Transportation.class));
    }

    @Test
    void testGetTransportationById() {
        Transportation transportation = new Transportation();
        transportation.setId(1L);
        transportation.setTransportationType(TransportationType.SUBWAY);

        when(transportationRepository.existsById(1L)).thenReturn(true);
        when(transportationRepository.findById(1L)).thenReturn(Optional.of(transportation));

        Optional<Transportation> result = transportationService.getTransportationById(1L);

        assertTrue(result.isPresent());
        assertEquals(TransportationType.SUBWAY, result.get().getTransportationType());
    }

    @Test
    void testDeleteTransportation() {
        when(transportationRepository.existsById(1L)).thenReturn(true);
        doNothing().when(transportationRepository).deleteById(1L);

        transportationService.deleteTransportation(1L);

        verify(transportationRepository, times(1)).deleteById(1L);
    }
}
