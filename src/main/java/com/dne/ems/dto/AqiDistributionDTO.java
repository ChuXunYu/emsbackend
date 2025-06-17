package com.dne.ems.dto;

import lombok.Data;

@Data
public class AqiDistributionDTO {
    private String level;
    private long count;

    // Default constructor for frameworks
    public AqiDistributionDTO() {
    }
    
    // Constructor used by JPQL query
    public AqiDistributionDTO(String level, Number count) {
        this.level = level;
        this.count = (count != null) ? count.longValue() : 0L;
    }
}