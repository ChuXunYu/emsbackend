package com.dne.ems.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.dne.ems.model.enums.FeedbackStatus;
import com.dne.ems.model.enums.PollutionType;
import com.dne.ems.model.enums.SeverityLevel;

import lombok.Data;

@Data
public class FeedbackDTO {
    private Long id;
    private String title;
    private String description;
    private PollutionType pollutionType;
    private SeverityLevel severityLevel;
    private Double latitude;
    private Double longitude;
    private String textAddress;
    private Integer gridX;
    private Integer gridY;
    private String eventId;
    private FeedbackStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long submitterId;
    private List<String> attachmentUrls;
} 