package com.tuempresa.dto;

import java.sql.Date;

import lombok.Data;

@Data
public class TrainerTrainingsRequestDTO {

	private String username;       
    private Date periodFrom;     
    private Date periodTo;        
    private String traineeName;
	
}
