package com.dne.ems.dto;

import com.dne.ems.model.enums.PollutionType;

/**
 * DTO for representing pollution statistics.
 *
 * @param pollutionType The type of pollution.
 * @param count The number of feedback events for this pollution type.
 */
public record PollutionStatsDTO(
        PollutionType pollutionType,
        Long count
) {} 