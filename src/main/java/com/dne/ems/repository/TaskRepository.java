package com.dne.ems.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.dne.ems.model.Task;
import com.dne.ems.model.enums.TaskStatus;

/**
 * Spring Data JPA repository for the {@link Task} entity.
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {

    long countByStatus(TaskStatus status);

    long countByAssigneeIdAndStatusIn(Long assigneeId, Collection<TaskStatus> statuses);

    List<Task> findByAssigneeIdAndStatus(Long assigneeId, TaskStatus status);
} 