package com.dne.ems.service;

import com.dne.ems.model.Feedback;

import java.util.List;

public interface SupervisorService {

    /**
     * Get a list of feedback entries that are pending supervisor review.
     * These are typically feedback that have passed initial AI review.
     *
     * @return A list of Feedback objects with PENDING_REVIEW status.
     */
    List<Feedback> getFeedbackForReview();

    /**
     * Approve a feedback entry, moving it to the next stage for task assignment.
     *
     * @param feedbackId The ID of the feedback to approve.
     */
    void approveFeedback(Long feedbackId);

    /**
     * Reject a feedback entry, marking it as invalid.
     *
     * @param feedbackId The ID of the feedback to reject.
     */
    void rejectFeedback(Long feedbackId);
} 