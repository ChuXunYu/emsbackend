package com.dne.ems.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.dne.ems.dto.UserFeedbackSummaryDTO;

/**
 * Service interface for user-specific feedback operations.
 */
public interface UserFeedbackService {

    /**
     * Retrieves a paginated history of feedback submitted by a specific user.
     *
     * @param userId The ID of the user whose feedback history is to be retrieved.
     * @param pageable Pagination and sorting information.
     * @return A {@link Page} of {@link UserFeedbackSummaryDTO} objects.
     */
    Page<UserFeedbackSummaryDTO> getFeedbackHistoryByUserId(Long userId, Pageable pageable);
} 