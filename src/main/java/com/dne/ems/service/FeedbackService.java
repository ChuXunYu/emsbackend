package com.dne.ems.service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.dne.ems.dto.FeedbackSubmissionRequest;
import com.dne.ems.dto.PublicFeedbackRequest;
import com.dne.ems.model.Feedback;

/**
 * Service interface for managing feedback submissions.
 */
public interface FeedbackService {

    /**
     * Submits a new feedback report from a logged-in user.
     * The user's identity is retrieved from the security context.
     * This method will handle the creation of the feedback record,
     * generate a unique event ID, and trigger the initial AI processing.
     *
     * @param request     The DTO containing all necessary feedback information.
     * @return The newly created and persisted Feedback entity.
     */
    Feedback submitFeedback(FeedbackSubmissionRequest request, MultipartFile[] files);

    Page<Feedback> getAllFeedback(Pageable pageable);

    /**
     * Submits a new feedback report from the public (anonymous user).
     * This method will handle the creation of the feedback record from a public request,
     * and trigger the initial AI processing.
     *
     * @param request The DTO containing the public feedback information.
     * @param files An array of uploaded image files (optional).
     * @return The newly created and persisted Feedback entity.
     */
    Feedback createPublicFeedback(PublicFeedbackRequest request, MultipartFile[] files);
} 