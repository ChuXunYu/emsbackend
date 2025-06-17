package com.dne.ems.model.enums;

/**
 * Defines the severity levels for a reported issue, such as a pollution event.
 * This helps in prioritizing tasks and allocating resources effectively.
 *
 * @version 1.1
 * @since 2025-06-16
 */
public enum SeverityLevel {
    /**
     * Indicates a critical issue that requires immediate attention and response.
     * High severity tasks should be prioritized above all others.
     */
    HIGH,

    /**
     * Indicates a significant issue that requires attention in a timely manner,
     * but is not as critical as a high-severity issue.
     */
    MEDIUM,

    /**
     * Indicates a minor issue that can be addressed with lower priority
     * or during routine maintenance.
     */
    LOW
}