package com.dne.ems.service.pathfinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.dne.ems.dto.Point;
import com.dne.ems.model.MapGrid;
import com.dne.ems.repository.MapGridRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AStarService {

    private final MapGridRepository mapGridRepository;

    private static class Node {
        Point point;
        int g; // cost from start to current node
        int h; // heuristic: estimated cost from current node to end
        int f; // g + h
        Node parent;

        public Node(Point point, Node parent, int g, int h) {
            this.point = point;
            this.parent = parent;
            this.g = g;
            this.h = h;
            this.f = g + h;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return point.equals(node.point);
        }

        @Override
        public int hashCode() {
            return Objects.hash(point);
        }
    }

    /**
     * Finds the shortest path between two points on a grid, avoiding obstacles.
     * The map, including dimensions and obstacles, is loaded dynamically from the database for each call.
     *
     * @param start The starting point.
     * @param end The ending point.
     * @return A list of points representing the path from start to end, or an empty list if no path is found.
     */
    public List<Point> findPath(Point start, Point end) {
        List<MapGrid> mapGrids = mapGridRepository.findAll();
        if (mapGrids.isEmpty()) {
            return Collections.emptyList(); // No map data
        }

        // Dynamically determine map dimensions and obstacles
        int maxX = 0;
        int maxY = 0;
        Set<Point> obstacles = new HashSet<>();
        for (MapGrid gridCell : mapGrids) {
            if (gridCell.isObstacle()) {
                obstacles.add(new Point(gridCell.getX(), gridCell.getY()));
            }
            if (gridCell.getX() > maxX) maxX = gridCell.getX();
            if (gridCell.getY() > maxY) maxY = gridCell.getY();
        }
        final int mapWidth = maxX + 1;
        final int mapHeight = maxY + 1;

        if (obstacles.contains(start) || obstacles.contains(end)) {
            return Collections.emptyList(); // Start or end is on an obstacle
        }

        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingInt(n -> n.f));
        Map<Point, Node> allNodes = new HashMap<>();

        Node startNode = new Node(start, null, 0, calculateHeuristic(start, end));
        openSet.add(startNode);
        allNodes.put(start, startNode);

        while (!openSet.isEmpty()) {
            Node currentNode = openSet.poll();

            if (currentNode.point.equals(end)) {
                return reconstructPath(currentNode);
            }

            for (Point neighborPoint : getNeighbors(currentNode.point, mapWidth, mapHeight)) {
                if (obstacles.contains(neighborPoint)) {
                    continue;
                }

                int tentativeG = currentNode.g + 1; // Cost between neighbors is 1

                Node neighborNode = allNodes.get(neighborPoint);

                if (neighborNode == null) {
                    int heuristic = calculateHeuristic(neighborPoint, end);
                    neighborNode = new Node(neighborPoint, currentNode, tentativeG, heuristic);
                    allNodes.put(neighborPoint, neighborNode);
                    openSet.add(neighborNode);
                } else if (tentativeG < neighborNode.g) {
                    // Found a better path to this neighbor
                    neighborNode.parent = currentNode;
                    neighborNode.g = tentativeG;
                    neighborNode.f = tentativeG + neighborNode.h;
                    // Re-heapify the priority queue (this is a bit of a hack for standard Java PQ)
                    openSet.remove(neighborNode);
                    openSet.add(neighborNode);
                }
            }
        }
        return Collections.emptyList(); // No path found
    }

    private int calculateHeuristic(Point a, Point b) {
        // Manhattan distance: suitable for grid-based movement (up, down, left, right)
        return Math.abs(a.x() - b.x()) + Math.abs(a.y() - b.y());
    }

    private List<Point> getNeighbors(Point point, int mapWidth, int mapHeight) {
        List<Point> neighbors = new ArrayList<>(4);
        int[] dx = {0, 0, 1, -1}; // Right, Left, Down, Up
        int[] dy = {1, -1, 0, 0}; // Corresponds to dx indices

        for (int i = 0; i < 4; i++) {
            int newX = point.x() + dx[i];
            int newY = point.y() + dy[i];

            if (newX >= 0 && newX < mapWidth && newY >= 0 && newY < mapHeight) {
                neighbors.add(new Point(newX, newY));
            }
        }
        return neighbors;
    }

    private List<Point> reconstructPath(Node node) {
        LinkedList<Point> path = new LinkedList<>();
        while (node != null) {
            path.addFirst(node.point);
            node = node.parent;
        }
        return path;
    }
} 