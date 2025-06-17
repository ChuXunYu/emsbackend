package com.dne.ems.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.dne.ems.model.enums.FeedbackStatus;
import com.dne.ems.model.enums.PollutionType;
import com.dne.ems.model.enums.SeverityLevel;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a feedback submission from a user about an environmental issue.
 * This is a core entity in the system, capturing all details of a report,
 * including its location, severity, and current status in the processing workflow.
 * Maps to the 'feedback' table.
 *
 * @version 1.0
 * @since 2025-06-16
 */
@Entity
@Table(name = "feedback")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Feedback {

    /**
     * The unique identifier for the feedback.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * A unique, human-readable identifier for the event or feedback case.
     */
    @Column(nullable = false, unique = true)
    private String eventId;

    /**
     * A brief, descriptive title for the feedback.
     */
    @Column(nullable = false)
    private String title;

    /**
     * A detailed description of the environmental issue.
     * Stored as a large object (TEXT/CLOB) to accommodate long text.
     */
    @Lob // For longer text
    private String description;

    /**
     * The type of pollution being reported.
     * @see PollutionType
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PollutionType pollutionType;

    /**
     * The perceived severity level of the issue.
     * @see SeverityLevel
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeverityLevel severityLevel;

    /**
     * The current status of the feedback in the processing workflow.
     * @see FeedbackStatus
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FeedbackStatus status;

    /**
     * The text-based address or location description of the event.
     */
    private String textAddress;

    /**
     * The x-coordinate of the grid cell where the event occurred.
     */
    private Integer gridX;

    /**
     * The y-coordinate of the grid cell where the event occurred.
     */
    private Integer gridY;

    /**
     * The ID of the user who submitted the feedback.
     */
    @Column(nullable = false)
    private Long submitterId;

    /**
     * The timestamp when the feedback was created.
     * Automatically set and not updatable.
     */
    @Builder.Default
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    /**
     * The timestamp of the last update to the feedback.
     * Automatically set on creation and updated via {@link #onPreUpdate()}.
     */
    @Builder.Default
    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    /**
     * The {@link UserAccount} who submitted the feedback.
     * This provides a direct link to the user entity.
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserAccount user;

    /**
     * The geographical latitude where the feedback was reported. Essential for location-based services.
     */
    private Double latitude;

    /**
     * The geographical longitude where the feedback was reported. Essential for location-based services.
     */
    private Double longitude;

    /**
     * A list of {@link Attachment} files associated with this feedback.
     * Configured to cascade all operations and remove orphan attachments.
     */
    @Builder.Default
    @OneToMany(mappedBy = "feedback", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attachment> attachments = new ArrayList<>();

    /**
     * JPA callback method to automatically update the {@code updatedAt} timestamp before an entity update.
     */
    @PreUpdate
    public void onPreUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}