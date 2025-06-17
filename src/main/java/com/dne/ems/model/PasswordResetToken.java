package com.dne.ems.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a token for resetting user passwords. This token is generated and sent to the user,
 * who can then use it to set a new password. The token has a limited lifespan and is associated
 * with a specific user account.
 * Maps to the 'password_reset_token' table.
 *
 * @version 1.0
 * @since 2025-06-16
 */
@Entity
@Data
@NoArgsConstructor
public class PasswordResetToken {

    /**
     * The duration in minutes for which the token is valid.
     */
    private static final int EXPIRATION_MINUTES = 60;

    /**
     * The unique identifier for the password reset token.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The unique token string.
     */
    @Column(nullable = false, unique = true)
    private String token;

    /**
     * The user account associated with this token. A token can only be used to reset the
     * password for the associated user.
     */
    @OneToOne(targetEntity = UserAccount.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_account_id")
    private UserAccount user;

    /**
     * The date and time when this token will expire.
     */
    @Column(nullable = false)
    private LocalDateTime expiryDate;

    /**
     * Constructs a new PasswordResetToken.
     *
     * @param token The token string.
     * @param user The user account associated with the token.
     */
    public PasswordResetToken(String token, UserAccount user) {
        this.token = token;
        this.user = user;
        this.expiryDate = calculateExpiryDate(EXPIRATION_MINUTES);
    }

    /**
     * Calculates the expiry date for the token.
     *
     * @param expiryTimeInMinutes The number of minutes until the token expires.
     * @return The calculated expiry date and time.
     */
    private LocalDateTime calculateExpiryDate(int expiryTimeInMinutes) {
        return LocalDateTime.now().plusMinutes(expiryTimeInMinutes);
    }

    /**
     * Checks if the token has expired.
     *
     * @return true if the token has expired, false otherwise.
     */
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiryDate);
    }
}