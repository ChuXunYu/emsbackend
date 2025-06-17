package com.dne.ems.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a historical record of Air Quality Index (AQI) for a specific location.
 * This entity is typically populated from external data sources or aggregated from {@link AqiData}.
 * It is used for trend analysis and heatmap generation.
 * Maps to the 'aqi_records' table.
 *
 * @version 1.0
 * @since 2025-06-16
 */
@Data
@Entity
@Table(name = "aqi_records")
@NoArgsConstructor
public class AqiRecord {

    /**
     * The unique identifier for the AQI record.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The name of the city where the AQI was recorded.
     */
    @Column(nullable = false)
    private String cityName;

    /**
     * The overall Air Quality Index (AQI) value.
     */
    @Column(nullable = false)
    private Integer aqiValue;

    /**
     * The timestamp when the record was created in the database.
     * This is automatically set upon creation and cannot be updated.
     */
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime recordTime;

    /**
     * The concentration of PM2.5 in µg/m³.
     */
    private Double pm25;
    /**
     * The concentration of PM10 in µg/m³.
     */
    private Double pm10;
    /**
     * The concentration of SO2 in µg/m³.
     */
    private Double so2;
    /**
     * The concentration of NO2 in µg/m³.
     */
    private Double no2;
    /**
     * The concentration of CO in mg/m³.
     */
    private Double co;
    /**
     * The concentration of O3 in µg/m³.
     */
    private Double o3;

    /**
     * The geographical latitude of the record.
     */
    private Double latitude;
    /**
     * The geographical longitude of the record.
     */
    private Double longitude;

    /**
     * The x-coordinate of the grid cell corresponding to the record's location.
     */
    @Column(name = "grid_x")
    private Integer gridX;

    /**
     * The y-coordinate of the grid cell corresponding to the record's location.
     */
    @Column(name = "grid_y")
    private Integer gridY;

    /**
     * The associated grid cell for this AQI record.
     */
    @ManyToOne
    @JoinColumn(name = "grid_id")
    private Grid grid;

    /**
     * Basic constructor for creating an AQI record with pollutant data.
     * Used primarily by the {@link com.dne.ems.config.DataInitializer}.
     *
     * @param cityName The city name.
     * @param aqiValue The AQI value.
     * @param recordTime The time of the record.
     * @param pm25 PM2.5 concentration.
     * @param pm10 PM10 concentration.
     * @param so2 SO2 concentration.
     * @param no2 NO2 concentration.
     * @param co CO concentration.
     * @param o3 O3 concentration.
     */
    public AqiRecord(String cityName, Integer aqiValue, LocalDateTime recordTime, Double pm25, Double pm10, Double so2, Double no2, Double co, Double o3) {
        this.cityName = cityName;
        this.aqiValue = aqiValue;
        this.recordTime = recordTime;
        this.pm25 = pm25;
        this.pm10 = pm10;
        this.so2 = so2;
        this.no2 = no2;
        this.co = co;
        this.o3 = o3;
    }

    /**
     * Extended constructor that includes geographical and grid information.
     *
     * @param cityName The city name.
     * @param aqiValue The AQI value.
     * @param recordTime The time of the record.
     * @param pm25 PM2.5 concentration.
     * @param pm10 PM10 concentration.
     * @param so2 SO2 concentration.
     * @param no2 NO2 concentration.
     * @param co CO concentration.
     * @param o3 O3 concentration.
     * @param latitude Geographical latitude.
     * @param longitude Geographical longitude.
     * @param gridX Grid x-coordinate.
     * @param gridY Grid y-coordinate.
     * @param grid The associated Grid entity.
     */
    public AqiRecord(String cityName, Integer aqiValue, LocalDateTime recordTime, Double pm25, Double pm10, Double so2, Double no2, Double co, Double o3, Double latitude, Double longitude, Integer gridX, Integer gridY, Grid grid) {
        this(cityName, aqiValue, recordTime, pm25, pm10, so2, no2, co, o3);
        this.latitude = latitude;
        this.longitude = longitude;
        this.gridX = gridX;
        this.gridY = gridY;
        this.grid = grid;
    }
}