package com.dne.ems.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dne.ems.dto.GridCoverageDTO;
import com.dne.ems.model.Grid;

/**
 * Spring Data JPA repository for the {@link Grid} entity.
 */
@Repository
public interface GridRepository extends JpaRepository<Grid, Long>, JpaSpecificationExecutor<Grid> {

    /**
     * Finds a grid cell by its X and Y coordinates.
     *
     * @param gridX The X coordinate of the grid.
     * @param gridY The Y coordinate of the grid.
     * @return An {@link Optional} containing the found grid cell, or {@link Optional#empty()} if not found.
     */
    Optional<Grid> findByGridXAndGridY(Integer gridX, Integer gridY);

    @Query("""
            SELECT new com.dne.ems.dto.GridCoverageDTO(g.cityName, COUNT(g.id))
            FROM Grid g
            WHERE g.cityName IS NOT NULL AND g.cityName != ''
            GROUP BY g.cityName
            ORDER BY COUNT(g.id) DESC
            """)
    List<GridCoverageDTO> getGridCoverageByCity();

    List<Grid> findByIsObstacle(boolean isObstacle);
} 