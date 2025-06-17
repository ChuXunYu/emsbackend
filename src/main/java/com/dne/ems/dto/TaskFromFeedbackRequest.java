package com.dne.ems.dto;

import com.dne.ems.model.enums.SeverityLevel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO for creating a task from an existing feedback.
 * The supervisor provides a title and a severity level.
 */
@Data
public class TaskFromFeedbackRequest {

    @NotBlank(message = "任务标题不能为空")
    private String title;

    @NotNull(message = "严重级别不能为空")
    private SeverityLevel severityLevel;
} 