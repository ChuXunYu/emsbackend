package com.dne.ems.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.dne.ems.model.enums.AssignmentMethod;
import com.dne.ems.model.enums.AssignmentStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * Represents a detailed record of a feedback assignment to a grid worker.
 * This entity provides a rich, relational view of an assignment, linking directly
 * to the Feedback, Grid Worker, and Admin entities. It also captures metadata
 * about how the assignment was made.
 * Maps to the 'assignment_record' table.
 *
 * @version 1.0
 * @since 2025-06-16
 */
@Data
@Entity
@Table(name = "assignment_record")
public class AssignmentRecord {

    /**
     * The unique identifier for the assignment record.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The {@link Feedback} entity that is being assigned.
     * This uses a lazy fetch type for performance optimization.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feedbackId", nullable = false)
    private Feedback feedback;

    /**
     * The {@link UserAccount} (grid worker) to whom the task is assigned.
     * This uses a lazy fetch type for performance optimization.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gridWorkerId", nullable = false)
    private UserAccount gridWorker;

    /**
     * The {@link UserAccount} (administrator) who made the assignment.
     * This uses a lazy fetch type for performance optimization.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adminId", nullable = false)
    private UserAccount admin;

    /**
     * The method used for the assignment (e.g., MANUAL, INTELLIGENT).
     * @see AssignmentMethod
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private AssignmentMethod assignmentMethod;

    /**
     * A JSON snapshot of the details from the intelligent assignment algorithm, if applicable.
     * Stored as a large object (TEXT/CLOB) in the database.
     */
    @Lob
    private String algorithmDetails;

    /**
     * The current status of the assignment (e.g., PENDING, COMPLETED).
     * @see AssignmentStatus
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private AssignmentStatus status;

    /**
     * The timestamp when the assignment record was created.
     * This is automatically set by Hibernate upon creation and is not updatable.
     */
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
} 