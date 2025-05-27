package com.tuempresa.dto;

import lombok.Data;

@Data
public class AddTrainingRequestDTO {
	
	 private String traineeUsername;  
	    private String trainerUsername;  
	    private String trainingName;     
	    private String trainingDate;     
	    private int trainingDuration;

}
