package com.dne.ems.dto;

/**
 * DTO for representing a single data point on a heatmap.
 *
 * @param gridX The X-coordinate of the grid cell.
 * @param gridY The Y-coordinate of the grid cell.
 * @param intensity The intensity value for this point (e.g., number of feedback events).
 */
public record HeatmapPointDTO(
    Integer gridX,
    Integer gridY,
    long intensity
) {
} 