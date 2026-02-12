package com.smartcity.waste.repository;

import com.smartcity.waste.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
    
    List<Route> findByStatus(String status);
    
    List<Route> findTop10ByOrderByCreatedAtDesc();
}
