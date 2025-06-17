package com.dne.ems.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dne.ems.model.Attachment;

/**
 * Spring Data JPA repository for the {@link Attachment} entity.
 */
@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

    /**
     * Finds an attachment by its unique stored file name.
     *
     * @param storedFileName The unique name of the file to search for.
     * @return An {@link Optional} containing the found attachment, or empty if not found.
     */
    Optional<Attachment> findByStoredFileName(String storedFileName);

}