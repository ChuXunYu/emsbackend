package com.dne.ems.dto;

import com.dne.ems.model.enums.Role;
import com.dne.ems.model.enums.UserStatus;
import com.dne.ems.model.enums.Gender;
import com.dne.ems.model.enums.Level;

import java.util.List;

/**
 * A DTO for updating user account information.
 *
 * @param name The new name for the user. Can be null if not updating.
 * @param phone The new phone for the user. Can be null if not updating.
 * @param region The new region for the user. Can be null if not updating.
 * @param role The new role for the user. Can be null if not updating.
 * @param status The new status for the user. Can be null if not updating.
 * @param gender The new gender for the user. Can be null if not updating.
 * @param gridX The new grid X coordinate.
 * @param gridY The new grid Y coordinate.
 * @param level The new level for the user.
 * @param skills The new skills for the user.
 * @param currentLatitude The new latitude.
 * @param currentLongitude The new longitude.
 */
public record UserUpdateRequest(
    String name,
    String phone,
    String region,
    Role role,
    UserStatus status,
    Gender gender,
    Integer gridX,
    Integer gridY,
    Level level,
    List<String> skills,
    Double currentLatitude,
    Double currentLongitude
) {} 