package com.dne.ems.model;

import java.time.LocalDateTime;
import java.util.List;

import com.dne.ems.config.converter.StringListConverter;
import com.dne.ems.model.enums.Gender;
import com.dne.ems.model.enums.Level;
import com.dne.ems.model.enums.Role;
import com.dne.ems.model.enums.UserStatus;


import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Represents a user account in the system. This entity stores all information
 * related to a user, including credentials, personal details, role, and status.
 * It is a central part of the application's security and data model.
 * Maps to the 'user_account' table, with unique constraints on phone and email.
 *
 * @version 1.2
 * @since 2025-06-16
 */
@Entity
@Table(name = "user_account", uniqueConstraints = {
        @UniqueConstraint(columnNames = "phone"),
        @UniqueConstraint(columnNames = "email")
})
@Data
public class UserAccount {

    /**
     * The unique identifier for the user account.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The full name of the user.
     */
    @NotEmpty
    private String name;

    /**
     * The user's unique phone number. Used for communication and as a login identifier.
     */
    @NotEmpty
    @Column(nullable = false, unique = true)
    private String phone;

    /**
     * The user's unique email address. Used for communication and as a login identifier.
     */
    @NotEmpty
    @Email
    @Column(nullable = false, unique = true)
    private String email;

    /**
     * The user's hashed password. Must be at least 8 characters long.
     */
    @NotEmpty
    @Size(min = 8)
    private String password;

    /**
     * The gender of the user.
     */
    @Enumerated(EnumType.STRING)
    private Gender gender;

    /**
     * The role of the user within the system (e.g., ADMIN, SUPERVISOR, GRID_WORKER).
     * Determines the user's permissions.
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private Role role;

    /**
     * The current status of the user account (e.g., ACTIVE, INACTIVE, SUSPENDED).
     * Defaults to ACTIVE.
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private UserStatus status = UserStatus.ACTIVE;

    /**
     * The x-coordinate of the grid cell this user is primarily associated with,
     * if applicable (e.g., for a grid worker).
     */
    @Column(name = "grid_x")
    private Integer gridX;

    /**
     * The y-coordinate of the grid cell this user is primarily associated with,
     * if applicable (e.g., for a grid worker).
     */
    @Column(name = "grid_y")
    private Integer gridY;

    /**
     * The geographical region or district the user belongs to.
     */
    @Column(length = 255)
    private String region;

    /**
     * The proficiency level of the user, if applicable (e.g., for a grid worker).
     */
    @Enumerated(EnumType.STRING)
    private Level level;

    /**
     * A JSON-formatted string to store a list of the user's skills.
     */
    @Column(columnDefinition = "JSON")
    @Convert(converter = StringListConverter.class)
    private List<String> skills;

    /**
     * The timestamp when the user account was created.
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    /**
     * The timestamp when the user account was last updated.
     */
    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    /**
     * A JPA callback method that automatically updates the `updatedAt` timestamp
     * before an update operation is persisted to the database.
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    /**
     * A flag indicating if the user's account is enabled. A disabled account cannot be logged into.
     * Defaults to true.
     */
    @Column(nullable = false)
    private boolean enabled = true;

    /**
     * The current geographical latitude of the user, typically for grid workers.
     * This can be updated periodically by the user's mobile device.
     */
    private Double currentLatitude;

    /**
     * The current geographical longitude of the user, typically for grid workers.
     * This can be updated periodically by the user's mobile device.
     */
    private Double currentLongitude;

    /**
     * A counter for the number of consecutive failed login attempts.
     * Used for implementing account lockout policies.
     */
    private int failedLoginAttempts = 0;

    /**
     * The timestamp until which the user's account is locked out due to
     * excessive failed login attempts. A null value means the account is not locked.
     */
    private LocalDateTime lockoutEndTime;
} 