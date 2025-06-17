package com.dne.ems.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dne.ems.dto.GridUpdateRequest;
import com.dne.ems.model.Grid;
import com.dne.ems.service.GridService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/grids")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'DECISION_MAKER')")
public class GridController {

    private final GridService gridService;

    /**
     * Retrieves a paginated list of grids, available to ADMIN and DECISION_MAKER roles.
     * Can be filtered by cityName and districtName.
     *
     * @param cityName     The city to filter by.
     * @param districtName The district to filter by.
     * @param pageable     Pagination information.
     * @return A page of Grid objects.
     */
    @GetMapping
    public ResponseEntity<Page<Grid>> getGrids(
            @RequestParam(required = false) String cityName,
            @RequestParam(required = false) String districtName,
            Pageable pageable) {
        Page<Grid> grids = gridService.getGrids(cityName, districtName, pageable);
        return ResponseEntity.ok(grids);
    }

    /**
     * Updates a grid's properties, available only to ADMIN role.
     *
     * @param gridId  The ID of the grid to update.
     * @param request The request body with the update information.
     * @return The updated grid object.
     */
    @PatchMapping("/{gridId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Grid> updateGrid(
            @PathVariable Long gridId,
            @RequestBody @Valid GridUpdateRequest request) {
        Grid updatedGrid = gridService.updateGrid(gridId, request);
        return ResponseEntity.ok(updatedGrid);
    }
} 