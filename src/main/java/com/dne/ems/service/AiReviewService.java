package com.dne.ems.service;

import com.dne.ems.model.Feedback;

public interface AiReviewService {

    /**
     * Reviews the given feedback using an AI model.
     * This method will contain the logic to call the external AI service
     * and update the feedback status based on the result.
     *
     * @param feedback The feedback entity to be reviewed.
     */
    void reviewFeedback(Feedback feedback);
} 