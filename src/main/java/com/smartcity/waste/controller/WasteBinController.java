package com.smartcity.waste.controller;

import com.smartcity.waste.dto.DashboardStats;
import com.smartcity.waste.dto.WasteBinDTO;
import com.smartcity.waste.service.WasteBinService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bins")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class WasteBinController {
    
    private final WasteBinService wasteBinService;
    
    @GetMapping
    public ResponseEntity<List<WasteBinDTO>> getAllBins() {
        return ResponseEntity.ok(wasteBinService.getAllBins());
    }
    
    @GetMapping("/full")
    public ResponseEntity<List<WasteBinDTO>> getFullBins() {
        return ResponseEntity.ok(wasteBinService.getFullBins());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<WasteBinDTO> getBinById(@PathVariable Long id) {
        return ResponseEntity.ok(wasteBinService.getBinById(id));
    }
    
    @PostMapping
    public ResponseEntity<WasteBinDTO> createBin(@RequestBody WasteBinDTO binDTO) {
        WasteBinDTO created = wasteBinService.createBin(binDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @PutMapping("/{id}/fill-level")
    public ResponseEntity<WasteBinDTO> updateFillLevel(
            @PathVariable Long id,
            @RequestParam Integer fillLevel) {
        return ResponseEntity.ok(wasteBinService.updateBinFillLevel(id, fillLevel));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBin(@PathVariable Long id) {
        wasteBinService.deleteBin(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/stats")
    public ResponseEntity<DashboardStats> getDashboardStats() {
        return ResponseEntity.ok(wasteBinService.getDashboardStats());
    }
}
