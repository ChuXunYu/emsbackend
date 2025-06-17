package com.dne.ems.dto;

/**
 * DTO for representing a single data point in a time-series trend chart.
 */
public class TrendDataPointDTO {
    /**
     * The month identifier, typically in "YYYY-MM" format.
     */
    private String yearMonth;
    /**
     * The value for that month (e.g., number of exceedances).
     */
    private Long count;
    
    /**
     * Default constructor required by JPA.
     */
    public TrendDataPointDTO() {
    }
    
    /**
     * Constructor for JPQL queries.
     *
     * @param yearMonth The month identifier in "YYYY-MM" format
     * @param count The count value for that month
     */
    public TrendDataPointDTO(String yearMonth, Long count) {
        this.yearMonth = yearMonth;
        this.count = count;
    }
    
    // Getters and setters
    public String getYearMonth() {
        return yearMonth;
    }
    
    public void setYearMonth(String yearMonth) {
        this.yearMonth = yearMonth;
    }
    
    public Long getCount() {
        return count;
    }
    
    public void setCount(Long count) {
        this.count = count;
    }
    
    @Override
    public String toString() {
        return "TrendDataPointDTO{" +
                "yearMonth='" + yearMonth + '\'' +
                ", count=" + count +
                '}';
    }
} 