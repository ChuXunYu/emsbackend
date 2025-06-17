package com.dne.ems.dto;

/**
 * DTO for carrying key statistics for the main dashboard.
 *
 * @param totalFeedbacks Total number of feedback submissions.
 * @param confirmedFeedbacks Total number of feedback entries that have been confirmed.
 * @param totalAqiRecords Total number of AQI data records submitted.
 * @param activeGridWorkers Number of grid workers with an 'ACTIVE' status.
 */
public record DashboardStatsDTO(
    long totalFeedbacks,
    long confirmedFeedbacks,
    long totalAqiRecords,
    long activeGridWorkers
) {
} 