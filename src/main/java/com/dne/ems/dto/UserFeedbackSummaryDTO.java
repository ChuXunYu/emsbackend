package com.dne.ems.dto;

import com.dne.ems.model.enums.FeedbackStatus;
import java.time.LocalDateTime;

/**
 * A DTO representing a summary of a feedback submission for the user's history view.
 *
 * @param eventId The unique business ID of the feedback event.
 * @param title The title of the feedback.
 * @param status The current status of the feedback.
 * @param createdAt The timestamp when the feedback was submitted.
 * @param imageUrl The URL of the first attachment image, if any.
 */
public record UserFeedbackSummaryDTO(
    String eventId,
    String title,
    FeedbackStatus status,
    LocalDateTime createdAt,
    String imageUrl
) {} 