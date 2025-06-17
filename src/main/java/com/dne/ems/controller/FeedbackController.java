package com.dne.ems.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dne.ems.dto.FeedbackSubmissionRequest;
import com.dne.ems.model.Feedback;
import com.dne.ems.security.CustomUserDetails;
import com.dne.ems.service.FeedbackService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    /**
     * [FOR TESTING PURPOSES]
     * Endpoint for submitting feedback using application/json.
     * This is easier to use with curl for testing.
     * The original endpoint is /submit with multipart/form-data.
     */
    @PostMapping(value = "/submit-json")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Feedback> submitFeedbackJson(
            @Valid @RequestBody FeedbackSubmissionRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        
        Feedback createdFeedback = feedbackService.submitFeedback(request, null); // No files for this endpoint
        return new ResponseEntity<>(createdFeedback, HttpStatus.CREATED);
    }

    @PostMapping(value = "/submit", consumes = {"multipart/form-data"})
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Feedback> submitFeedback(
            @Valid @RequestPart("feedback") FeedbackSubmissionRequest request,
            @RequestPart(value = "files", required = false) MultipartFile[] files,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        
        Feedback createdFeedback = feedbackService.submitFeedback(request, files);
        return new ResponseEntity<>(createdFeedback, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<Feedback>> getAllFeedback(Pageable pageable) {
        Page<Feedback> feedbackPage = feedbackService.getAllFeedback(pageable);
        return ResponseEntity.ok(feedbackPage);
    }
}