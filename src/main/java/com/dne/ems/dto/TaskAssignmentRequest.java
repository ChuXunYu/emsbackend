package com.dne.ems.dto;

import jakarta.validation.constraints.NotNull;

/**
 * DTO for assigning a task to a user.
 *
 * @param assigneeId The ID of the user (grid worker) to whom the task is being assigned.
 */
public record TaskAssignmentRequest(
        @NotNull
        Long assigneeId
) {} 