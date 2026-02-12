package com.smartcity.waste.repository;

import com.smartcity.waste.entity.WasteBin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WasteBinRepository extends JpaRepository<WasteBin, Long> {
    
    // Find all bins with fill level above threshold
    @Query("SELECT b FROM WasteBin b WHERE b.fillLevel >= :threshold")
    List<WasteBin> findBinsByFillLevelGreaterThanEqual(@Param("threshold") Integer threshold);
    
    // Find bins near a location using Haversine formula approximation
    @Query(value = "SELECT * FROM bins WHERE " +
           "(6371 * acos(cos(radians(:lat)) * cos(radians(latitude)) * " +
           "cos(radians(longitude) - radians(:lon)) + sin(radians(:lat)) * " +
           "sin(radians(latitude)))) <= :radius", 
           nativeQuery = true)
    List<WasteBin> findBinsWithinRadius(
        @Param("lat") Double latitude, 
        @Param("lon") Double longitude, 
        @Param("radius") Double radiusKm
    );
    
    // Find full bins (fill level >= 80%)
    @Query("SELECT b FROM WasteBin b WHERE b.fillLevel >= 80")
    List<WasteBin> findFullBins();
    
    // Count bins by status
    @Query("SELECT " +
           "SUM(CASE WHEN b.fillLevel < 50 THEN 1 ELSE 0 END) as low, " +
           "SUM(CASE WHEN b.fillLevel >= 50 AND b.fillLevel < 80 THEN 1 ELSE 0 END) as medium, " +
           "SUM(CASE WHEN b.fillLevel >= 80 THEN 1 ELSE 0 END) as high " +
           "FROM WasteBin b")
    Object[] getBinStatusCounts();
}
