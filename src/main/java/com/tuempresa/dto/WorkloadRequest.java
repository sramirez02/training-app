package com.tuempresa.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class WorkloadRequest {
    private String trainerUsername;
    private String trainerFirstName;
    private String trainerLastName;
    private boolean isActive;
    private LocalDate trainingDate;
    private int trainingDuration;
    private String actionType;
    
}
