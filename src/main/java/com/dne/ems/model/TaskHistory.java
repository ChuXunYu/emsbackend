package com.dne.ems.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.dne.ems.model.enums.TaskStatus;

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
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a historical record of a change in a task's status. Each time a task's
 * state is modified (e.g., from PENDING to IN_PROGRESS), a new TaskHistory entry
 * is created to log the change. This provides an audit trail for tasks.
 * Maps to the 'task_history' table.
 *
 * @version 1.1
 * @since 2025-06-16
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "task_history")
public class TaskHistory {

    /**
     * The unique identifier for the task history record.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The task to which this history record belongs.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "task_id")
    private Task task;

    /**
     * The status of the task before the change. Can be null for the initial creation event.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "old_status")
    private TaskStatus oldStatus;

    /**
     * The new status of the task after the change.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "new_status", nullable = false)
    private TaskStatus newStatus;

    /**
     * The user who initiated the status change.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "changed_by_id")
    private UserAccount changedBy;

    /**
     * Optional comments or notes regarding the status change.
     */
    private String comments;

    /**
     * The timestamp when the status change occurred. Automatically managed by Hibernate.
     */
    @CreationTimestamp
    @Column(name = "changed_at", nullable = false, updatable = false)
    private LocalDateTime changedAt;

    /**
     * Constructs a new TaskHistory record.
     *
     * @param task      The associated task.
     * @param oldStatus The previous status of the task.
     * @param newStatus The new status of the task.
     * @param changedBy The user who made the change.
     * @param comments  Any comments related to the change.
     */
    public TaskHistory(Task task, TaskStatus oldStatus, TaskStatus newStatus, UserAccount changedBy, String comments) {
        this.task = task;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
        this.changedBy = changedBy;
        this.comments = comments;
    }
} 