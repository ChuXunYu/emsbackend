package com.dne.ems.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dne.ems.model.Assignment;
import com.dne.ems.model.Feedback;
import com.dne.ems.model.Task;
import com.dne.ems.model.UserAccount;
import com.dne.ems.model.enums.AssignmentStatus;
import com.dne.ems.model.enums.FeedbackStatus;
import com.dne.ems.model.enums.Role;
import com.dne.ems.model.enums.TaskStatus;
import com.dne.ems.repository.AssignmentRepository;
import com.dne.ems.repository.FeedbackRepository;
import com.dne.ems.repository.TaskRepository;
import com.dne.ems.repository.UserAccountRepository;
import com.dne.ems.service.TaskAssignmentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskAssignmentServiceImpl implements TaskAssignmentService {

    private final FeedbackRepository feedbackRepository;
    private final UserAccountRepository userAccountRepository;
    private final AssignmentRepository assignmentRepository;
    private final TaskRepository taskRepository;

    @Override
    public List<Feedback> getUnassignedFeedback() {
        // Unassigned feedback are those that have been approved and are awaiting assignment.
        return feedbackRepository.findByStatus(FeedbackStatus.PENDING_ASSIGNMENT);
    }

    @Override
    public List<UserAccount> getAvailableGridWorkers() {
        return userAccountRepository.findByRole(Role.GRID_WORKER);
    }

    @Override
    @Transactional
    public Assignment assignTask(Long feedbackId, Long assigneeId, Long assignerId) {
        // 1. Validate the feedback exists and is in a state that can be assigned.
        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new IllegalArgumentException("Feedback not found with ID: " + feedbackId));

        if (feedback.getStatus() != FeedbackStatus.PENDING_ASSIGNMENT) {
            throw new IllegalStateException("Feedback with ID " + feedbackId + " is not in a PENDING_ASSIGNMENT state and cannot be assigned.");
        }

        // 2. Validate the assignee is a valid grid worker.
        UserAccount assignee = userAccountRepository.findById(assigneeId)
                .orElseThrow(() -> new IllegalArgumentException("Assignee user not found with ID: " + assigneeId));

        if (assignee.getRole() != Role.GRID_WORKER) {
            throw new IllegalArgumentException("User with ID " + assigneeId + " is not a grid worker.");
        }

        // 3. Create the Task from the Feedback
        Task newTask = new Task();
        newTask.setFeedback(feedback);
        newTask.setAssignee(assignee);
        newTask.setStatus(TaskStatus.ASSIGNED);
        newTask.setTitle(feedback.getTitle());
        newTask.setDescription(feedback.getDescription());
        newTask.setSeverityLevel(feedback.getSeverityLevel());
        newTask.setLatitude(feedback.getLatitude());
        newTask.setLongitude(feedback.getLongitude());
        newTask.setTextAddress(feedback.getTextAddress());

        log.info("Attempting to save new task for feedback ID: {}. Setting status to: {}", feedback.getId(), newTask.getStatus());
        taskRepository.save(newTask);
        log.info("Successfully saved new task with ID: {}", newTask.getId());

        // 4. Create and save the new assignment record.
        Assignment newAssignment = new Assignment();
        newAssignment.setFeedbackId(feedbackId);
        newAssignment.setAssigneeId(assigneeId);
        newAssignment.setAssignerId(assignerId); // The admin performing the action
        newAssignment.setStatus(AssignmentStatus.PENDING);
        // deadline could be set here based on business rules, e.g., now() + 3 days
        assignmentRepository.save(newAssignment);

        // 5. Update the feedback status to ASSIGNED.
        feedback.setStatus(FeedbackStatus.ASSIGNED);
        feedbackRepository.save(feedback);

        return newAssignment;
    }
} 