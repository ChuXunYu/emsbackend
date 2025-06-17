package com.dne.ems.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.dne.ems.model.enums.PollutionType;
import com.dne.ems.model.enums.SeverityLevel;
import com.dne.ems.model.enums.TaskStatus;

/**
 * A DTO representing the full details of a task.
 *
 * @param id The unique identifier of the task.
 * @param status The current status of the task.
 * @param feedbackDetails Full details of the original feedback.
 * @param assigneeDetails Details of the assigned grid worker. Can be null.
 * @param createdAt The creation timestamp of the task.
 * @param assignedAt The timestamp when the task was assigned. Can be null.
 * @param completedAt The timestamp when the task was completed. Can be null.
 */
public record TaskDetailDTO(
        Long id,
        FeedbackDTO feedback,
        String title,
        String description,
        SeverityLevel severityLevel,
        PollutionType pollutionType,
        String textAddress,
        Integer gridX,
        Integer gridY,
        TaskStatus status,
        AssigneeDTO assignee,
        LocalDateTime assignedAt,
        LocalDateTime completedAt,
        List<TaskHistoryDTO> history
) {
    /**
     * DTO for feedback details within a task.
     */
    public record FeedbackDTO(
            Long feedbackId,
            String eventId,
            String title,
            String description,
            SeverityLevel severityLevel,
            String textAddress,
            Long submitterId,
            String submitterName,
            LocalDateTime createdAt,
            List<String> imageUrls
    ) {}

    /**
     * DTO for assignee details within a task.
     */
    public record AssigneeDTO(
            Long id,
            String name,
            String phone
    ) {}
} 