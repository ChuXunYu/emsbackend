package com.dne.ems.dto;

import com.dne.ems.model.enums.PollutionType;
import com.dne.ems.model.enums.SeverityLevel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TaskCreationRequest(
    @NotBlank(message = "Title is mandatory")
    String title,

    String description,

    @NotNull(message = "Pollution type is mandatory")
    PollutionType pollutionType,

    @NotNull(message = "Severity level is mandatory")
    SeverityLevel severityLevel,

    @NotNull(message = "Location information is mandatory")
    LocationDTO location
) {
    public record LocationDTO(
        Double latitude,
        Double longitude,
        @NotBlank(message = "Text address is mandatory")
        String textAddress,

        @NotNull(message = "Grid X coordinate is mandatory")
        Integer gridX,

        @NotNull(message = "Grid Y coordinate is mandatory")
        Integer gridY
    ) {}
} 