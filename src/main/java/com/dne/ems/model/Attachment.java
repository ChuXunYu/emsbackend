package com.dne.ems.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a file attachment, typically associated with a {@link Feedback} submission.
 * This entity stores metadata about an uploaded file, such as its name, type, size,
 * and how it is stored on the server.
 * Maps to the 'attachment' table.
 *
 * @version 1.0
 * @since 2025-06-16
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Attachment {

    /**
     * The unique identifier for the attachment.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The original name of the file as provided by the user (e.g., "photo1.jpg").
     */
    @Column(nullable = false)
    private String fileName;

    /**
     * The MIME type of the file (e.g., "image/jpeg", "application/pdf").
     */
    @Column(nullable = false)
    private String fileType;

    /**
     * A unique name generated for storing the file on the server to prevent name conflicts.
     * This is the name used to retrieve the file.
     */
    @Column(nullable = false, unique = true)
    private String storedFileName;

    /**
     * The size of the file in bytes.
     */
    @Column(nullable = false)
    private Long fileSize;

    /**
     * The timestamp when the file was uploaded.
     * This is automatically set upon creation and is not updatable.
     */
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime uploadDate;

    /**
     * The {@link Feedback} submission this attachment is associated with.
     * This creates a many-to-one relationship, allowing a feedback to have multiple attachments.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feedback_id")
    private Feedback feedback;

} 