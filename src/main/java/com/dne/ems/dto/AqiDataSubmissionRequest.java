package com.dne.ems.dto;

import jakarta.validation.constraints.NotNull;

/**
 * DTO for submitting AQI data from a grid worker.
 *
 * @param gridId The ID of the grid cell where data was recorded.
 * @param aqiValue The overall AQI value.
 * @param pm25 PM2.5 concentration.
 * @param pm10 PM10 concentration.
 * @param so2 SO2 concentration.
 * @param no2 NO2 concentration.
 * @param co CO concentration.
 * @param o3 O3 concentration.
 * @param primaryPollutant The main pollutant identified.
 */
public record AqiDataSubmissionRequest(
        @NotNull Long gridId,
        Integer aqiValue,
        Double pm25,
        Double pm10,
        Double so2,
        Double no2,
        Double co,
        Double o3,
        String primaryPollutant
) {
} 