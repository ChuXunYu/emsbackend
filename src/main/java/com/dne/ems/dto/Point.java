package com.dne.ems.dto;

/**
 * A simple record representing a 2D point with integer coordinates.
 * This is used for pathfinding requests and representing locations on the grid.
 *
 * @param x The x-coordinate.
 * @param y The y-coordinate.
 */
public record Point(int x, int y) {
} 