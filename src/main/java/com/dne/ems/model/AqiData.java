package com.dne.ems.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * Represents Air Quality Index (AQI) data submitted by a grid worker.
 * This entity maps to the 'aqi_data' table and stores detailed pollutant information
 * collected at a specific grid location.
 *
 * @version 1.0
 * @since 2025-06-16
 */
@Entity
@Table(name = "aqi_data")
@Data
public class AqiData {

    /**
     * The unique identifier for the AQI data record.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The ID of the feedback this data is related to. 
     * This can be null if the worker submits data proactively without a corresponding feedback task.
     */
    private Long feedbackId;

    /**
     * The grid cell (location) where this data was recorded.
     * This field is mandatory.
     */
    @ManyToOne
    @JoinColumn(name = "grid_id", nullable = false)
    private Grid grid;

    /**
     * The ID of the grid worker who reported this data.
     * This field is mandatory.
     */
    @Column(nullable = false)
    private Long reporterId;

    /**
     * The overall Air Quality Index (AQI) value.
     */
    private Integer aqiValue;

    /**
     * The concentration of PM2.5 (fine particulate matter) in µg/m³.
     */
    private Double pm25;

    /**
     * The concentration of PM10 (inhalable particulate matter) in µg/m³.
     */
    private Double pm10;

    /**
     * The concentration of SO2 (Sulfur Dioxide) in µg/m³.
     */
    private Double so2;

    /**
     * The concentration of NO2 (Nitrogen Dioxide) in µg/m³.
     */
    private Double no2;

    /**
     * The concentration of CO (Carbon Monoxide) in mg/m³.
     */
    private Double co;

    /**
     * The concentration of O3 (Ozone) in µg/m³.
     */
    private Double o3;

    /**
     * The name of the primary pollutant contributing to the AQI value.
     */
    private String primaryPollutant;

    /**
     * The timestamp when this data was recorded.
     * Defaults to the current time upon creation.
     */
    @Column(nullable = false)
    private LocalDateTime recordTime = LocalDateTime.now();
} 