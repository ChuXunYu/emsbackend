package com.dne.ems.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dne.ems.dto.AqiDistributionDTO;
import com.dne.ems.dto.AqiHeatmapPointDTO;
import com.dne.ems.model.AqiRecord;

@Repository
public interface AqiRecordRepository extends JpaRepository<AqiRecord, Long> {

    @Query("SELECT new com.dne.ems.dto.AqiDistributionDTO(" +
           "CASE " +
           "WHEN ar.aqiValue <= 50 THEN 'Good' " +
           "WHEN ar.aqiValue <= 100 THEN 'Moderate' " +
           "WHEN ar.aqiValue <= 150 THEN 'Unhealthy for Sensitive Groups' " +
           "WHEN ar.aqiValue <= 200 THEN 'Unhealthy' " +
           "WHEN ar.aqiValue <= 300 THEN 'Very Unhealthy' " +
           "ELSE 'Hazardous' END, " +
           "COUNT(ar.id)) " +
           "FROM AqiRecord ar " +
           "GROUP BY CASE " +
           "WHEN ar.aqiValue <= 50 THEN 'Good' " +
           "WHEN ar.aqiValue <= 100 THEN 'Moderate' " +
           "WHEN ar.aqiValue <= 150 THEN 'Unhealthy for Sensitive Groups' " +
           "WHEN ar.aqiValue <= 200 THEN 'Unhealthy' " +
           "WHEN ar.aqiValue <= 300 THEN 'Very Unhealthy' " +
           "ELSE 'Hazardous' END")
    List<AqiDistributionDTO> getAqiDistribution();
    
    @Query(value = "SELECT DATE_FORMAT(record_time, '%Y-%m') as yearMonth, COUNT(id) as count " +
           "FROM aqi_records " +
           "WHERE aqi_value > 100 AND record_time >= :startDate " +
           "GROUP BY DATE_FORMAT(record_time, '%Y-%m') " +
           "ORDER BY 1", nativeQuery = true)
    List<Object[]> getMonthlyExceedanceTrendRaw(@Param("startDate") LocalDateTime startDate);
    
    @Query("SELECT new com.dne.ems.dto.AqiHeatmapPointDTO(ar.gridX, ar.gridY, AVG(ar.aqiValue)) " +
           "FROM AqiRecord ar " +
           "WHERE ar.gridX IS NOT NULL AND ar.gridY IS NOT NULL " +
           "GROUP BY ar.gridX, ar.gridY")
    List<AqiHeatmapPointDTO> getAqiHeatmapData();
    
    @Query(value = "SELECT grid_x, grid_y, AVG(aqi_value) as avg_aqi " +
           "FROM aqi_records " +
           "WHERE grid_x IS NOT NULL AND grid_y IS NOT NULL " +
           "GROUP BY grid_x, grid_y", nativeQuery = true)
    List<Object[]> getAqiHeatmapDataRaw();
}