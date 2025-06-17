package com.dne.ems.service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dne.ems.dto.UserCreationRequest;
import com.dne.ems.dto.UserUpdateRequest;
import com.dne.ems.model.UserAccount;
import com.dne.ems.model.enums.Role;

/**
 * Service interface for personnel management operations.
 */
public interface PersonnelService {

    /**
     * Retrieves a paginated and filtered list of users.
     *
     * @param role Optional filter by user role.
     * @param name Optional filter by user name (case-insensitive search).
     * @param pageable Pagination and sorting information.
     * @return A {@link Page} of {@link UserAccount} objects.
     */
    Page<UserAccount> getUsers(Role role, String name, Pageable pageable);

    /**
     * Updates a user's information.
     *
     * @param userId The ID of the user to update.
     * @param request The request containing the fields to update.
     * @return The updated {@link UserAccount}.
     */
    /**
     * Creates a new user account.
     *
     * @param request The request containing the new user's details.
     * @return The created {@link UserAccount}.
     */
    UserAccount createUser(UserCreationRequest request);
    UserAccount updateUser(Long userId, UserUpdateRequest request);

    /**
     * Deletes a user by marking them as inactive.
     *
     * @param userId The ID of the user to delete.
     */
    void deleteUser(Long userId);

    UserAccount updateOwnProfile(String currentUserEmail, UserUpdateRequest request);
} 