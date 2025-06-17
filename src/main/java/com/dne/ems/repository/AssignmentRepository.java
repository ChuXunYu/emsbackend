package com.dne.ems.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dne.ems.model.Assignment;

/**
 * JPA Repository for {@link Assignment} entities.
 */
@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    List<Assignment> findByAssigneeId(Long assigneeId);
} 