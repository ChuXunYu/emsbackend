package com.dne.ems.dto;

/**
 * DTO for representing grid coverage statistics for a specific area.
 *
 * @param areaName The name of the administrative area (e.g., city name).
 * @param gridCount The number of grid cells within that area.
 */
public record GridCoverageDTO(
    String areaName,
    long gridCount
) {
} 