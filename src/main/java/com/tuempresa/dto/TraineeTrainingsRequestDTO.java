package com.tuempresa.dto;

import java.util.Date;

import lombok.Data;

@Data
public class TraineeTrainingsRequestDTO {
	 private String username;
	    private Date periodFrom;
	    private Date periodTo;
	    private String trainerName;
	    private String trainingType;
}
