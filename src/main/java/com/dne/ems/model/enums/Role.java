package com.dne.ems.model.enums;

/**
 * Defines the roles a user can have within the system. Each role grants a different
 * set of permissions and capabilities, governing access to various system functionalities.
 * This enum is used in the `UserAccount` entity and is crucial for authorization.
 *
 * @version 1.1
 * @since 2025-06-16
 */
public enum Role {
    /**
     * Represents a public supervisor, a user from the public who is tasked with
     * overseeing and validating feedback submissions. They act as a first line of quality control.
     */
    PUBLIC_SUPERVISOR,

    /**
     * Represents a supervisor, an internal role with responsibilities to manage,
     * assign, and review tasks based on feedback. They bridge the gap between public
     * submissions and actionable tasks for grid workers.
     */
    SUPERVISOR,

    /**
     * Represents a grid worker, a field agent who performs on-the-ground tasks.
     * They are assigned specific tasks by supervisors and are responsible for executing
     * them and reporting back on their status.
     */
    GRID_WORKER,

    /**
     * Represents a system administrator, who has the highest level of permissions.
     * Administrators can manage user accounts, system settings, and have full
     * oversight of all data and operations.
     */
    ADMIN,

    /**
     * Represents a decision-maker, a high-level role focused on analytics and reporting.
     * They have access to dashboards, system-wide statistics, and reports to inform
     * strategic decisions, but typically do not perform day-to-day operations.
     */
    DECISION_MAKER
} 