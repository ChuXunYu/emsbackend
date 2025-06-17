package com.dne.ems.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO for receiving feedback from the public.
 */
@Data
public class PublicFeedbackRequest {

    /**
     * The title of the feedback.
     */
    @NotBlank(message = "Title cannot be blank")
    private String title;

    /**
     * The detailed description of the feedback.
     */
    @NotBlank(message = "Description cannot be blank")
    private String description;

    /**
     * The type of pollution.
     */
    @NotBlank(message = "Pollution type cannot be blank")
    private String pollutionType;

    /**
     * The text description of the address of the event.
     */
    @NotBlank(message = "Text address cannot be blank")
    private String textAddress;

    /**
     * The grid coordinate X.
     */
    @NotNull(message = "Grid X coordinate cannot be null")
    private Double gridX;

    /**
     * The grid coordinate Y.
     */
    @NotNull(message = "Grid Y coordinate cannot be null")
    private Double gridY;
} 