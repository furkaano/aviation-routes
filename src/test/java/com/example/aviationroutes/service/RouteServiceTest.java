package com.example.aviationroutes.service;

import com.example.aviationroutes.dto.RouteDto;
import com.example.aviationroutes.model.Location;
import com.example.aviationroutes.model.Transportation;
import com.example.aviationroutes.model.Transportation.TransportationType;
import com.example.aviationroutes.repository.TransportationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class RouteServiceTest {
    @Mock
    private TransportationRepository transportationRepository;

    @InjectMocks
    private RouteService routeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAllValidRoutes_DirectFlight(){
        Transportation flight = createTransportation(1L, 2L, TransportationType.FLIGHT);

        List<Transportation> allTransportations = List.of(flight);
        when(transportationRepository.findAll()).thenReturn(allTransportations);

        List<RouteDto> result = routeService.findAllValidRoutes(1L, 2L);

        assertEquals(1, result.size());
        RouteDto route = result.get(0);
        assertEquals("direct flight", route.getDescription());
        assertNotNull(route.getFlight());
        assertNull(route.getBeforeFlightTransfer());
        assertNull(route.getAfterFlightTransfer());
    }

    @Test
    void testFindAllValidRoutes_BeforeFlight() {
        Transportation bus = createTransportation(1L, 10L, TransportationType.BUS);
        Transportation flight = createTransportation(10L, 2L, TransportationType.FLIGHT);

        List<Transportation> allTransportations = List.of(bus, flight);
        when(transportationRepository.findAll()).thenReturn(allTransportations);

        List<RouteDto> result = routeService.findAllValidRoutes(1L, 2L);

        assertEquals(1, result.size());
        RouteDto route = result.get(0);
        assertTrue(route.getDescription().contains("before flight"));
        assertNotNull(route.getBeforeFlightTransfer());
        assertNotNull(route.getFlight());
        assertNull(route.getAfterFlightTransfer());
    }

    @Test
    void testFindAllValidRoutes_AfterFlight() {
        Transportation flight = createTransportation(1L, 10L, TransportationType.FLIGHT);
        Transportation uber = createTransportation(10L, 2L, TransportationType.UBER);

        List<Transportation> allTransportations = List.of(flight, uber);
        when(transportationRepository.findAll()).thenReturn(allTransportations);

        List<RouteDto> result = routeService.findAllValidRoutes(1L, 2L);

        assertEquals(1, result.size());
        RouteDto route = result.get(0);
        assertTrue(route.getDescription().contains("after flight"));
        assertNull(route.getBeforeFlightTransfer());
        assertNotNull(route.getFlight());
        assertNotNull(route.getAfterFlightTransfer());
    }

    @Test
    void testFindAllValidRoutes_BeforeAndAfterFlight() {
        Transportation nf1 = createTransportation(1L, 10L, TransportationType.BUS);
        Transportation f = createTransportation(10L, 20L, TransportationType.FLIGHT);
        Transportation nf2 = createTransportation(20L, 2L, TransportationType.SUBWAY);

        List<Transportation> allTransportations = List.of(nf1, f, nf2);
        when(transportationRepository.findAll()).thenReturn(allTransportations);

        List<RouteDto> result = routeService.findAllValidRoutes(1L, 2L);

        assertEquals(1, result.size());
        RouteDto route = result.get(0);
        assertTrue(route.getDescription().contains("before & after flight"));
        assertNotNull(route.getBeforeFlightTransfer());
        assertNotNull(route.getFlight());
        assertNotNull(route.getAfterFlightTransfer());
    }

    private Transportation createTransportation(Long originId, Long destinationId, TransportationType type) {
        Location origin = new Location();
        origin.setId(originId);
        origin.setName("Location: " + originId);

        Location destination = new Location();
        destination.setId(destinationId);
        destination.setName("Location: " + destinationId);

        Transportation transportation = new Transportation();
        transportation.setOriginLocation(origin);
        transportation.setDestinationLocation(destination);
        transportation.setTransportationType(type);

        return transportation;
    }
}
