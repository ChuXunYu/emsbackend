package com.dne.ems.dto;

import com.dne.ems.model.enums.PollutionType;
import com.dne.ems.model.enums.SeverityLevel;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object for submitting a new feedback report.
 *
 * @param title       A brief title for the feedback report.
 * @param description A detailed description of the issue.
 * @param pollutionType The type of pollution being reported.
 * @param severityLevel The perceived severity of the issue.
 * @param location    The geographical location data for the report.
 */
public record FeedbackSubmissionRequest(
    @NotEmpty(message = "标题不能为空")
    @Size(max = 255, message = "标题长度不能超过255个字符")
    String title,

    String description,

    @NotNull(message = "污染类型不能为空")
    PollutionType pollutionType,

    @NotNull(message = "严重等级不能为空")
    SeverityLevel severityLevel,

    @NotNull(message = "地理位置信息不能为空")
    @Valid
    LocationData location
) {
    /**
     * Nested record to represent geographical location data.
     *
     * @param latitude      The geographical latitude of the event.
     * @param longitude     The geographical longitude of the event.
     * @param textAddress   A human-readable address string (e.g., "省-市-区").
     * @param gridX         The X coordinate on the system's internal grid map.
     * @param gridY         The Y coordinate on the system's internal grid map.
     */
    public record LocationData(
        Double latitude,
        Double longitude,
        @NotEmpty(message = "文字地址不能为空")
        String textAddress,

        @NotNull(message = "网格X坐标不能为空")
        Integer gridX,

        @NotNull(message = "网格Y坐标不能为空")
        Integer gridY
    ) {}
} 