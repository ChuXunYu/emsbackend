package com.dne.ems.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dne.ems.model.MapGrid;
import com.dne.ems.repository.MapGridRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/map")
@RequiredArgsConstructor
public class MapController {

    private final MapGridRepository mapGridRepository;

    @GetMapping("/grid")
    public ResponseEntity<List<MapGrid>> getFullMap() {
        List<MapGrid> mapGrids = mapGridRepository.findAllByOrderByYAscXAsc();
        return ResponseEntity.ok(mapGrids);
    }

    @PostMapping("/grid")
    @Transactional
    public ResponseEntity<MapGrid> createOrUpdateGridCell(@RequestBody MapGrid gridCellRequest) {
        // Simple validation
        if (gridCellRequest.getX() < 0 || gridCellRequest.getY() < 0) {
            return ResponseEntity.badRequest().build();
        }

        MapGrid gridCell = mapGridRepository.findByXAndY(gridCellRequest.getX(), gridCellRequest.getY())
                .orElse(new MapGrid());

        gridCell.setX(gridCellRequest.getX());
        gridCell.setY(gridCellRequest.getY());
        gridCell.setObstacle(gridCellRequest.isObstacle());
        gridCell.setTerrainType(gridCellRequest.getTerrainType());
        
        MapGrid savedCell = mapGridRepository.save(gridCell);
        return ResponseEntity.ok(savedCell);
    }

    @PostMapping("/initialize")
    @Transactional
    public ResponseEntity<String> initializeMap(
            @RequestParam(defaultValue = "20") int width,
            @RequestParam(defaultValue = "20") int height) {
        
        mapGridRepository.deleteAll(); // Clear existing map
        mapGridRepository.flush(); // Ensure the delete is executed before inserting

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                MapGrid cell = MapGrid.builder()
                        .x(x)
                        .y(y)
                        .isObstacle(false)
                        .terrainType("ground")
                        .build();
                mapGridRepository.save(cell);
            }
        }
        return ResponseEntity.ok("Initialized a " + width + "x" + height + " map.");
    }
} 