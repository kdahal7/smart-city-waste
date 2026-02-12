package com.smartcity.waste.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteResponse {
    private Long routeId;
    private List<WasteBinDTO> bins;
    private Double totalDistance; // in kilometers
    private Integer estimatedTime; // in minutes
    private String status;
}
