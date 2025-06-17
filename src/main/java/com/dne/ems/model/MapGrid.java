package com.dne.ems.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a single cell in the grid-based map, specifically for A* pathfinding.
 * Each cell has coordinates and can be marked as an obstacle. A unique constraint
 * on (x, y) coordinates ensures that each position is represented only once.
 * Maps to the 'map_grid' table.
 *
 * @version 1.0
 * @since 2025-06-16
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "map_grid", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"x", "y"})
})
public class MapGrid {

    /**
     * The unique identifier for the map grid cell.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The x-coordinate of the grid cell.
     */
    private int x;

    /**
     * The y-coordinate of the grid cell.
     */
    private int y;

    /**
     * A flag indicating if this grid cell is an obstacle (e.g., a wall, a river).
     * The A* pathfinding algorithm cannot traverse through cells marked as obstacles.
     */
    private boolean isObstacle;

    /**
     * Optional: Describes the type of terrain (e.g., "road", "grass", "water").
     * This could be used in more advanced pathfinding algorithms to influence movement cost.
     */
    private String terrainType;
} 