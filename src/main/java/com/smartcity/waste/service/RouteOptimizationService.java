package com.smartcity.waste.service;

import com.smartcity.waste.dto.RouteResponse;
import com.smartcity.waste.dto.WasteBinDTO;
import com.smartcity.waste.entity.Route;
import com.smartcity.waste.entity.WasteBin;
import com.smartcity.waste.repository.RouteRepository;
import com.smartcity.waste.repository.WasteBinRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RouteOptimizationService {
    
    private final WasteBinRepository wasteBinRepository;
    private final RouteRepository routeRepository;
    
    @Value("${app.depot.latitude:40.7128}")
    private Double depotLatitude;
    
    @Value("${app.depot.longitude:-74.0060}")
    private Double depotLongitude;
    
    /**
     * Generates an optimized route using Nearest Neighbor Algorithm (Greedy TSP Approximation)
     * This is a simple but effective approach for route optimization
     */
    @Transactional
    public RouteResponse generateOptimizedRoute() {
        // Get all full bins (fill level >= 80%)
        List<WasteBin> fullBins = wasteBinRepository.findFullBins();
        
        if (fullBins.isEmpty()) {
            log.info("No full bins found for route optimization");
            return new RouteResponse(null, Collections.emptyList(), 0.0, 0, "NO_BINS");
        }
        
        log.info("Found {} full bins for route optimization", fullBins.size());
        
        // Apply Nearest Neighbor Algorithm
        List<WasteBin> optimizedRoute = nearestNeighborTSP(fullBins, depotLatitude, depotLongitude);
        
        // Calculate total distance
        double totalDistance = calculateTotalDistance(optimizedRoute, depotLatitude, depotLongitude);
        
        // Estimate time (assuming average speed of 30 km/h and 5 minutes per bin)
        int estimatedTime = (int) ((totalDistance / 30.0) * 60) + (optimizedRoute.size() * 5);
        
        // Save route to database
        Route route = new Route();
        route.setCreatedAt(LocalDateTime.now());
        route.setTotalDistance(totalDistance);
        route.setEstimatedTime(estimatedTime);
        route.setBinIds(optimizedRoute.stream().map(WasteBin::getId).collect(Collectors.toList()));
        route.setStatus("PLANNED");
        
        Route savedRoute = routeRepository.save(route);
        
        // Prepare response
        List<WasteBinDTO> binDTOs = optimizedRoute.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        return new RouteResponse(
                savedRoute.getId(),
                binDTOs,
                totalDistance,
                estimatedTime,
                "PLANNED"
        );
    }
    
    /**
     * Nearest Neighbor Algorithm for TSP
     * Time Complexity: O(n^2) where n is the number of bins
     * 
     * Algorithm:
     * 1. Start at depot
     * 2. Find nearest unvisited bin
     * 3. Move to that bin
     * 4. Repeat until all bins are visited
     */
    private List<WasteBin> nearestNeighborTSP(List<WasteBin> bins, double startLat, double startLon) {
        List<WasteBin> route = new ArrayList<>();
        Set<Long> visited = new HashSet<>();
        
        double currentLat = startLat;
        double currentLon = startLon;
        
        while (visited.size() < bins.size()) {
            WasteBin nearest = null;
            double minDistance = Double.MAX_VALUE;
            
            // Find the nearest unvisited bin
            for (WasteBin bin : bins) {
                if (!visited.contains(bin.getId())) {
                    double distance = calculateHaversineDistance(
                            currentLat, currentLon,
                            bin.getLatitude(), bin.getLongitude()
                    );
                    
                    if (distance < minDistance) {
                        minDistance = distance;
                        nearest = bin;
                    }
                }
            }
            
            if (nearest != null) {
                route.add(nearest);
                visited.add(nearest.getId());
                currentLat = nearest.getLatitude();
                currentLon = nearest.getLongitude();
            }
        }
        
        return route;
    }
    
    /**
     * Calculate total route distance including return to depot
     */
    private double calculateTotalDistance(List<WasteBin> route, double depotLat, double depotLon) {
        if (route.isEmpty()) {
            return 0.0;
        }
        
        double totalDistance = 0.0;
        
        // Distance from depot to first bin
        totalDistance += calculateHaversineDistance(
                depotLat, depotLon,
                route.get(0).getLatitude(), route.get(0).getLongitude()
        );
        
        // Distance between consecutive bins
        for (int i = 0; i < route.size() - 1; i++) {
            WasteBin current = route.get(i);
            WasteBin next = route.get(i + 1);
            totalDistance += calculateHaversineDistance(
                    current.getLatitude(), current.getLongitude(),
                    next.getLatitude(), next.getLongitude()
            );
        }
        
        // Distance from last bin back to depot
        WasteBin lastBin = route.get(route.size() - 1);
        totalDistance += calculateHaversineDistance(
                lastBin.getLatitude(), lastBin.getLongitude(),
                depotLat, depotLon
        );
        
        return totalDistance;
    }
    
    /**
     * Haversine Formula for calculating distance between two lat/lon points
     * Returns distance in kilometers
     */
    private double calculateHaversineDistance(double lat1, double lon1, double lat2, double lon2) {
        final int EARTH_RADIUS_KM = 6371;
        
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return EARTH_RADIUS_KM * c;
    }
    
    @Transactional(readOnly = true)
    public List<RouteResponse> getRecentRoutes() {
        List<Route> routes = routeRepository.findTop10ByOrderByCreatedAtDesc();
        return routes.stream()
                .map(this::convertRouteToResponse)
                .collect(Collectors.toList());
    }
    
    private RouteResponse convertRouteToResponse(Route route) {
        List<WasteBinDTO> bins = route.getBinIds().stream()
                .map(wasteBinRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        return new RouteResponse(
                route.getId(),
                bins,
                route.getTotalDistance(),
                route.getEstimatedTime(),
                route.getStatus()
        );
    }
    
    private WasteBinDTO convertToDTO(WasteBin bin) {
        WasteBinDTO dto = new WasteBinDTO();
        dto.setId(bin.getId());
        dto.setLatitude(bin.getLatitude());
        dto.setLongitude(bin.getLongitude());
        dto.setFillLevel(bin.getFillLevel());
        dto.setCapacity(bin.getCapacity());
        dto.setLocationName(bin.getLocationName());
        dto.setStatus(bin.getStatus());
        dto.setBinType(bin.getBinType());
        return dto;
    }
}
