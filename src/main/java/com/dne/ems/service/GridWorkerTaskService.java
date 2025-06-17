package com.dne.ems.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dne.ems.dto.TaskDetailDTO;
import com.dne.ems.dto.TaskSubmissionRequest;
import com.dne.ems.dto.TaskSummaryDTO;
import com.dne.ems.model.enums.TaskStatus;

/**
 * Service interface for grid workers to manage their assigned tasks.
 */
public interface GridWorkerTaskService {

    /**
     * Retrieves a paginated list of tasks assigned to a specific grid worker.
     *
     * @param workerId The ID of the grid worker.
     * @param status Optional filter by task status.
     * @param pageable Pagination and sorting information.
     * @return A page of task summaries.
     */
    Page<TaskSummaryDTO> getAssignedTasks(Long workerId, TaskStatus status, Pageable pageable);

    /**
     * Allows a grid worker to accept an assigned task.
     *
     * @param taskId The ID of the task to accept.
     * @param workerId The ID of the worker accepting the task, for validation.
     * @return A summary of the updated task.
     */
    TaskSummaryDTO acceptTask(Long taskId, Long workerId);
    /**
     * Submits the completion details for a task.
     *
     * @param taskId The ID of the task being completed.
     * @param workerId The ID of the worker submitting the task, for validation.
     * @param request The submission request DTO.
     * @return A summary of the updated task.
     */
    TaskSummaryDTO submitTaskCompletion(Long taskId, Long workerId, TaskSubmissionRequest request);

    /**
     * Retrieves the details of a specific task assigned to a worker.
     *
     * @param taskId The ID of the task.
     * @param workerId The ID of the worker requesting the details, for validation.
     * @return The full details of the task.
     */
    TaskDetailDTO getTaskDetails(Long taskId, Long workerId);
} 