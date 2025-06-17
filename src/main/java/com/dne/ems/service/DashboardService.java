package com.dne.ems.service;

import java.util.List;

import com.dne.ems.dto.AqiDistributionDTO;
import com.dne.ems.dto.AqiHeatmapPointDTO;
import com.dne.ems.dto.DashboardStatsDTO;
import com.dne.ems.dto.GridCoverageDTO;
import com.dne.ems.dto.HeatmapPointDTO;
import com.dne.ems.dto.PollutionStatsDTO;
import com.dne.ems.dto.TaskStatsDTO;
import com.dne.ems.dto.TrendDataPointDTO;

/**
 * Service interface for dashboard-related business logic.
 */
public interface DashboardService {

    /**
     * Retrieves pollution statistics.
     *
     * @return A list of {@link PollutionStatsDTO} objects, each representing the count of a specific pollution type.
     */
    List<PollutionStatsDTO> getPollutionStats();

    /**
     * Retrieves task completion statistics.
     *
     * @return A {@link TaskStatsDTO} object containing task statistics.
     */
    TaskStatsDTO getTaskCompletionStats();

    /**
     * Gathers key statistics from across the system for the main dashboard.
     *
     * @return A {@link DashboardStatsDTO} containing the aggregated data.
     */
    DashboardStatsDTO getDashboardStats();

    /**
     * Gathers AQI distribution data for a pie/bar chart.
     *
     * @return A list of {@link AqiDistributionDTO} objects, each representing an AQI level and its count.
     */
    List<AqiDistributionDTO> getAqiDistribution();

    /**
     * Gathers data for the monthly air quality exceedance trend chart.
     *
     * @return A list of {@link TrendDataPointDTO} for the last 12 months.
     */
    List<TrendDataPointDTO> getMonthlyExceedanceTrend();

    /**
     * Gathers grid coverage data, grouped by city.
     *
     * @return A list of {@link GridCoverageDTO} objects, each representing a city and its grid count.
     */
    List<GridCoverageDTO> getGridCoverageByCity();

    /**
     * Gathers data for the pollution source heatmap.
     *
     * @return A list of {@link HeatmapPointDTO} objects, each representing a grid cell and its event intensity.
     */
    List<HeatmapPointDTO> getHeatmapData();

    /**
     * Gathers data for the AQI heatmap.
     *
     * @return A list of {@link AqiHeatmapPointDTO} objects.
     */
    List<AqiHeatmapPointDTO> getAqiHeatmapData();

} 