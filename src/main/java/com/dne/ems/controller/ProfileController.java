package com.dne.ems.controller;

import com.dne.ems.dto.UserFeedbackSummaryDTO;
import com.dne.ems.security.CustomUserDetails;
import com.dne.ems.service.UserFeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for authenticated users to manage and view their own feedback submissions.
 *
 * @version 1.2
 * @since 2025-06-16
 */
@RestController
@RequestMapping("/api/me")
@RequiredArgsConstructor
@Tag(name = "User Profile", description = "Endpoints related to the authenticated user's own data")
@SecurityRequirement(name = "bearerAuth")
public class ProfileController {

    private final UserFeedbackService userFeedbackService;

    /**
     * Retrieves a paginated history of the currently authenticated user's feedback submissions.
     *
     * @param userDetails The details of the authenticated user.
     * @param pageable    Pagination information.
     * @return A {@link Page} of {@link UserFeedbackSummaryDTO} objects representing the user's feedback history.
     */
    @GetMapping("/feedback")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get my feedback history", description = "Retrieves a paginated list of all feedback submissions made by the currently authenticated user.")
    public ResponseEntity<Page<UserFeedbackSummaryDTO>> getMyFeedbackHistory(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Pageable pageable) {
        Page<UserFeedbackSummaryDTO> historyPage = userFeedbackService.getFeedbackHistoryByUserId(userDetails.getId(), pageable);
        return ResponseEntity.ok(historyPage);
    }
} 