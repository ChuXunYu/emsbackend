package com.dne.ems.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating a grid's properties.
 *
 * @param isObstacle The new obstacle status for the grid.
 * @param description The new description for the grid.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GridUpdateRequest {

    @NotNull(message = "Obstacle status cannot be null")
    private Boolean isObstacle;
    private String description;
} 