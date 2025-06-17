package com.dne.ems.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dne.ems.model.PasswordResetToken;
import com.dne.ems.model.UserAccount;

/**
 * Spring Data JPA repository for the {@link PasswordResetToken} entity.
 */
@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    /**
     * Finds a password reset token by the token string.
     *
     * @param token The token string to search for.
     * @return An {@link Optional} containing the found token, or empty if not found.
     */
    Optional<PasswordResetToken> findByToken(String token);

    /**
     * Finds a password reset token by the associated user.
     *
     * @param user The user to search for.
     * @return An {@link Optional} containing the found token, or empty if not found.
     */
    Optional<PasswordResetToken> findByUser(UserAccount user);
}