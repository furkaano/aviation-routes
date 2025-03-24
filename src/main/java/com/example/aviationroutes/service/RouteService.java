package com.example.aviationroutes.service;

import com.example.aviationroutes.dto.RouteDto;
import com.example.aviationroutes.dto.SegmentDto;
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

    public List<RouteDto> findAllValidRoutes(Long originId, Long destinationId){
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

        return results;
    }

    private List<RouteDto> findDirectFlights(List<Transportation> flights, Long originId, Long destinationId) {
        List<RouteDto> routeList = new ArrayList<>();
        for (Transportation flight: flights){
            if(flight.getOriginLocation().getId().equals(originId) &&
                    flight.getDestinationLocation().getId().equals(destinationId)){
                RouteDto rd = new RouteDto();
                rd.setDescription("direct flight");
                rd.setBeforeFlightTransfer(null);
                rd.setFlight(toSegmentDto(flight));
                rd.setAfterFlightTransfer(null);
                routeList.add(rd);
            }
        }
        return routeList;
    }

    private List<RouteDto> findBeforeFlight(List<Transportation> flights, List<Transportation> nonFlights, Long originId, Long destinationId) {
        List<RouteDto> routeList = new ArrayList<>();
        for(Transportation nf: nonFlights){
            if(nf.getOriginLocation().getId().equals(originId)){
                Location loc = nf.getDestinationLocation();
                for(Transportation f: flights){
                    if(f.getOriginLocation().getId().equals(loc.getId()) &&
                    f.getDestinationLocation().getId().equals(destinationId)){
                        RouteDto rd = new RouteDto();
                        rd.setDescription("before flight: " + loc.getName());
                        rd.setBeforeFlightTransfer(toSegmentDto(nf));
                        rd.setFlight(toSegmentDto(f));
                        rd.setAfterFlightTransfer(null);

                        routeList.add(rd);
                    }
                }
            }
        }
        return routeList;
    }

    private List<RouteDto> findAfterFlight(List<Transportation> flights, List<Transportation> nonFlights, Long originId, Long destinationId) {
        List<RouteDto> routeList = new ArrayList<>();
        for (Transportation f : flights) {
            if (f.getOriginLocation().getId().equals(originId)) {
                Location loc = f.getDestinationLocation();
                for (Transportation nf : nonFlights) {
                    if (nf.getOriginLocation().getId().equals(loc.getId()) &&
                            nf.getDestinationLocation().getId().equals(destinationId)) {
                        RouteDto rd = new RouteDto();
                        rd.setDescription("after flight: " + loc.getName());
                        rd.setBeforeFlightTransfer(null);
                        rd.setFlight(toSegmentDto(f));
                        rd.setAfterFlightTransfer(toSegmentDto(nf));

                        routeList.add(rd);
                    }
                }
            }
        }
        return routeList;
    }

    private List<RouteDto> findBeforeAndAfterFlight(List<Transportation> flights, List<Transportation> nonFlights, Long originId, Long destinationId) {
        List<RouteDto> routeList = new ArrayList<>();

        for (Transportation nf1 : nonFlights) {
            if (nf1.getOriginLocation().getId().equals(originId)) {
                Location loc1 = nf1.getDestinationLocation();
                for (Transportation f : flights) {
                    if (f.getOriginLocation().getId().equals(loc1.getId())) {
                        Location loc2 = f.getDestinationLocation();
                        for (Transportation nf2 : nonFlights) {
                            if (nf2.getOriginLocation().getId().equals(loc2.getId()) &&
                                    nf2.getDestinationLocation().getId().equals(destinationId)) {
                                RouteDto rd = new RouteDto();
                                rd.setDescription("before & after flight: " + loc1.getName() + " & " + loc2.getName());
                                rd.setBeforeFlightTransfer(toSegmentDto(nf1));
                                rd.setFlight(toSegmentDto(f));
                                rd.setAfterFlightTransfer(toSegmentDto(nf2));

                                routeList.add(rd);
                            }
                        }
                    }
                }
            }
        }
        return routeList;
    }

    private SegmentDto toSegmentDto(Transportation transportation) {
        SegmentDto sd = new SegmentDto();
        sd.setOrigin(transportation.getOriginLocation().getName());       // display name
        sd.setDestination(transportation.getDestinationLocation().getName());
        sd.setTransportationType(transportation.getTransportationType().name());
        return sd;
    }
}
