package com.dne.ems.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dne.ems.model.Assignment;
import com.dne.ems.model.Feedback;
import com.dne.ems.model.UserAccount;
import com.dne.ems.security.CustomUserDetails;
import com.dne.ems.service.TaskAssignmentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskAssignmentController {

    private final TaskAssignmentService taskAssignmentService;

    @GetMapping("/unassigned")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    public ResponseEntity<List<Feedback>> getUnassignedFeedback() {
        List<Feedback> feedbackList = taskAssignmentService.getUnassignedFeedback();
        return ResponseEntity.ok(feedbackList);
    }

    @GetMapping("/grid-workers")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    public ResponseEntity<List<UserAccount>> getAvailableGridWorkers() {
        List<UserAccount> workers = taskAssignmentService.getAvailableGridWorkers();
        return ResponseEntity.ok(workers);
    }

    @PostMapping("/assign")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    public ResponseEntity<Assignment> assignTask(
            @RequestBody AssignmentRequest request,
            @AuthenticationPrincipal CustomUserDetails adminDetails) {

        Long assignerId = adminDetails.getId();
        Assignment newAssignment = taskAssignmentService.assignTask(
                request.feedbackId(),
                request.assigneeId(),
                assignerId
        );
        return ResponseEntity.ok(newAssignment);
    }

    // A simple record to represent the assignment request payload
    public record AssignmentRequest(Long feedbackId, Long assigneeId) {}
} 