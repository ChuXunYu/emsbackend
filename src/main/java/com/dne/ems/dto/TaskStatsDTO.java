package com.dne.ems.dto;

/**
 * DTO for representing task completion statistics.
 *
 * @param totalTasks      The total number of tasks.
 * @param completedTasks  The number of completed tasks.
 * @param completionRate  The calculated completion rate (completedTasks / totalTasks).
 */
public record TaskStatsDTO(
    long totalTasks,
    long completedTasks,
    double completionRate
) {} 