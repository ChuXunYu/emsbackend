package com.dne.ems.dto;

import jakarta.validation.constraints.NotEmpty;

/**
 * DTO for a grid worker to submit the results of a completed task.
 *
 * @param notes Notes or comments from the grid worker about the task resolution.
 */
public record TaskSubmissionRequest(
        @NotEmpty
        String notes
) {} 