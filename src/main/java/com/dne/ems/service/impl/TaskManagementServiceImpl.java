package com.dne.ems.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.dne.ems.dto.TaskApprovalRequest;
import com.dne.ems.dto.TaskAssignmentRequest;
import com.dne.ems.dto.TaskCreationRequest;
import com.dne.ems.dto.TaskDetailDTO;
import com.dne.ems.dto.TaskFromFeedbackRequest;
import com.dne.ems.dto.TaskHistoryDTO;
import com.dne.ems.dto.TaskSummaryDTO;
import com.dne.ems.event.TaskReadyForAssignmentEvent;
import com.dne.ems.exception.InvalidOperationException;
import com.dne.ems.exception.ResourceNotFoundException;
import com.dne.ems.model.Feedback;
import com.dne.ems.model.Task;
import com.dne.ems.model.TaskHistory;
import com.dne.ems.model.UserAccount;
import com.dne.ems.model.enums.FeedbackStatus;
import com.dne.ems.model.enums.PollutionType;
import com.dne.ems.model.enums.Role;
import com.dne.ems.model.enums.SeverityLevel;
import com.dne.ems.model.enums.TaskStatus;
import com.dne.ems.repository.FeedbackRepository;
import com.dne.ems.repository.TaskHistoryRepository;
import com.dne.ems.repository.TaskRepository;
import com.dne.ems.repository.UserAccountRepository;
import com.dne.ems.security.CustomUserDetails;
import com.dne.ems.service.TaskManagementService;
import com.dne.ems.service.pathfinding.AStarService;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskManagementServiceImpl implements TaskManagementService {

    private final TaskRepository taskRepository;
    private final UserAccountRepository userAccountRepository;
    private final TaskHistoryRepository taskHistoryRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final FeedbackRepository feedbackRepository;
    @SuppressWarnings("unused")
    private final AStarService aStarService;

    @Override
    public Page<TaskSummaryDTO> getTasks(
        TaskStatus status, Long assigneeId, SeverityLevel severity,
        PollutionType pollutionType, LocalDate startDate, LocalDate endDate, Pageable pageable
    ) {
        if (pageable.getSort().isUnsorted()) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createdAt").descending());
        }
        Specification<Task> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (status != null) predicates.add(cb.equal(root.get("status"), status));
            if (assigneeId != null) predicates.add(cb.equal(root.get("assignee").get("id"), assigneeId));
            if (severity != null) predicates.add(cb.or(cb.equal(root.get("feedback").get("severityLevel"), severity), cb.equal(root.get("severityLevel"), severity)));
            if (pollutionType != null) predicates.add(cb.or(cb.equal(root.get("feedback").get("pollutionType"), pollutionType), cb.equal(root.get("pollutionType"), pollutionType)));
            if (startDate != null) predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), startDate.atStartOfDay()));
            if (endDate != null) predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), endDate.atTime(23, 59, 59)));
            return cb.and(predicates.toArray(Predicate[]::new));
        };
        return taskRepository.findAll(spec, pageable).map(this::convertToSummaryDto);
    }

    @Override
    public TaskSummaryDTO assignTask(Long taskId, TaskAssignmentRequest request) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        if (task.getStatus() != TaskStatus.PENDING_ASSIGNMENT && task.getStatus() != TaskStatus.ASSIGNED) {
            throw new InvalidOperationException("Task must be pending or already assigned.");
        }
        UserAccount assignee = userAccountRepository.findById(request.assigneeId()).orElseThrow(() -> new ResourceNotFoundException("Assignee not found"));
        if (assignee.getRole() != Role.GRID_WORKER) {
            throw new InvalidOperationException("Cannot assign tasks to non-grid workers.");
        }
        TaskStatus oldStatus = task.getStatus();
        task.setAssignee(assignee);
        task.setStatus(TaskStatus.ASSIGNED);
        task.setAssignedAt(LocalDateTime.now());
        Task updatedTask = taskRepository.save(task);
        logTaskHistory(updatedTask, oldStatus, updatedTask.getStatus(), "Task assigned to " + assignee.getName());
        return convertToSummaryDto(updatedTask);
    }

    @Override
    public TaskDetailDTO getTaskDetails(Long taskId) {
        return taskRepository.findById(taskId).map(this::convertToDetailDto).orElseThrow(() -> new ResourceNotFoundException("Task not found"));
    }

    @Override
    public TaskDetailDTO reviewTask(Long taskId, TaskApprovalRequest request) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        if (task.getStatus() != TaskStatus.SUBMITTED) {
            throw new InvalidOperationException("Task must be SUBMITTED to be reviewed.");
        }
        TaskStatus oldStatus = task.getStatus();
        task.setStatus(request.approved() ? TaskStatus.COMPLETED : TaskStatus.ASSIGNED);
        if (request.approved()) task.setCompletedAt(LocalDateTime.now());
        Task updatedTask = taskRepository.save(task);
        logTaskHistory(updatedTask, oldStatus, updatedTask.getStatus(), request.comments());
        return convertToDetailDto(updatedTask);
    }
    
    @Override
    public TaskDetailDTO cancelTask(Long taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        if (task.getStatus() == TaskStatus.COMPLETED || task.getStatus() == TaskStatus.CANCELLED) {
            throw new InvalidOperationException("Cannot cancel a task that is already completed or cancelled.");
        }
        TaskStatus oldStatus = task.getStatus();
        task.setStatus(TaskStatus.CANCELLED);
        Task updatedTask = taskRepository.save(task);
        logTaskHistory(updatedTask, oldStatus, updatedTask.getStatus(), "Task manually cancelled.");
        return convertToDetailDto(updatedTask);
    }

    @Override
    public List<TaskHistoryDTO> getTaskHistory(Long taskId) {
        if (!taskRepository.existsById(taskId)) throw new ResourceNotFoundException("Task not found");
        return taskHistoryRepository.findByTaskIdOrderByChangedAtDesc(taskId).stream().map(this::convertToHistoryDto).collect(Collectors.toList());
    }

    @Override
    public TaskDetailDTO createTask(TaskCreationRequest request) {
        Task task = new Task();
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setPollutionType(request.pollutionType());
        task.setSeverityLevel(request.severityLevel());
        task.setLatitude(request.location().latitude());
        task.setLongitude(request.location().longitude());
        task.setTextAddress(request.location().textAddress());
        task.setGridX(request.location().gridX());
        task.setGridY(request.location().gridY());
        task.setStatus(TaskStatus.PENDING_ASSIGNMENT);
        task.setCreatedBy(getCurrentUser());
        task.setCreatedAt(LocalDateTime.now());
        Task createdTask = taskRepository.save(task);
        logTaskHistory(createdTask, null, createdTask.getStatus(), "Task created manually.");
        eventPublisher.publishEvent(new TaskReadyForAssignmentEvent(this, createdTask));
        return convertToDetailDto(createdTask);
    }

    @Override
    public List<Feedback> getPendingFeedback() {
        return feedbackRepository.findByStatus(FeedbackStatus.PENDING_REVIEW);
    }

    @Override
    public TaskDetailDTO createTaskFromFeedback(Long feedbackId, TaskFromFeedbackRequest request) {
        Feedback feedback = feedbackRepository.findById(feedbackId).orElseThrow(() -> new ResourceNotFoundException("Feedback not found"));
        if (feedback.getStatus() != FeedbackStatus.PENDING_REVIEW) {
            throw new InvalidOperationException("Feedback must be in PENDING_REVIEW state.");
        }
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(feedback.getDescription());
        task.setPollutionType(feedback.getPollutionType());
        task.setSeverityLevel(request.getSeverityLevel());
        task.setLatitude(feedback.getLatitude());
        task.setLongitude(feedback.getLongitude());
        task.setTextAddress(feedback.getTextAddress());
        task.setGridX(feedback.getGridX());
        task.setGridY(feedback.getGridY());
        task.setStatus(TaskStatus.PENDING_ASSIGNMENT);
        task.setFeedback(feedback);
        task.setCreatedBy(getCurrentUser());
        task.setCreatedAt(LocalDateTime.now());
        Task createdTask = taskRepository.save(task);
        feedback.setStatus(FeedbackStatus.PROCESSED);
        feedbackRepository.save(feedback);
        logTaskHistory(createdTask, null, createdTask.getStatus(), "Task created from feedback ID: " + feedbackId);
        eventPublisher.publishEvent(new TaskReadyForAssignmentEvent(this, createdTask));
        return convertToDetailDto(createdTask);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Override
    public void handleTaskReadyForAssignment(TaskReadyForAssignmentEvent event) {
        log.info("Received task ready for assignment event for task ID: {}", event.getTask().getId());
        intelligentAssignTask(event.getTask());
    }

    private void intelligentAssignTask(Task task) {
        log.info("Intelligently assigning task {}", task.getId());
    }

    private UserAccount getCurrentUser() {
        return ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserAccount();
    }
    
    private void logTaskHistory(Task task, TaskStatus oldStatus, TaskStatus newStatus, String comments) {
        TaskHistory history = new TaskHistory();
        history.setTask(task);
        history.setOldStatus(oldStatus);
        history.setNewStatus(newStatus);
        history.setComments(comments);
        history.setChangedAt(LocalDateTime.now());
        history.setChangedBy(getCurrentUser());
        taskHistoryRepository.save(history);
    }

    private TaskSummaryDTO convertToSummaryDto(Task task) {
        SeverityLevel severity = task.getFeedback() != null ? task.getFeedback().getSeverityLevel() : task.getSeverityLevel();
        return new TaskSummaryDTO(
            task.getId(),
            task.getTitle(),
            task.getDescription(),
            task.getStatus(),
            task.getAssignee() != null ? task.getAssignee().getName() : null,
            task.getCreatedAt(),
            task.getTextAddress(),
            null, 
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
            feedbackDTO = new TaskDetailDTO.FeedbackDTO(
                feedback.getId(), feedback.getEventId(), feedback.getTitle(),
                feedback.getDescription(), feedback.getSeverityLevel(), feedback.getTextAddress(),
                submitter != null ? submitter.getId() : null,
                submitter != null ? submitter.getName() : null,
                feedback.getCreatedAt(), Collections.emptyList()
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