package com.tuempresa.dto;

import java.util.List;

import com.tuempresa.entity.TrainingType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateTrainerProfileResponseDto {
	
	private String username;
    private String firstName;
    private String lastName;
    private TrainingType specialization;
    private Boolean isActive;
    private List<TraineeInfoDto> traineesList;


}
