package com.dne.ems.service.impl;

import com.dne.ems.dto.UserFeedbackSummaryDTO;
import com.dne.ems.model.Feedback;
import com.dne.ems.repository.FeedbackRepository;
import com.dne.ems.service.UserFeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserFeedbackServiceImpl implements UserFeedbackService {

    private final FeedbackRepository feedbackRepository;

    @Value("${app.base.url}")
    private String appBaseUrl;

    @Override
    public Page<UserFeedbackSummaryDTO> getFeedbackHistoryByUserId(Long userId, Pageable pageable) {
        Page<Feedback> feedbackPage = feedbackRepository.findBySubmitterId(userId, pageable);
        return feedbackPage.map(this::convertToDto);
    }

    private UserFeedbackSummaryDTO convertToDto(Feedback feedback) {
        String imageUrl = null;
        if (feedback.getAttachments() != null && !feedback.getAttachments().isEmpty()) {
            imageUrl = appBaseUrl + "/api/files/view/" + feedback.getAttachments().get(0).getStoredFileName();
        }

        return new UserFeedbackSummaryDTO(
                feedback.getEventId(),
                feedback.getTitle(),
                feedback.getStatus(),
                feedback.getCreatedAt(),
                imageUrl
        );
    }
} 