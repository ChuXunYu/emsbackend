package com.dne.ems.model;

import java.time.LocalDateTime;

import com.dne.ems.model.enums.AssignmentStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * Represents the assignment of a feedback-derived task to a grid worker.
 * This entity links a piece of feedback to the worker responsible for handling it.
 * It tracks the lifecycle of the assignment from creation to completion.
 * Maps to the 'assignments' table.
 *
 * @version 1.0
 * @since 2025-06-16
 */
@Entity
@Table(name = "assignments")
@Data
public class Assignment {

    /**
     * The unique identifier for the assignment.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The ID of the {@link Feedback} entity that is being assigned.
     * This establishes a direct link to the source of the task.
     */
    @Column(nullable = false)
    private Long feedbackId;

    /**
     * The ID of the {@link UserAccount} (grid worker) to whom the task is assigned.
     */
    @Column(nullable = false)
    private Long assigneeId;

    /**
     * The ID of the {@link UserAccount} (admin or supervisor) who created the assignment.
     */
    @Column(nullable = false)
    private Long assignerId;

    /**
     * The timestamp when the assignment was created.
     * This is automatically set to the current time and is not updatable.
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime assignmentTime = LocalDateTime.now();

    /**
     * The deadline by which the task should be completed.
     * Can be null if no deadline is set.
     */
    private LocalDateTime deadline;

    /**
     * The current status of the assignment (e.g., PENDING, IN_PROGRESS, COMPLETED).
     * @see AssignmentStatus
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AssignmentStatus status;

    /**
     * Any notes or remarks from the assigner regarding the task.
     */
    private String remarks;
} 