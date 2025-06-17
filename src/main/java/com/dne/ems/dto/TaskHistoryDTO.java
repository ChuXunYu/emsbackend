package com.dne.ems.dto;

import java.time.LocalDateTime;

import com.dne.ems.model.enums.TaskStatus;

public record TaskHistoryDTO(
        Long id,
        TaskStatus oldStatus,
        TaskStatus newStatus,
        String comments,
        LocalDateTime changedAt,
        UserDTO changedBy
) {
    public record UserDTO(Long id, String name) {}
}