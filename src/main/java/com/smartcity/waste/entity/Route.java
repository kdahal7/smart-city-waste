package com.smartcity.waste.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "routes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Route {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "total_distance")
    private Double totalDistance; // in kilometers
    
    @Column(name = "estimated_time")
    private Integer estimatedTime; // in minutes
    
    @ElementCollection
    @CollectionTable(name = "route_waypoints", joinColumns = @JoinColumn(name = "route_id"))
    @Column(name = "bin_id")
    private List<Long> binIds = new ArrayList<>();
    
    @Column(name = "status")
    private String status; // "PLANNED", "IN_PROGRESS", "COMPLETED"
}
