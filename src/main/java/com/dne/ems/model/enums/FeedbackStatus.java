package com.dne.ems.model.enums;

/**
 * Defines the lifecycle status of a feedback submission. This enum tracks the journey
 * of a feedback report from initial submission through review, processing, and resolution.
 *
 * @version 1.1
 * @since 2025-06-16
 */
public enum FeedbackStatus {
    /**
     * Initial state after a public user submits feedback. It is waiting for a supervisor
     * to begin the review process.
     */
    PENDING_REVIEW,

    /**
     * The feedback is currently being analyzed by an automated AI system to
     * extract relevant details and suggest actions.
     */
    AI_REVIEWING,

    /**
     * The automated AI review could not be completed due to a technical error.
     * This status indicates that manual intervention is required.
     */
    AI_REVIEW_FAILED,

    /**
     * The AI has successfully reviewed the feedback and is now processing it,
     * which may include generating summaries or categorizing the issue.
     */
    AI_PROCESSING,

    /**
     * The feedback has been validated and is now in a queue, waiting to be
     * assigned as a task to a grid worker.
     */
    PENDING_ASSIGNMENT,

    /**
     * A task has been created from the feedback and assigned to a specific grid worker.
     */
    ASSIGNED,

    /**
     * The assigned grid worker has investigated the issue on-site and confirmed the
     * details reported in the feedback.
     */
    CONFIRMED,

    /**
     * The feedback has been closed after being deemed invalid, a duplicate, or irrelevant
     * by a supervisor or administrator. No further action will be taken.
     */
    CLOSED_INVALID,

    /**
     * The feedback has been fully processed, and a corresponding task has been created
     * and is being managed in the task lifecycle. This marks the end of the feedback's own active lifecycle.
     */
    PROCESSED
}