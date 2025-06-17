package com.dne.ems.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dne.ems.model.Feedback;
import com.dne.ems.service.SupervisorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/supervisor")
@RequiredArgsConstructor
@Tag(name = "Supervisor", description = "Endpoints for supervisor to review and manage feedback")
public class SupervisorController {

    private final SupervisorService supervisorService;

    @GetMapping("/reviews")
    @PreAuthorize("hasAnyRole('SUPERVISOR', 'ADMIN')")
    @Operation(summary = "Get feedback list pending for supervisor review")
    public ResponseEntity<List<Feedback>> getFeedbackForReview() {
        List<Feedback> feedbackList = supervisorService.getFeedbackForReview();
        return ResponseEntity.ok(feedbackList);
    }

    @PostMapping("/reviews/{feedbackId}/approve")
    @PreAuthorize("hasAnyRole('SUPERVISOR', 'ADMIN')")
    @Operation(summary = "Approve a feedback")
    public ResponseEntity<Void> approveFeedback(@PathVariable Long feedbackId) {
        supervisorService.approveFeedback(feedbackId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reviews/{feedbackId}/reject")
    @PreAuthorize("hasAnyRole('SUPERVISOR', 'ADMIN')")
    @Operation(summary = "Reject a feedback")
    public ResponseEntity<Void> rejectFeedback(@PathVariable Long feedbackId) {
        supervisorService.rejectFeedback(feedbackId);
        return ResponseEntity.ok().build();
    }
} 