package com.dne.ems.dto;

import jakarta.validation.constraints.NotNull;

/**
 * DTO for an administrator's decision on a submitted task.
 *
 * @param approved True if the task submission is approved, false otherwise.
 * @param comments Optional comments from the administrator regarding the decision.
 */
public record TaskApprovalRequest(
        @NotNull
        Boolean approved,
        String comments
) {} 