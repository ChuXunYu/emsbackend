package com.dne.ems.model.enums;

/**
 * Represents the lifecycle status of a task, tracking its progress from creation to completion.
 *
 * @version 1.1
 * @since 2025-06-16
 */
public enum TaskStatus {
    /**
     * The task has been created, typically from a processed feedback report,
     * but has not yet been assigned to a specific grid worker.
     */
    PENDING_ASSIGNMENT,

    /**
     * The task has been officially assigned to a grid worker but work has not yet begun.
     */
    ASSIGNED,

    /**
     * The assigned grid worker has acknowledged the task and is actively working to resolve it.
     */
    IN_PROGRESS,

    /**
     * The grid worker has finished their work and submitted their findings or results
     * for review by a supervisor or administrator.
     */
    SUBMITTED,

    /**
     * A supervisor or administrator has reviewed the submitted work and confirmed that
     * the task has been successfully resolved. This is a final state.
     */
    COMPLETED,

    /**
     * The task has been cancelled, either before or during its execution.
     * No further action will be taken on this task. This is a final state.
     */
    CANCELLED
} 