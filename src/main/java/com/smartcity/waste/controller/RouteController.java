package com.smartcity.waste.controller;

import com.smartcity.waste.dto.RouteResponse;
import com.smartcity.waste.service.RouteOptimizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/routes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RouteController {
    
    private final RouteOptimizationService routeOptimizationService;
    
    @PostMapping("/optimize")
    public ResponseEntity<RouteResponse> generateOptimizedRoute() {
        RouteResponse route = routeOptimizationService.generateOptimizedRoute();
        return ResponseEntity.ok(route);
    }
    
    @GetMapping("/recent")
    public ResponseEntity<List<RouteResponse>> getRecentRoutes() {
        return ResponseEntity.ok(routeOptimizationService.getRecentRoutes());
    }
}
