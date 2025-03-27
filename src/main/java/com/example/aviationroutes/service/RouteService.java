package com.example.aviationroutes.service;

import com.example.aviationroutes.dto.RouteDto;
import com.example.aviationroutes.dto.SegmentDto;
import com.example.aviationroutes.exception.InvalidRouteException;
import com.example.aviationroutes.model.Location;
import com.example.aviationroutes.model.Transportation;
import com.example.aviationroutes.repository.LocationRepository;
import com.example.aviationroutes.repository.TransportationRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RouteService {

    private static class PathState {
        private final Long currentLocationId;
        private final List<Transportation> path;
        private final int flightsUsed;

        public PathState(Long currentLocationId, List<Transportation> path, int flightsUsed) {
            this.currentLocationId = currentLocationId;
            this.path = path;
            this.flightsUsed = flightsUsed;
        }
    }

    private final TransportationRepository transportationRepository;
    private final LocationRepository locationRepository;

    public RouteService(TransportationRepository transportationRepository, LocationRepository locationRepository) {
        this.transportationRepository = transportationRepository;
        this.locationRepository = locationRepository;
    }

    public List<RouteDto> findAllValidRoutes(Long originId, Long destinationId){
        if (originId.equals(destinationId)) {
            throw new InvalidRouteException("Origin and destination cannot be the same.");
        }
        List<Transportation> transportations = transportationRepository.findAll();
        if(transportations.isEmpty()){
            throw new InvalidRouteException("No transportation records found");
        }

        Map<Long, List<Transportation>> adjacencyMap = buildAdjacencyMap(transportations);
        //Initialize BFS
        Queue<PathState> queue = new LinkedList<>();
        queue.offer(new PathState(originId, new ArrayList<>(), 0));

        List<RouteDto> results = new ArrayList<>();

        while (!queue.isEmpty()){
            PathState current = queue.poll();
            Long currentLoc = current.currentLocationId;
            List<Transportation> pathSoFar = current.path;
            int flightsUsedSoFar = current.flightsUsed;

            // Check if reached the destination
            if(currentLoc.equals(destinationId)){
                if(pathSoFar.size() <= 3 && flightsUsedSoFar == 1 && isValidPath(pathSoFar)){
                    results.add(buildRouteDtoFromPath(pathSoFar));
                }
                continue;
            }

            if(pathSoFar.size() >= 3){
                continue;
            }

            List<Transportation> edges = adjacencyMap.getOrDefault(currentLoc, Collections.emptyList());
            for(Transportation next : edges){
                int nextFlightsUsed = flightsUsedSoFar;
                if(next.getTransportationType() == Transportation.TransportationType.FLIGHT){
                    nextFlightsUsed++;
                    if(nextFlightsUsed > 1){
                        continue;
                    }
                }

                List<Transportation> newPath = new ArrayList<>(pathSoFar);
                newPath.add(next);

                queue.offer(new PathState(
                        next.getDestinationLocation().getId(),
                        newPath,
                        nextFlightsUsed
                ));
            }
        }

        if (results.isEmpty()) {
            throw new InvalidRouteException("Invalid route: no flight segment found or no valid route.");
        }

        return results;
    }

    private boolean isValidPath(List<Transportation> path) {
        if (path.isEmpty()) return false;

        int flightIndex = -1;
        for (int i = 0; i < path.size(); i++) {
            if (path.get(i).getTransportationType() == Transportation.TransportationType.FLIGHT) {
                flightIndex = i;
                break;
            }
        }
        if (flightIndex == -1) return false; // No flight found; invalid.

        if (path.size() == 1) {
            // Only one segment: it must be a FLIGHT.
            return flightIndex == 0;
        } else if (path.size() == 2) {
            // Two segments: valid if either [non-FLIGHT, FLIGHT] or [FLIGHT, non-FLIGHT].
            return flightIndex == 0 || flightIndex == 1;
        } else if (path.size() == 3) {
            // Three segments: valid only if FLIGHT is in the middle.
            return flightIndex == 1;
        }
        return false;
    }

    /**
     * Build adjacency map: originId -> list of transportations
     */
    private Map<Long, List<Transportation>> buildAdjacencyMap(List<Transportation> transportations) {
        Map<Long, List<Transportation>> adjacency = new HashMap<>();
        for (Transportation transportation : transportations) {
            Long originId = transportation.getOriginLocation().getId();
            adjacency.computeIfAbsent(originId, key -> new ArrayList<>()).add(transportation);
        }
        return adjacency;
    }

    /**
     * Convert BFS into routeDto
     */
    private RouteDto buildRouteDtoFromPath(List<Transportation> path){
        RouteDto routeDto = new RouteDto();
        routeDto.setDescription(buildDescription(path));

        switch (path.size()){
            case 1: // direct flight
                routeDto.setFlight(toSegmentDto(path.get(0)));
                break;
            case 2: // either before flight or after flight
                Transportation t0 = path.get(0);
                Transportation t1 = path.get(1);
                if(t0.getTransportationType() == Transportation.TransportationType.FLIGHT){ // flight + after
                    routeDto.setFlight(toSegmentDto(t0));
                    routeDto.setAfterFlightTransfer(toSegmentDto(t1));
                } else { // before + flight
                    routeDto.setBeforeFlightTransfer(toSegmentDto(t0));
                    routeDto.setFlight(toSegmentDto(t1));
                }
                break;
            case 3: // before + flight + after
                routeDto.setBeforeFlightTransfer(toSegmentDto(path.get(0)));
                routeDto.setFlight(toSegmentDto(path.get(1)));
                routeDto.setAfterFlightTransfer(toSegmentDto(path.get(2)));
                break;
        }
        return routeDto;
    }

    private String buildDescription(List<Transportation> path){
        if(path.size() == 1){
            return "direct flight";
        } else if (path.size() == 2){
            Transportation first = path.get(0);
            if(first.getTransportationType() == Transportation.TransportationType.FLIGHT){
                return "after flight: " + first.getDestinationLocation().getName();
            } else {
                return "before flight: " + first.getDestinationLocation().getName();
            }
        } else if (path.size() == 3) {
            return "before & after flights: "
                    + path.get(0).getDestinationLocation().getName() + " &"
                    + path.get(1).getDestinationLocation().getName();
        }
        return "unknown route";
    }

    private SegmentDto toSegmentDto(Transportation transportation) {
        SegmentDto sd = new SegmentDto();
        sd.setOrigin(transportation.getOriginLocation().getName());       // display name
        sd.setDestination(transportation.getDestinationLocation().getName());
        sd.setTransportationType(transportation.getTransportationType().name());
        return sd;
    }
}
