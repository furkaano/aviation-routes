package com.example.aviationroutes.service;

import com.example.aviationroutes.dto.RouteDto;
import com.example.aviationroutes.dto.SegmentDto;
import com.example.aviationroutes.exception.InvalidRouteException;
import com.example.aviationroutes.model.Location;
import com.example.aviationroutes.model.Transportation;
import com.example.aviationroutes.repository.LocationRepository;
import com.example.aviationroutes.repository.TransportationRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RouteService {

    private final TransportationRepository transportationRepository;
    private final LocationRepository locationRepository;

    public RouteService(TransportationRepository transportationRepository, LocationRepository locationRepository) {
        this.transportationRepository = transportationRepository;
        this.locationRepository = locationRepository;
    }

    /**
     * Combines different route patterns (direct flight, flight with pre-transfer,
     * flight with post-transfer, flight with both pre- and post-transfer).
     * If no valid route is found, throws InvalidRouteException.
     */
    public List<RouteDto> findAllValidRoutes(Long originId, Long destinationId){
        if (originId.equals(destinationId)) {
            throw new InvalidRouteException("Origin and destination cannot be the same.");
        }
        List<Transportation> transportations = transportationRepository.findAll();

        List<Transportation> flights = new ArrayList<>();
        List<Transportation> nonFlights = new ArrayList<>();

        for(Transportation transportation : transportations){
            if(transportation.getTransportationType() == Transportation.TransportationType.FLIGHT){
                flights.add(transportation);
            } else {
                nonFlights.add(transportation);
            }
        }

        List<RouteDto> results = new ArrayList<>();

        results.addAll(findDirectFlights(flights, originId, destinationId));
        results.addAll(findBeforeFlight(flights, nonFlights, originId, destinationId));
        results.addAll(findAfterFlight(flights, nonFlights, originId, destinationId));
        results.addAll(findBeforeAndAfterFlight(flights, nonFlights, originId, destinationId));

        if (results.isEmpty()) {
            throw new InvalidRouteException("Invalid route: no flight segment found or no valid route.");
        }

        return results;
    }

    /**
     * Finds direct flights from originId to destinationId.
     */
    private List<RouteDto> findDirectFlights(List<Transportation> flights, Long originId, Long destinationId) {
        List<RouteDto> routeList = new ArrayList<>();
        for (Transportation flight: flights){
            if(flight.getOriginLocation().getId().equals(originId) &&
                    flight.getDestinationLocation().getId().equals(destinationId)){
                String description = "direct flight";
                routeList.add(createRouteDto(description, null, flight, null));
            }
        }
        return routeList;
    }

    /**
     * Finds before flight from originId to destinationId.
     */
    private List<RouteDto> findBeforeFlight(List<Transportation> flights, List<Transportation> nonFlights, Long originId, Long destinationId) {
        List<RouteDto> routeList = new ArrayList<>();
        for(Transportation beforeFlight : nonFlights){
            if(beforeFlight.getOriginLocation().getId().equals(originId)){
                Location loc = beforeFlight.getDestinationLocation();
                for(Transportation flight: flights){
                    if(flight.getOriginLocation().getId().equals(loc.getId()) &&
                            flight.getDestinationLocation().getId().equals(destinationId)){
                        String description = "before flight: " + loc.getName();
                        routeList.add(createRouteDto(description, beforeFlight, flight, null));
                    }
                }
            }
        }
        return routeList;
    }

    /**
     * Finds after flight from originId to destinationId.
     */
    private List<RouteDto> findAfterFlight(List<Transportation> flights, List<Transportation> nonFlights, Long originId, Long destinationId) {
        List<RouteDto> routeList = new ArrayList<>();
        for (Transportation flight : flights) {
            if (flight.getOriginLocation().getId().equals(originId)) {
                Location loc = flight.getDestinationLocation();
                for (Transportation afterFlight : nonFlights) {
                    if (afterFlight.getOriginLocation().getId().equals(loc.getId()) &&
                            afterFlight.getDestinationLocation().getId().equals(destinationId)) {
                        String description = "after flight: " + loc.getName();
                        routeList.add(createRouteDto(description, null, flight, afterFlight));
                    }
                }
            }
        }
        return routeList;
    }

    /**
     * Finds before and after flight from originId to destinationId.
     */
    private List<RouteDto> findBeforeAndAfterFlight(List<Transportation> flights, List<Transportation> nonFlights, Long originId, Long destinationId) {
        List<RouteDto> routeList = new ArrayList<>();

        for (Transportation beforeFlight : nonFlights) {
            if (beforeFlight.getOriginLocation().getId().equals(originId)) {
                Location loc1 = beforeFlight.getDestinationLocation();
                for (Transportation flight : flights) {
                    if (flight.getOriginLocation().getId().equals(loc1.getId())) {
                        Location loc2 = flight.getDestinationLocation();
                        for (Transportation afterFlight : nonFlights) {
                            if (afterFlight.getOriginLocation().getId().equals(loc2.getId()) &&
                                    afterFlight.getDestinationLocation().getId().equals(destinationId)) {
                                String description = "before & after flight: " + loc1.getName() + " & " + loc2.getName();
                                routeList.add(createRouteDto(description, beforeFlight, flight, afterFlight));
                            }
                        }
                    }
                }
            }
        }
        return routeList;
    }

    private RouteDto createRouteDto(String description, Transportation before, Transportation flight, Transportation after){
        RouteDto routeDto = new RouteDto();
        routeDto.setDescription(description);
        routeDto.setBeforeFlightTransfer(before == null ? null : toSegmentDto(before));
        routeDto.setFlight(flight == null ? null : toSegmentDto(flight));
        routeDto.setAfterFlightTransfer(after == null ? null : toSegmentDto(after));
        return routeDto;
    }

    private SegmentDto toSegmentDto(Transportation transportation) {
        SegmentDto sd = new SegmentDto();
        sd.setOrigin(transportation.getOriginLocation().getName());       // display name
        sd.setDestination(transportation.getDestinationLocation().getName());
        sd.setTransportationType(transportation.getTransportationType().name());
        return sd;
    }
}
