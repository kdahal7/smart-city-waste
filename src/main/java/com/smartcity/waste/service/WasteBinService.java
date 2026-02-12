package com.smartcity.waste.service;

import com.smartcity.waste.dto.DashboardStats;
import com.smartcity.waste.dto.WasteBinDTO;
import com.smartcity.waste.entity.WasteBin;
import com.smartcity.waste.repository.WasteBinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WasteBinService {
    
    private final WasteBinRepository wasteBinRepository;
    
    @Transactional(readOnly = true)
    public List<WasteBinDTO> getAllBins() {
        return wasteBinRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<WasteBinDTO> getFullBins() {
        return wasteBinRepository.findFullBins().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public WasteBinDTO getBinById(Long id) {
        WasteBin bin = wasteBinRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bin not found with id: " + id));
        return convertToDTO(bin);
    }
    
    @Transactional
    public WasteBinDTO createBin(WasteBinDTO binDTO) {
        WasteBin bin = new WasteBin();
        bin.setLatitude(binDTO.getLatitude());
        bin.setLongitude(binDTO.getLongitude());
        bin.setFillLevel(binDTO.getFillLevel() != null ? binDTO.getFillLevel() : 0);
        bin.setCapacity(binDTO.getCapacity() != null ? binDTO.getCapacity() : 240);
        bin.setLocationName(binDTO.getLocationName());
        bin.setBinType(binDTO.getBinType() != null ? binDTO.getBinType() : "General");
        bin.setLastUpdated(LocalDateTime.now());
        
        WasteBin saved = wasteBinRepository.save(bin);
        return convertToDTO(saved);
    }
    
    @Transactional
    public WasteBinDTO updateBinFillLevel(Long id, Integer fillLevel) {
        WasteBin bin = wasteBinRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bin not found with id: " + id));
        
        bin.setFillLevel(Math.min(100, Math.max(0, fillLevel)));
        bin.setLastUpdated(LocalDateTime.now());
        
        WasteBin updated = wasteBinRepository.save(bin);
        return convertToDTO(updated);
    }
    
    @Transactional(readOnly = true)
    public DashboardStats getDashboardStats() {
        List<WasteBin> allBins = wasteBinRepository.findAll();
        
        long totalBins = allBins.size();
        long lowFillBins = allBins.stream().filter(b -> b.getFillLevel() < 50).count();
        long mediumFillBins = allBins.stream().filter(b -> b.getFillLevel() >= 50 && b.getFillLevel() < 80).count();
        long highFillBins = allBins.stream().filter(b -> b.getFillLevel() >= 80).count();
        
        double averageFillLevel = allBins.stream()
                .mapToInt(WasteBin::getFillLevel)
                .average()
                .orElse(0.0);
        
        return new DashboardStats(totalBins, lowFillBins, mediumFillBins, highFillBins, averageFillLevel);
    }
    
    @Transactional
    public void deleteBin(Long id) {
        wasteBinRepository.deleteById(id);
    }
    
    private WasteBinDTO convertToDTO(WasteBin bin) {
        WasteBinDTO dto = new WasteBinDTO();
        dto.setId(bin.getId());
        dto.setLatitude(bin.getLatitude());
        dto.setLongitude(bin.getLongitude());
        dto.setFillLevel(bin.getFillLevel());
        dto.setCapacity(bin.getCapacity());
        dto.setLocationName(bin.getLocationName());
        dto.setStatus(bin.getStatus());
        dto.setBinType(bin.getBinType());
        return dto;
    }
}
