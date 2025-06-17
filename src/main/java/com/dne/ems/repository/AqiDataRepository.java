package com.dne.ems.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dne.ems.dto.AqiDistributionDTO;
import com.dne.ems.dto.AqiHeatmapPointDTO;
import com.dne.ems.dto.TrendDataPointDTO;
import com.dne.ems.model.AqiData;

/**
 * JPA Repository for {@link AqiData} entities.
 */
@Repository
public interface AqiDataRepository extends JpaRepository<AqiData, Long> {

    @Query("""
            SELECT new com.dne.ems.dto.AqiDistributionDTO(
                CASE
                    WHEN a.aqiValue <= 50 THEN 'Good'
                    WHEN a.aqiValue <= 100 THEN 'Moderate'
                    WHEN a.aqiValue <= 150 THEN 'Unhealthy for Sensitive Groups'
                    WHEN a.aqiValue <= 200 THEN 'Unhealthy'
                    WHEN a.aqiValue <= 300 THEN 'Very Unhealthy'
                    ELSE 'Hazardous'
                END,
                COUNT(a.id)
            )
            FROM AqiData a
            GROUP BY 1
            """)
    List<AqiDistributionDTO> getAqiDistribution();

    @Query(value = "SELECT DATE_FORMAT(record_time, '%Y-%m') as yearMonth, COUNT(id) as count " +
           "FROM aqi_data " +
           "WHERE aqi_value > 100 AND record_time >= :startDate " +
           "GROUP BY DATE_FORMAT(record_time, '%Y-%m') " +
           "ORDER BY 1", nativeQuery = true)
    List<Object[]> getMonthlyExceedanceTrendRaw(@Param("startDate") LocalDateTime startDate);

    default List<TrendDataPointDTO> getMonthlyExceedanceTrend(LocalDateTime startDate) {
        List<Object[]> rawResults = getMonthlyExceedanceTrendRaw(startDate);
        return rawResults.stream()
                .map(row -> new TrendDataPointDTO(
                        (String) row[0],
                        ((Number) row[1]).longValue()))
                .toList();
    }

    @Query("""
            SELECT new com.dne.ems.dto.AqiHeatmapPointDTO(g.gridX, g.gridY, AVG(d.aqiValue))
            FROM AqiData d JOIN d.grid g
            WHERE g.gridX IS NOT NULL AND g.gridY IS NOT NULL
            GROUP BY g.gridX, g.gridY
            """)
    List<AqiHeatmapPointDTO> getAqiHeatmapData();
}