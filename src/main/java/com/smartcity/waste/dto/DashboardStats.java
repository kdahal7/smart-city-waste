package com.smartcity.waste.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStats {
    private Long totalBins;
    private Long lowFillBins;
    private Long mediumFillBins;
    private Long highFillBins;
    private Double averageFillLevel;
}
