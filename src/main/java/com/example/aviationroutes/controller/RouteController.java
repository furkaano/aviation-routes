package com.example.aviationroutes.controller;

import com.example.aviationroutes.dto.RouteDto;
import com.example.aviationroutes.service.RouteService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/routes")
@CrossOrigin(origins = "http://localhost:3000")
public class RouteController {

    private final RouteService routeService;

    public RouteController(RouteService routeService){
        this.routeService = routeService;
    }

    @Operation(summary = "Get all valid routes at once",
            description = "Get all valid routes at once")
    @GetMapping
    public ResponseEntity<List<RouteDto>> getAllValidRoutes(@RequestParam Long origin, @RequestParam Long destination){
        List<RouteDto> routes = routeService.findAllValidRoutes(origin, destination);
        return ResponseEntity.ok(routes);
    }
}
