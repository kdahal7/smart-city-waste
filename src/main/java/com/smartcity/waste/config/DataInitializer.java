package com.smartcity.waste.config;

import com.smartcity.waste.entity.WasteBin;
import com.smartcity.waste.repository.WasteBinRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Initializes the database with sample waste bins if empty
 * Creates bins in a grid pattern around New York City (can be customized)
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    
    private final WasteBinRepository wasteBinRepository;
    private final Random random = new Random();
    
    @Override
    public void run(String... args) {
        if (wasteBinRepository.count() == 0) {
            log.info("Initializing database with sample waste bins...");
            createSampleBins();
            log.info("Database initialization complete!");
        } else {
            log.info("Database already contains {} bins", wasteBinRepository.count());
        }
    }
    
    private void createSampleBins() {
        List<WasteBin> bins = new ArrayList<>();
        
        // Base coordinates (New York City area)
        double baseLat = 40.7128;
        double baseLon = -74.0060;
        
        // Create a grid of bins
        for (int i = 0; i < 50; i++) {
            WasteBin bin = new WasteBin();
            
            // Spread bins in a ~10km x 10km area
            double latOffset = (random.nextDouble() - 0.5) * 0.1; // ~11km
            double lonOffset = (random.nextDouble() - 0.5) * 0.1;
            
            bin.setLatitude(baseLat + latOffset);
            bin.setLongitude(baseLon + lonOffset);
            bin.setFillLevel(random.nextInt(101)); // 0-100%
            bin.setCapacity(240); // 240 liters standard bin
            bin.setLocationName(generateLocationName(i));
            bin.setBinType(getRandomBinType());
            bin.setLastUpdated(LocalDateTime.now());
            
            bins.add(bin);
        }
        
        wasteBinRepository.saveAll(bins);
        log.info("Created {} sample bins", bins.size());
    }
    
    private String generateLocationName(int index) {
        String[] streets = {
            "5th Avenue", "Broadway", "Park Avenue", "Madison Avenue",
            "Lexington Avenue", "7th Avenue", "8th Avenue", "Amsterdam Avenue",
            "Columbus Avenue", "Central Park West", "Wall Street", "Houston Street",
            "Bleecker Street", "Canal Street", "Spring Street"
        };
        
        String[] locations = {
            "Corner", "Near Park", "Shopping District", "Residential Area",
            "Business District", "Market Square", "Transit Hub", "City Center"
        };
        
        String street = streets[random.nextInt(streets.length)];
        String location = locations[random.nextInt(locations.length)];
        
        return street + " - " + location + " #" + (index + 1);
    }
    
    private String getRandomBinType() {
        String[] types = {"General", "Recyclable", "Organic"};
        return types[random.nextInt(types.length)];
    }
}
