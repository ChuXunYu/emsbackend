package com.dne.ems.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dne.ems.dto.TaskApprovalRequest;
import com.dne.ems.dto.TaskAssignmentRequest;
import com.dne.ems.dto.TaskCreationRequest;
import com.dne.ems.dto.TaskDetailDTO;
import com.dne.ems.dto.TaskFromFeedbackRequest;
import com.dne.ems.dto.TaskHistoryDTO;
import com.dne.ems.dto.TaskSummaryDTO;
import com.dne.ems.event.TaskReadyForAssignmentEvent;
import com.dne.ems.model.Feedback;
import com.dne.ems.model.enums.PollutionType;
import com.dne.ems.model.enums.SeverityLevel;
import com.dne.ems.model.enums.TaskStatus;

/**
 * Service interface for task management operations by administrators.
 */
public interface TaskManagementService {

    /**
     * Retrieves a paginated and filtered list of tasks for administrators.
     *
     * @param status Optional filter by task status.
     * @param assigneeId Optional filter by the assigned grid worker's ID.
     * @param severity Optional filter by severity level.
     * @param pollutionType Optional filter by pollution type.
     * @param startDate Optional filter for tasks created on or after this date.
     * @param endDate Optional filter for tasks created on or before this date.
     * @param pageable Pagination and sorting information.
     * @return A {@link Page} of {@link TaskSummaryDTO} objects.
     */
    Page<TaskSummaryDTO> getTasks(
            TaskStatus status,
            Long assigneeId,
            SeverityLevel severity,
            PollutionType pollutionType,
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable
    );

    /**
     * Assigns a task to a grid worker.
     *
     * @param taskId The ID of the task to assign.
     * @param request The assignment request containing the assignee's ID.
     * @return A summary of the updated task.
     */
    TaskSummaryDTO assignTask(Long taskId, TaskAssignmentRequest request);

    /**
     * Retrieves the full details of a specific task.
     *
     * @param taskId The ID of the task to retrieve.
     * @return A {@link TaskDetailDTO} containing the task's details.
     */
    TaskDetailDTO getTaskDetails(Long taskId);

    /**
     * Processes an administrator's approval or rejection of a submitted task.
     *
     * @param taskId The ID of the task to review.
     * @param request The approval request.
     * @return The updated task details.
     */
    TaskDetailDTO reviewTask(Long taskId, TaskApprovalRequest request);

    /**
     * Cancels a task.
     *
     * @param taskId The ID of the task to cancel.
     * @return The updated task details.
     */
    TaskDetailDTO cancelTask(Long taskId);

    /**
     * Retrieves the history of a specific task.
     *
     * @param taskId The ID of the task.
     * @return A list of {@link TaskHistoryDTO} objects representing the task's history.
     */
    List<TaskHistoryDTO> getTaskHistory(Long taskId);

    TaskDetailDTO createTask(TaskCreationRequest request);

    List<Feedback> getPendingFeedback();

    TaskDetailDTO createTaskFromFeedback(Long feedbackId, TaskFromFeedbackRequest request);

    void handleTaskReadyForAssignment(TaskReadyForAssignmentEvent event);
} 