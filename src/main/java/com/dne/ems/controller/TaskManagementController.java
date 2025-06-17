package com.dne.ems.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dne.ems.dto.TaskApprovalRequest;
import com.dne.ems.dto.TaskAssignmentRequest;
import com.dne.ems.dto.TaskCreationRequest;
import com.dne.ems.dto.TaskDetailDTO;
import com.dne.ems.dto.TaskFromFeedbackRequest;
import com.dne.ems.dto.TaskSummaryDTO;
import com.dne.ems.model.Feedback;
import com.dne.ems.model.enums.PollutionType;
import com.dne.ems.model.enums.SeverityLevel;
import com.dne.ems.model.enums.TaskStatus;
import com.dne.ems.service.TaskManagementService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/management/tasks")
@RequiredArgsConstructor
@PreAuthorize("hasRole('SUPERVISOR')")
public class TaskManagementController {

    private final TaskManagementService taskManagementService;

    @GetMapping
    public ResponseEntity<Page<TaskSummaryDTO>> getTasks(
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) Long assigneeId,
            @RequestParam(required = false) SeverityLevel severity,
            @RequestParam(required = false) PollutionType pollutionType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Pageable pageable) {
        Page<TaskSummaryDTO> tasks = taskManagementService.getTasks(status, assigneeId, severity, pollutionType, startDate, endDate, pageable);
        return ResponseEntity.ok(tasks);
    }

    @PostMapping("/{taskId}/assign")
    public ResponseEntity<TaskSummaryDTO> assignTask(
            @PathVariable Long taskId,
            @RequestBody @Valid TaskAssignmentRequest request) {
        TaskSummaryDTO updatedTask = taskManagementService.assignTask(taskId, request);
        return ResponseEntity.ok(updatedTask);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskDetailDTO> getTaskDetails(@PathVariable Long taskId) {
        TaskDetailDTO taskDetails = taskManagementService.getTaskDetails(taskId);
        return ResponseEntity.ok(taskDetails);
    }

    @PostMapping("/{taskId}/review")
    public ResponseEntity<TaskDetailDTO> reviewTask(
            @PathVariable Long taskId,
            @RequestBody @Valid TaskApprovalRequest request) {
        TaskDetailDTO updatedTask = taskManagementService.reviewTask(taskId, request);
        return ResponseEntity.ok(updatedTask);
    }

    @PostMapping("/{taskId}/cancel")
    public ResponseEntity<TaskDetailDTO> cancelTask(@PathVariable Long taskId) {
        TaskDetailDTO updatedTask = taskManagementService.cancelTask(taskId);
        return ResponseEntity.ok(updatedTask);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TaskDetailDTO> createTask(@RequestBody @Valid TaskCreationRequest request) {
        TaskDetailDTO createdTask = taskManagementService.createTask(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    @GetMapping("/feedback")
    @PreAuthorize("hasAnyRole('SUPERVISOR', 'ADMIN')")
    public ResponseEntity<List<Feedback>> getPendingFeedback() {
        List<Feedback> feedbackList = taskManagementService.getPendingFeedback();
        return ResponseEntity.ok(feedbackList);
    }

    @PostMapping("/feedback/{feedbackId}/create-task")
    @PreAuthorize("hasRole('SUPERVISOR')")
    public ResponseEntity<TaskDetailDTO> createTaskFromFeedback(
            @PathVariable Long feedbackId,
            @RequestBody @Valid TaskFromFeedbackRequest request) {
        TaskDetailDTO createdTask = taskManagementService.createTaskFromFeedback(feedbackId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }
} 