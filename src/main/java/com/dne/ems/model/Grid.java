package com.dne.ems.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * Represents a geographical grid cell used for regional management and data aggregation.
 * Each grid cell can be associated with a city and district and can be marked as an obstacle.
 * Maps to the 'grid' table.
 *
 * @version 1.0
 * @since 2025-06-16
 */
@Entity
@Table(name = "grid")
@Data
public class Grid {

    /**
     * The unique identifier for the grid cell.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The x-coordinate of the grid cell in the map.
     */
    @Column(nullable = false)
    private Integer gridX;

    /**
     * The y-coordinate of the grid cell in the map.
     */
    @Column(nullable = false)
    private Integer gridY;

    /**
     * The name of the city this grid cell belongs to.
     */
    private String cityName;

    /**
     * The name of the district this grid cell belongs to.
     */
    private String districtName;

    /**
     * A textual description of the grid cell, which can include notes or details.
     * Stored as a large object (TEXT/CLOB).
     */
    @Column(columnDefinition = "TEXT")
    private String description;

    /**
     * A flag indicating if this grid cell is an obstacle (e.g., a restricted area).
     * Defaults to false.
     */
    @Column(nullable = false)
    private Boolean isObstacle = false;
} 