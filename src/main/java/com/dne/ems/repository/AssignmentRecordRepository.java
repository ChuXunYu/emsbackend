package com.dne.ems.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dne.ems.model.AssignmentRecord;

/**
 * Spring Data JPA repository for the {@link AssignmentRecord} entity.
 */
@Repository
public interface AssignmentRecordRepository extends JpaRepository<AssignmentRecord, Long> {
    // Basic CRUD operations are inherited from JpaRepository.
    // Custom query methods can be added here as needed in the future.
} 