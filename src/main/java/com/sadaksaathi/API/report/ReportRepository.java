package com.sadaksaathi.API.report;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    
    // NEW: Find reports within a certain radius (in kilometers)
    // Note: This only works for reports that have latitude/longitude data
    @Query(value = "SELECT * FROM reports r WHERE " +
            "r.latitude IS NOT NULL AND r.longitude IS NOT NULL AND " +
            "6371 * acos(cos(radians(:lat)) * cos(radians(r.latitude)) * " +
            "cos(radians(r.longitude) - radians(:lng)) + " +
            "sin(radians(:lat)) * sin(radians(r.latitude))) <= :radius", 
            nativeQuery = true)
    List<Report> findReportsWithinRadius(@Param("lat") Double latitude, 
                                        @Param("lng") Double longitude, 
                                        @Param("radius") Double radiusInKm);
                                
    
    // Custom queries can be added later
    List<Report> findByUserEmail(String userEmail);

}