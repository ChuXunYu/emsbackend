package com.dne.ems.model.enums;

/**
 * Defines the operational status of a user account, which controls their
 * ability to access the system.
 *
 * @version 1.1
 * @since 2025-06-16
 */
public enum UserStatus {
    /**
     * The user account is active and fully operational. The user can log in
     * and perform all actions permitted by their role.
     */
    ACTIVE,

    /**
     * The user account has been deactivated, either manually by an admin or
     * automatically. The user cannot log in or access the system.
     */
    INACTIVE,

    /**
     * The user is temporarily on leave. This status can be used to disable
     * task assignments or notifications without fully deactivating the account.
     */
    ON_LEAVE
} 