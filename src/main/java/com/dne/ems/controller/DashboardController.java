package com.dne.ems.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dne.ems.dto.AqiDistributionDTO;
import com.dne.ems.dto.AqiHeatmapPointDTO;
import com.dne.ems.dto.DashboardStatsDTO;
import com.dne.ems.dto.GridCoverageDTO;
import com.dne.ems.dto.HeatmapPointDTO;
import com.dne.ems.dto.PollutionStatsDTO;
import com.dne.ems.dto.TaskStatsDTO;
import com.dne.ems.dto.TrendDataPointDTO;
import com.dne.ems.service.DashboardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;
    private static final Logger log = LoggerFactory.getLogger(DashboardController.class);

    @GetMapping("/stats")
    @PreAuthorize("hasAnyRole('DECISION_MAKER', 'ADMIN')")
    public ResponseEntity<DashboardStatsDTO> getDashboardStats() {
        DashboardStatsDTO stats = dashboardService.getDashboardStats();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/reports/aqi-distribution")
    @PreAuthorize("hasAnyRole('DECISION_MAKER', 'ADMIN')")
    public ResponseEntity<List<AqiDistributionDTO>> getAqiDistribution() {
        List<AqiDistributionDTO> distribution = dashboardService.getAqiDistribution();
        return ResponseEntity.ok(distribution);
    }

    @GetMapping("/reports/pollution-trend")
    @PreAuthorize("hasAnyRole('DECISION_MAKER', 'ADMIN')")
    public ResponseEntity<List<TrendDataPointDTO>> getMonthlyExceedanceTrend() {
        List<TrendDataPointDTO> trend = dashboardService.getMonthlyExceedanceTrend();
        return ResponseEntity.ok(trend);
    }

    @GetMapping("/reports/grid-coverage")
    @PreAuthorize("hasAnyRole('DECISION_MAKER', 'ADMIN')")
    public ResponseEntity<List<GridCoverageDTO>> getGridCoverage() {
        List<GridCoverageDTO> coverage = dashboardService.getGridCoverageByCity();
        return ResponseEntity.ok(coverage);
    }

    @GetMapping("/map/heatmap")
    @PreAuthorize("hasAnyRole('DECISION_MAKER', 'ADMIN')")
    public ResponseEntity<List<HeatmapPointDTO>> getHeatmapData() {
        List<HeatmapPointDTO> heatmapData = dashboardService.getHeatmapData();
        if (heatmapData == null || heatmapData.isEmpty()) {
            log.warn("反馈热力图数据为空");
        } else {
            log.info("成功获取反馈热力图数据，共 {} 个数据点", heatmapData.size());
        }
        return ResponseEntity.ok(heatmapData);
    }

    @GetMapping("/reports/pollution-stats")
    @PreAuthorize("hasAnyRole('DECISION_MAKER', 'ADMIN')")
    public ResponseEntity<List<PollutionStatsDTO>> getPollutionStats() {
        List<PollutionStatsDTO> stats = dashboardService.getPollutionStats();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/reports/task-completion-stats")
    @PreAuthorize("hasAnyRole('DECISION_MAKER', 'ADMIN')")
    public ResponseEntity<TaskStatsDTO> getTaskCompletionStats() {
        TaskStatsDTO stats = dashboardService.getTaskCompletionStats();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/map/aqi-heatmap")
    @PreAuthorize("hasAnyRole('DECISION_MAKER', 'ADMIN')")
    public ResponseEntity<List<AqiHeatmapPointDTO>> getAqiHeatmapData() {
        List<AqiHeatmapPointDTO> heatmapData = dashboardService.getAqiHeatmapData();
        if (heatmapData == null || heatmapData.isEmpty()) {
            log.warn("AQI热力图数据为空");
        } else {
            log.info("成功获取AQI热力图数据，共 {} 个数据点", heatmapData.size());
        }
        return ResponseEntity.ok(heatmapData);
    }
}