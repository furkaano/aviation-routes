package com.example.aviationroutes.service;

import com.example.aviationroutes.model.Location;
import com.example.aviationroutes.repository.LocationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LocationServiceTest {
    @Mock
    private LocationRepository locationRepository;

    @InjectMocks
    private LocationService locationService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateLocation(){
        Location location = new Location();
        location.setName("Istanbul Airport");
        location.setCity("Istanbul");
        location.setCountry("Turkey");
        location.setLocationCode("IST");

        when(locationRepository.save(any(Location.class)))
                .thenAnswer(invocation -> {
                    Location loc = invocation.getArgument(0);
                    loc.setId(1L);
                    return loc;
                });

        Location saved = locationService.createLocation(location);

        assertNotNull(saved.getId());
        assertEquals("IST", saved.getLocationCode());
        verify(locationRepository, times(1)).save(any(Location.class));
    }

    @Test
    void testGetLocationById() {
        Location location = new Location();
        location.setLocationCode("IST");

        when(locationRepository.existsById(1L)).thenReturn(true);
        when(locationRepository.findById(1L)).thenReturn(Optional.of(location));


        Optional<Location> result = locationService.getLocationById(1L);
        assertTrue(result.isPresent());
        assertEquals("IST", result.get().getLocationCode());
    }

    @Test
    void testDeleteLocation() {
        when(locationRepository.existsById(1L)).thenReturn(true);
        doNothing().when(locationRepository).deleteById(1L);

        locationService.deleteLocation(1L);

        verify(locationRepository, times(1)).deleteById(1L);
    }
}
