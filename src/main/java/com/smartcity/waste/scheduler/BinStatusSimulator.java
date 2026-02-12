package com.smartcity.waste.scheduler;

import com.smartcity.waste.entity.WasteBin;
import com.smartcity.waste.repository.WasteBinRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

/**
 * Background scheduler that simulates real-world bin fill level changes
 * Runs every 30 seconds (configurable via application.properties)
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class BinStatusSimulator {
    
    private final WasteBinRepository wasteBinRepository;
    private final Random random = new Random();
    
    /**
     * Simulates bin fill level changes
     * - Randomly increases fill levels (simulating waste accumulation)
     * - Occasionally resets bins to 0% (simulating collection)
     */
    @Scheduled(fixedDelayString = "${app.bin.simulation.interval:30000}")
    public void simulateBinFillLevels() {
        List<WasteBin> bins = wasteBinRepository.findAll();
        
        if (bins.isEmpty()) {
            return;
        }
        
        int updatedCount = 0;
        
        for (WasteBin bin : bins) {
            // 10% chance to empty the bin (simulating collection)
            if (random.nextInt(100) < 10) {
                bin.setFillLevel(0);
                bin.setLastUpdated(LocalDateTime.now());
                updatedCount++;
            }
            // Otherwise, increase fill level
            else if (bin.getFillLevel() < 100) {
                // Increase by 1-15%
                int increase = random.nextInt(15) + 1;
                int newLevel = Math.min(100, bin.getFillLevel() + increase);
                bin.setFillLevel(newLevel);
                bin.setLastUpdated(LocalDateTime.now());
                updatedCount++;
            }
        }
        
        if (updatedCount > 0) {
            wasteBinRepository.saveAll(bins);
            log.info("Bin status simulation: Updated {} bins", updatedCount);
        }
    }
}
