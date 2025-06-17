package com.dne.ems.listener;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.dne.ems.event.FeedbackSubmittedForAiReviewEvent;
import com.dne.ems.service.AiReviewService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class FeedbackEventListener {

    private final AiReviewService aiReviewService;

    @Async
    @EventListener
    public void handleFeedbackSubmittedForAiReview(FeedbackSubmittedForAiReviewEvent event) {
        log.info("Received feedback submission event for AI review. Feedback ID: {}", event.getFeedback().getId());
        aiReviewService.reviewFeedback(event.getFeedback());
    }
} 