package com.smartcity.waste.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
// import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;

@Entity
@Table(name = "bins")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WasteBin {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Double latitude;
    
    @Column(nullable = false)
    private Double longitude;
    
    @Column(name = "fill_level", nullable = false)
    private Integer fillLevel; // Percentage: 0-100
    
    @Column(name = "capacity", nullable = false)
    private Integer capacity; // Liters
    
    @Column(name = "location_name")
    private String locationName;
    
    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;
    
    @Column(name = "bin_type")
    private String binType; // e.g., "General", "Recyclable", "Organic"
    
    public boolean isFull() {
        return fillLevel >= 80;
    }
    
    public String getStatus() {
        if (fillLevel < 50) {
            return "LOW";
        } else if (fillLevel < 80) {
            return "MEDIUM";
        } else {
            return "HIGH";
        }
    }
}
