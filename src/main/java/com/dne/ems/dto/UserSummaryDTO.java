package com.dne.ems.dto;

import com.dne.ems.model.enums.Role;
import com.dne.ems.model.enums.UserStatus;

/**
 * A DTO for providing a summary of user information, typically for lists.
 *
 * @param id The user's unique identifier.
 * @param name The user's full name.
 * @param email The user's email address.
 * @param phone The user's phone number.
 * @param role The user's assigned role.
 * @param status The user's current account status.
 */
public record UserSummaryDTO(
    Long id,
    String name,
    String email,
    String phone,
    Role role,
    UserStatus status
) {} 