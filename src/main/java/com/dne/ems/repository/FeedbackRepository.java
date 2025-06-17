package com.dne.ems.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dne.ems.dto.HeatmapPointDTO;
import com.dne.ems.dto.PollutionStatsDTO;
import com.dne.ems.model.Feedback;
import com.dne.ems.model.enums.FeedbackStatus;

/**
 * Spring Data JPA repository for the {@link Feedback} entity.
 */
@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    /**
     * Finds a feedback entry by its unique event ID.
     *
     * @param eventId The business event ID to search for.
     * @return An {@link Optional} containing the found feedback, or {@link Optional#empty()} if not found.
     */
    Optional<Feedback> findByEventId(String eventId);

    List<Feedback> findByStatus(FeedbackStatus status);

    long countByStatus(FeedbackStatus status);

    @Query("""
            SELECT new com.dne.ems.dto.HeatmapPointDTO(f.gridX, f.gridY, COUNT(f.id))
            FROM Feedback f
            WHERE f.status = 'CONFIRMED' AND f.gridX IS NOT NULL AND f.gridY IS NOT NULL
            GROUP BY f.gridX, f.gridY
            """)
    List<HeatmapPointDTO> getHeatmapData();

    @Query("SELECT new com.dne.ems.dto.PollutionStatsDTO(f.pollutionType, COUNT(f)) FROM Feedback f GROUP BY f.pollutionType")
    List<PollutionStatsDTO> countByPollutionType();

    Page<Feedback> findBySubmitterId(Long submitterId, Pageable pageable);

} 