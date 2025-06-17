package com.dne.ems.controller;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dne.ems.dto.TaskDetailDTO;
import com.dne.ems.dto.TaskSubmissionRequest;
import com.dne.ems.dto.TaskSummaryDTO;
import com.dne.ems.model.enums.TaskStatus;
import com.dne.ems.security.CustomUserDetails;
import com.dne.ems.service.GridWorkerTaskService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/worker")
@RequiredArgsConstructor
@PreAuthorize("hasRole('GRID_WORKER')")
public class GridWorkerTaskController {

    private final GridWorkerTaskService gridWorkerTaskService;

    @GetMapping
    public ResponseEntity<Page<TaskSummaryDTO>> getMyTasks(
            @RequestParam(required = false) TaskStatus status,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Pageable pageable) {
        Page<TaskSummaryDTO> tasks = gridWorkerTaskService.getAssignedTasks(userDetails.getId(), status, pageable);
        return ResponseEntity.ok(tasks);
    }

    @PostMapping("/{taskId}/accept")
    public ResponseEntity<TaskSummaryDTO> acceptTask(
            @PathVariable Long taskId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        TaskSummaryDTO updatedTask = gridWorkerTaskService.acceptTask(taskId, userDetails.getId());
        return ResponseEntity.ok(updatedTask);
    }

    @PostMapping("/{taskId}/submit")
    public ResponseEntity<TaskSummaryDTO> submitTask(
            @PathVariable Long taskId,
            @RequestBody @Valid TaskSubmissionRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        TaskSummaryDTO updatedTask = gridWorkerTaskService.submitTaskCompletion(taskId, userDetails.getId(), request);
        return ResponseEntity.ok(updatedTask);
    }
    @GetMapping("/{taskId}")
    public ResponseEntity<TaskDetailDTO> getTaskDetails(
            @PathVariable Long taskId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        TaskDetailDTO taskDetails = gridWorkerTaskService.getTaskDetails(taskId, userDetails.getId());
        return ResponseEntity.ok(taskDetails);
    }
}