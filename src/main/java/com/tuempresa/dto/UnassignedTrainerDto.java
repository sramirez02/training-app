package com.tuempresa.dto;

import com.tuempresa.entity.TrainingType;

import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class UnassignedTrainerDto {
	
	private String username;
    private String firstName;
    private String lastName;
    private TrainingType specialization;

}
