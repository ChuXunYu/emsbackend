package com.dne.ems.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dne.ems.dto.PublicFeedbackRequest;
import com.dne.ems.model.Feedback;
import com.dne.ems.service.FeedbackService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
public class PublicController {

    private final FeedbackService feedbackService;

    /**
     * Endpoint for the public to submit feedback, including optional image uploads.
     * This endpoint is open and does not require authentication.
     * It consumes multipart/form-data.
     *
     * @param request The public feedback request DTO, sent as a JSON part.
     * @param files   An array of uploaded image files (optional).
     * @return The created feedback entity.
     */
    @PostMapping(value = "/feedback", consumes = "multipart/form-data")
    public ResponseEntity<Feedback> submitPublicFeedback(
            @Valid @RequestPart("feedback") PublicFeedbackRequest request,
            @RequestPart(value = "files", required = false) MultipartFile[] files) {
        // We will need to update the service method to handle files.
        // For now, let's pass null and focus on the controller signature.
        Feedback createdFeedback = feedbackService.createPublicFeedback(request, files);
        return new ResponseEntity<>(createdFeedback, HttpStatus.CREATED);
    }
} 