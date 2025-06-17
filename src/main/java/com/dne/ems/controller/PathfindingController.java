package com.dne.ems.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dne.ems.dto.PathfindingRequest;
import com.dne.ems.dto.Point;
import com.dne.ems.service.pathfinding.AStarService;

import lombok.RequiredArgsConstructor;

/**
 * Controller to expose A* pathfinding functionality.
 */
@RestController
@RequestMapping("/api/pathfinding")
@RequiredArgsConstructor
public class PathfindingController {

    private final AStarService aStarService;

    /**
     * Finds a path between a start and end point using the A* algorithm.
     * The endpoint is located at {@code POST /api/pathfinding/find}.
     * This endpoint is publicly accessible and does not require authentication.
     *
     * @param request The request body containing the start and end points' coordinates.
     * @return A list of points representing the path, or an empty list if no path is found.
     */
    @PostMapping("/find")
    public ResponseEntity<List<Point>> findPath(@RequestBody PathfindingRequest request) {
        if (request == null) {
            return ResponseEntity.badRequest().build();
        }
        Point start = new Point(request.getStartX(), request.getStartY());
        Point end = new Point(request.getEndX(), request.getEndY());

        List<Point> path = aStarService.findPath(start, end);
        return ResponseEntity.ok(path);
    }
} 