package com.dne.ems.model.enums;

/**
 * Defines the method used to assign a task to a grid worker. This helps
 * in auditing and analyzing the effectiveness of different assignment strategies.
 *
 * @version 1.1
 * @since 2025-06-16
 */
public enum AssignmentMethod {
    /**
     * Indicates that the task was assigned manually by a user, typically a supervisor
     * or administrator, who made a direct choice of the assignee.
     */
    MANUAL,

    /**
     * Indicates that the task was assigned automatically by the system. This usually
     * involves an algorithm that considers factors like worker availability, location,
     * skills, and current workload.
     */
    INTELLIGENT
}