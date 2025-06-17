package com.dne.ems.dto;

import java.time.LocalDateTime;

import com.dne.ems.model.enums.SeverityLevel;
import com.dne.ems.model.enums.TaskStatus;

/**
 * A DTO representing a summary of a task for display in lists.
 *
 * @param id The unique identifier of the task.
 * @param title A brief title or description of the task.
 * @param status The current status of the task.
 * @param assigneeName The name of the grid worker assigned to the task. Can be null.
 * @param creationDate The date and time when the task was created.
 * @param cityName The city where the task is located.
 * @param districtName The district where the task is located.
 * @param severity The severity level of the issue related to the task.
 */
public record TaskSummaryDTO(
        Long id,
        String title,
        String description,
        TaskStatus status,
        String assigneeName,
        LocalDateTime createdAt,
        String textAddress,
        String imageUrl,
        SeverityLevel severity
) {} 