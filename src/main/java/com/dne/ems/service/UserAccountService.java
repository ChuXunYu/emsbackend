package com.dne.ems.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dne.ems.dto.UserRegistrationRequest;
import com.dne.ems.dto.UserRoleUpdateRequest;
import com.dne.ems.dto.UserSummaryDTO;
import com.dne.ems.model.UserAccount;
import com.dne.ems.model.enums.Role;
import com.dne.ems.model.enums.UserStatus;

/**
 * Service interface for managing user accounts.
 * Defines the contract for user-related business operations.
 */
public interface UserAccountService {

    /**
     * Registers a new user in the system.
     *
     * @param registrationRequest DTO containing the new user's information.
     * @return The newly created UserAccount entity.
     * @throws com.dne.ems.exception.UserAlreadyExistsException if a user with the same email or phone already exists.
     */
    UserAccount registerUser(UserRegistrationRequest registrationRequest);

    /**
     * Updates the role and potentially the location of a user.
     *
     * @param userId The ID of the user to update.
     * @param request DTO containing the new role and, if required, location data.
     * @return The updated UserAccount entity.
     * @throws jakarta.persistence.EntityNotFoundException if the user is not found.
     */
    UserAccount updateUserRole(Long userId, UserRoleUpdateRequest request);

    UserAccount getUserById(Long userId);

    Page<UserAccount> getUsersByRole(Role role, Pageable pageable);

    void updateUserLocation(Long userId, Double latitude, Double longitude);

    Page<UserSummaryDTO> getAllUsers(Role role, UserStatus status, Pageable pageable);

} 