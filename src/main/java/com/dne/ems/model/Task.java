package com.dne.ems.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.dne.ems.model.enums.PollutionType;
import com.dne.ems.model.enums.SeverityLevel;
import com.dne.ems.model.enums.TaskStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * Represents a task to be performed by a grid worker. A task is typically created
 * in response to a feedback submission and contains all necessary details for the
 * assignee to address the reported issue.
 * Maps to the 'tasks' table.
 *
 * @version 1.2
 * @since 2025-06-16
 */
@Data
@Entity
@Table(name = "tasks")
public class Task {

    /**
     * The unique identifier for the task.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The original feedback submission that this task is based on.
     */
    @OneToOne
    @JoinColumn(name = "feedback_id")
    private Feedback feedback;

    /**
     * The grid worker to whom this task is assigned.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignee_id")
    private UserAccount assignee;

    /**
     * The user (typically a supervisor) who created this task.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private UserAccount createdBy;

    /**
     * The current status of the task (e.g., PENDING, IN_PROGRESS, COMPLETED).
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status;

    /**
     * The timestamp when the task was assigned to the worker.
     */
    @Column(name = "assigned_at")
    private LocalDateTime assignedAt;

    /**
     * The timestamp when the task was marked as completed.
     */
    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    /**
     * The timestamp when the task was created. Automatically managed by Hibernate.
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * The timestamp when the task was last updated. Automatically managed by Hibernate.
     */
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * A brief title for the task.
     */
    private String title;

    /**
     * A detailed description of the task requirements.
     */
    @Column(columnDefinition = "TEXT")
    private String description;

    /**
     * The type of pollution this task addresses.
     */
    @Enumerated(EnumType.STRING)
    private PollutionType pollutionType;

    /**
     * The severity level of the issue.
     */
    @Enumerated(EnumType.STRING)
    private SeverityLevel severityLevel;

    /**
     * A human-readable text address for the task location.
     */
    private String textAddress;

    /**
     * The x-coordinate of the grid cell where the task is located.
     */
    private Integer gridX;

    /**
     * The y-coordinate of the grid cell where the task is located.
     */
    private Integer gridY;

    /**
     * The geographical latitude of the task location.
     */
    private Double latitude;

    /**
     * The geographical longitude of the task location.
     */
    private Double longitude;

    /**
     * A list of history records for this task, tracking all status changes and updates.
     * Managed with a one-to-many relationship, and orphan removal ensures that
     * history records are deleted when the task is deleted.
     */
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskHistory> history = new ArrayList<>();
}