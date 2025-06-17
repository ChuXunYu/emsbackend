package com.dne.ems.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dne.ems.model.UserAccount;
import com.dne.ems.model.enums.Role;
import com.dne.ems.model.enums.UserStatus;

/**
 * Spring Data JPA repository for the {@link UserAccount} entity.
 * Provides methods to perform CRUD operations and custom queries on UserAccount data.
 */
@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Long>, JpaSpecificationExecutor<UserAccount> 
{
    /**
     * Finds a user by their email address.
     *
     * @param email The email address to search for.
     * @return An {@link Optional} containing the found user, or {@link Optional#empty()} if not found.
     */
    Optional<UserAccount> findByEmail(String email);

    /**
     * Finds a user by their phone number.
     *
     * @param phone The phone number to search for.
     * @return An {@link Optional} containing the found user, or {@link Optional#empty()} if not found.
     */
    Optional<UserAccount> findByPhone(String phone);
    /**
     * Finds all users with a specific role.
     * @param role The role to search for.
     * @return A list of users with the specified role.
     */
    List<UserAccount> findByRole(Role role);

    /**
     * Counts all users with a specific role.
     * @param role The role to count.
     * @return The number of users with the specified role.
     */
    long countByRole(Role role);

    @Query("SELECT u FROM UserAccount u WHERE u.role = 'GRID_WORKER' AND u.status = 'ACTIVE' AND u.gridX BETWEEN ?1 AND ?2 AND u.gridY BETWEEN ?3 AND ?4")
    List<UserAccount> findActiveWorkersInArea(int minX, int maxX, int minY, int maxY);

    Page<UserAccount> findByRole(Role role, Pageable pageable);

    List<UserAccount> findByRoleAndStatus(Role role, UserStatus status);
} 