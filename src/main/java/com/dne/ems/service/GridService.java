package com.dne.ems.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dne.ems.dto.GridUpdateRequest;
import com.dne.ems.model.Grid;

/**
 * Service interface for managing grids.
 */
public interface GridService {

    /**
     * Retrieves a paginated list of grids, optionally filtered by city and district.
     *
     * @param cityName     The name of the city to filter by (optional).
     * @param districtName The name of the district to filter by (optional).
     * @param pageable     Pagination information.
     * @return A page of grids matching the criteria.
     */
    Page<Grid> getGrids(String cityName, String districtName, Pageable pageable);

    /**
     * Updates an existing grid.
     *
     * @param gridId  The ID of the grid to update.
     * @param request The request object containing the new data.
     * @return The updated grid.
     */
    Grid updateGrid(Long gridId, GridUpdateRequest request);
} 