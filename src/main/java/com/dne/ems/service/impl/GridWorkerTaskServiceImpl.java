package com.dne.ems.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.dne.ems.dto.TaskDetailDTO;
import com.dne.ems.dto.TaskHistoryDTO;
import com.dne.ems.dto.TaskSubmissionRequest;
import com.dne.ems.dto.TaskSummaryDTO;
import com.dne.ems.exception.InvalidOperationException;
import com.dne.ems.exception.ResourceNotFoundException;
import com.dne.ems.model.Feedback;
import com.dne.ems.model.Task;
import com.dne.ems.model.TaskHistory;
import com.dne.ems.model.TaskSubmission;
import com.dne.ems.model.UserAccount;
import com.dne.ems.model.enums.SeverityLevel;
import com.dne.ems.model.enums.TaskStatus;
import com.dne.ems.repository.TaskHistoryRepository;
import com.dne.ems.repository.TaskRepository;
import com.dne.ems.repository.TaskSubmissionRepository;
import com.dne.ems.repository.UserAccountRepository;
import com.dne.ems.security.CustomUserDetails;
import com.dne.ems.service.GridWorkerTaskService;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GridWorkerTaskServiceImpl implements GridWorkerTaskService {

    private final TaskRepository taskRepository;
    private final TaskHistoryRepository taskHistoryRepository;
    private final TaskSubmissionRepository taskSubmissionRepository;
    @SuppressWarnings("unused")
    private final UserAccountRepository userAccountRepository;

    @Value("${app.base.url}")
    private String appBaseUrl;

    @Override
    public Page<TaskSummaryDTO> getAssignedTasks(Long workerId, TaskStatus status, Pageable pageable) {
        if (pageable.getSort().isUnsorted()) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createdAt").descending());
        }
        Specification<Task> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("assignee").get("id"), workerId));
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            return cb.and(predicates.toArray(Predicate[]::new));
        };
        return taskRepository.findAll(spec, pageable).map(this::convertToSummaryDto);
    }

    @Override
    public TaskSummaryDTO acceptTask(Long taskId, Long workerId) {
        Task task = validateTaskOwnership(taskId, workerId);
        if (task.getStatus() != TaskStatus.ASSIGNED) {
            throw new InvalidOperationException("Task cannot be accepted. Current status: " + task.getStatus());
        }
        TaskStatus oldStatus = task.getStatus();
        task.setStatus(TaskStatus.IN_PROGRESS);
        Task updatedTask = taskRepository.save(task);
        logTaskHistory(updatedTask, oldStatus, updatedTask.getStatus(), "Task accepted by worker.");
        return convertToSummaryDto(updatedTask);
    }

    @Override
    public TaskSummaryDTO submitTaskCompletion(Long taskId, Long workerId, TaskSubmissionRequest request) {
        Task task = validateTaskOwnership(taskId, workerId);
        if (task.getStatus() != TaskStatus.IN_PROGRESS) {
            throw new InvalidOperationException("Task must be IN_PROGRESS to be submitted. Current status: " + task.getStatus());
        }
        TaskSubmission submission = new TaskSubmission(task, request.notes());
        taskSubmissionRepository.save(submission);
        TaskStatus oldStatus = task.getStatus();
        task.setStatus(TaskStatus.SUBMITTED);
        Task updatedTask = taskRepository.save(task);
        logTaskHistory(updatedTask, oldStatus, updatedTask.getStatus(), "Worker submitted notes: " + request.notes());
        return convertToSummaryDto(updatedTask);
    }

    @Override
    public TaskDetailDTO getTaskDetails(Long taskId, Long workerId) {
        Task task = validateTaskOwnership(taskId, workerId);
        return convertToDetailDto(task);
    }
    
    private Task validateTaskOwnership(Long taskId, Long workerId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        if (task.getAssignee() == null || !Objects.equals(task.getAssignee().getId(), workerId)) {
            throw new AccessDeniedException("This task is not assigned to you.");
        }
        return task;
    }

    private void logTaskHistory(Task task, TaskStatus oldStatus, TaskStatus newStatus, String comments) {
        UserAccount currentUser = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserAccount();
        TaskHistory history = new TaskHistory(task, oldStatus, newStatus, currentUser, comments);
        taskHistoryRepository.save(history);
    }
    
    private TaskSummaryDTO convertToSummaryDto(Task task) {
        SeverityLevel severity = task.getFeedback() != null ? task.getFeedback().getSeverityLevel() : task.getSeverityLevel();
        String imageUrl = "";
        if (task.getFeedback() != null && task.getFeedback().getAttachments() != null && !task.getFeedback().getAttachments().isEmpty()) {
            imageUrl = appBaseUrl + "/api/files/view/" + task.getFeedback().getAttachments().get(0).getStoredFileName();
        }
        return new TaskSummaryDTO(
            task.getId(),
            task.getTitle(),
            task.getDescription(),
            task.getStatus(),
            task.getAssignee() != null ? task.getAssignee().getName() : null,
            task.getCreatedAt(),
            task.getTextAddress(),
            imageUrl,
            severity
        );
    }

    private TaskDetailDTO convertToDetailDto(Task task) {
        List<TaskHistoryDTO> history = task.getHistory().stream().map(this::convertToHistoryDto).collect(Collectors.toList());
        UserAccount assignee = task.getAssignee();
        Feedback feedback = task.getFeedback();
        
        TaskDetailDTO.FeedbackDTO feedbackDTO = null;
        if (feedback != null) {
            UserAccount submitter = feedback.getUser();
            List<String> imageUrls = feedback.getAttachments().stream()
                .map(attachment -> appBaseUrl + "/api/files/view/" + attachment.getStoredFileName())
                .toList();
            feedbackDTO = new TaskDetailDTO.FeedbackDTO(
                feedback.getId(), feedback.getEventId(), feedback.getTitle(),
                feedback.getDescription(), feedback.getSeverityLevel(), feedback.getTextAddress(),
                submitter != null ? submitter.getId() : null,
                submitter != null ? submitter.getName() : null,
                feedback.getCreatedAt(), imageUrls
            );
        }

        TaskDetailDTO.AssigneeDTO assigneeDTO = null;
        if (assignee != null) {
            assigneeDTO = new TaskDetailDTO.AssigneeDTO(
                assignee.getId(), assignee.getName(), assignee.getPhone()
            );
        }

        return new TaskDetailDTO(
            task.getId(), feedbackDTO, task.getTitle(), task.getDescription(),
            task.getSeverityLevel(), task.getPollutionType(), task.getTextAddress(),
            task.getGridX(), task.getGridY(), task.getStatus(),
            assigneeDTO, task.getAssignedAt(), task.getCompletedAt(), history
        );
    }

    private TaskHistoryDTO convertToHistoryDto(TaskHistory history) {
        UserAccount changedBy = history.getChangedBy();
        return new TaskHistoryDTO(
            history.getId(),
            history.getOldStatus(),
            history.getNewStatus(),
            history.getComments(),
            history.getChangedAt(),
            new TaskHistoryDTO.UserDTO(changedBy.getId(), changedBy.getName())
        );
    }
}