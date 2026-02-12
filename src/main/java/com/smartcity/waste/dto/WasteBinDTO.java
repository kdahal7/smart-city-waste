package com.smartcity.waste.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WasteBinDTO {
    private Long id;
    private Double latitude;
    private Double longitude;
    private Integer fillLevel;
    private Integer capacity;
    private String locationName;
    private String status;
    private String binType;
}
