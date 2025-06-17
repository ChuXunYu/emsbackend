package com.dne.ems.dto;

/**
 * DTO for representing a data point on the AQI heatmap.
 *
 * @param gridX       The X-coordinate of the grid cell.
 * @param gridY       The Y-coordinate of the grid cell.
 * @param averageAqi  The average AQI value for this grid cell.
 */
public record AqiHeatmapPointDTO(
    Integer gridX,
    Integer gridY,
    Double averageAqi
) {
    /**
     * 构造函数，用于处理从数据库查询返回的 Number 类型
     */
    public AqiHeatmapPointDTO(Number gridX, Number gridY, Number averageAqi) {
        this(
            gridX != null ? gridX.intValue() : null,
            gridY != null ? gridY.intValue() : null,
            averageAqi != null ? averageAqi.doubleValue() : null
        );
    }
}
