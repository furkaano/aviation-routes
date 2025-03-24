package com.example.aviationroutes.controller;

import com.example.aviationroutes.dto.RouteDto;
import com.example.aviationroutes.service.RouteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/routes")
public class RouteController {

    private final RouteService routeService;

    public RouteController(RouteService routeService){
        this.routeService = routeService;
    }

    @GetMapping
    public ResponseEntity<List<RouteDto>> getAllValidRoutes(@RequestParam Long origin, @RequestParam Long destination){
        List<RouteDto> routes = routeService.findAllValidRoutes(origin, destination);
        return ResponseEntity.ok(routes);
    }
}
