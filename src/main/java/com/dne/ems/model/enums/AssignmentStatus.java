package com.dne.ems.model.enums;

/**
 * Represents the status of a specific task assignment record. This tracks the
 * state of a task as it pertains to the assigned worker.
 *
 * @version 1.1
 * @since 2025-06-16
 */
public enum AssignmentStatus {
    /**
     * The task has been assigned to a grid worker, but they have not yet
     * started working on it.
     * (任务已分配，但网格员尚未开始处理。)
     */
    PENDING,

    /**
     * The grid worker is actively engaged in handling the assigned task.
     * (网格员正在处理任务。)
     */
    IN_PROGRESS,

    /**
     * The grid worker has successfully completed all work for the task.
     * (任务已由网格员完成。)
     */
    COMPLETED,

    /**
     * The task was not completed by its designated deadline and is now considered overdue.
     * (任务未在截止日期前完成。)
     */
    OVERDUE
} 