package com.dne.ems.service;

import java.util.List;

import com.dne.ems.model.Assignment;
import com.dne.ems.model.Feedback;
import com.dne.ems.model.UserAccount;

/**
 * Service interface for handling task assignment operations.
 */
public interface TaskAssignmentService {

    /**
     * Retrieves a list of all feedback entries that are pending assignment.
     *
     * @return A list of {@link Feedback} objects with a status of PENDING_ASSIGNMENT.
     */
    List<Feedback> getUnassignedFeedback();

    /**
     * Retrieves a list of all available grid workers.
     *
     * @return A list of {@link UserAccount} objects with the role GRID_WORKER.
     */
    List<UserAccount> getAvailableGridWorkers();

    /**
     * Assigns a feedback task to a grid worker.
     *
     * @param feedbackId The ID of the feedback to be assigned.
     * @param assigneeId The ID of the grid worker (UserAccount) to whom the task is assigned.
     * @param assignerId The ID of the admin (UserAccount) who is assigning the task.
     * @return The newly created {@link Assignment} object.
     */
    Assignment assignTask(Long feedbackId, Long assigneeId, Long assignerId);

} 