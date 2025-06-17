package com.dne.ems.model.enums;

/**
 * Defines the specific types of pollution that can be reported in a feedback submission.
 * This categorization helps in directing the issue to the right teams and resources.
 *
 * @version 1.1
 * @since 2025-06-16
 */
public enum PollutionType {
    /**
     * Represents fine particulate matter (PM2.5), which are tiny particles in the air
     * that can cause serious health problems.
     */
    PM25,

    /**
     * Represents ground-level Ozone (O3), a major component of smog.
     */
    O3,

    /**
     * Represents Nitrogen Dioxide (NO2), a gas commonly emitted from vehicles
     * and industrial sources.
     */
    NO2,

    /**
     * Represents Sulfur Dioxide (SO2), a gas produced by burning fossil fuels
     * containing sulfur.
     */
    SO2,

    /**
     * A catch-all category for other types of pollution not explicitly listed.
     * Submissions with this type may require more detailed manual review.
     */
    OTHER
} 