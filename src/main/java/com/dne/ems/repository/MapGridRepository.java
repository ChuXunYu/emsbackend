package com.dne.ems.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dne.ems.model.MapGrid;

/**
 * Repository for {@link MapGrid} entities.
 */
@Repository
public interface MapGridRepository extends JpaRepository<MapGrid, Long> {

    /**
     * Finds a grid cell by its x and y coordinates.
     *
     * @param x the x-coordinate.
     * @param y the y-coordinate.
     * @return an Optional containing the MapGrid cell if found.
     */
    Optional<MapGrid> findByXAndY(int x, int y);

    /**
     * Retrieves all grid cells, ordered by their y-coordinate, then by their x-coordinate.
     * This provides a consistent, traversable order for rebuilding the map.
     *
     * @return a list of all MapGrid cells.
     */
    List<MapGrid> findAllByOrderByYAscXAsc();
} 