package com.dne.ems.event;

import org.springframework.context.ApplicationEvent;

import com.dne.ems.model.Feedback;

public class FeedbackSubmittedForAiReviewEvent extends ApplicationEvent {

    private final Feedback feedback;

    public FeedbackSubmittedForAiReviewEvent(Object source, Feedback feedback) {
        super(source);
        this.feedback = feedback;
    }

    public Feedback getFeedback() {
        return feedback;
    }
} 