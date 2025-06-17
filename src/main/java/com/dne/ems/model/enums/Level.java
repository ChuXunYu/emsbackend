package com.dne.ems.model.enums;

/**
 * Defines the administrative or hierarchical level, typically for a user or a geographical region.
 * This is used to scope data visibility and permissions.
 *
 * @version 1.1
 * @since 2025-06-16
 */
public enum Level {
    /**
     * Represents the provincial level, the highest administrative division in this context.
     * Users or data at this level may have broad oversight.
     */
    PROVINCE,

    /**
     * Represents the city level, a subdivision of a province. Users or data at this
     * level have a more localized scope.
     */
    CITY
} 