package com.dne.ems.service.impl;

import com.dne.ems.model.Feedback;
import com.dne.ems.model.enums.FeedbackStatus;
import com.dne.ems.repository.FeedbackRepository;
import com.dne.ems.service.SupervisorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SupervisorServiceImpl implements SupervisorService {

    private final FeedbackRepository feedbackRepository;

    @Override
    public List<Feedback> getFeedbackForReview() {
        log.info("Fetching feedback for supervisor review with status PENDING_REVIEW.");
        return feedbackRepository.findByStatus(FeedbackStatus.PENDING_REVIEW);
    }

    @Override
    @Transactional
    public void approveFeedback(Long feedbackId) {
        log.info("Attempting to approve feedback with ID: {}", feedbackId);
        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new IllegalArgumentException("Feedback not found with ID: " + feedbackId));

        if (feedback.getStatus() != FeedbackStatus.PENDING_REVIEW) {
            log.warn("Feedback {} cannot be approved because its status is {} instead of PENDING_REVIEW.", feedbackId, feedback.getStatus());
            throw new IllegalStateException("Feedback is not in a PENDING_REVIEW state.");
        }

        feedback.setStatus(FeedbackStatus.PENDING_ASSIGNMENT);
        feedbackRepository.save(feedback);
        log.info("Feedback {} approved successfully. Status changed to PENDING_ASSIGNMENT.", feedbackId);
    }

    @Override
    @Transactional
    public void rejectFeedback(Long feedbackId) {
        log.info("Attempting to reject feedback with ID: {}", feedbackId);
        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new IllegalArgumentException("Feedback not found with ID: " + feedbackId));

        if (feedback.getStatus() != FeedbackStatus.PENDING_REVIEW) {
            log.warn("Feedback {} cannot be rejected because its status is {} instead of PENDING_REVIEW.", feedbackId, feedback.getStatus());
            throw new IllegalStateException("Feedback is not in a PENDING_REVIEW state.");
        }

        feedback.setStatus(FeedbackStatus.CLOSED_INVALID);
        feedbackRepository.save(feedback);
        log.info("Feedback {} rejected successfully. Status changed to CLOSED_INVALID.", feedbackId);
    }
} 