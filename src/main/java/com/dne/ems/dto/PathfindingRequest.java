package com.dne.ems.dto;


import lombok.Data;

/**
 * A DTO for requesting a path from the A* service.
 */
@Data
public class PathfindingRequest {
    
    /**
     * The starting point for the pathfinding.
     */
    private int startX;
    private int startY;

    /**
     * The target (end) point for the pathfinding.
     */
    private int endX;
    private int endY;
} 