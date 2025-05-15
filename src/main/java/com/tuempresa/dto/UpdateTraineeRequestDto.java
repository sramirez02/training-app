package com.tuempresa.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateTraineeRequestDto {
	
	
	@NotBlank
	private String username;
	@NotBlank
	private String firstName;
	@NotBlank
	private String lastName; 
	
    private LocalDate dateOfBirth; 
    private String address;
    
    private boolean isActive;
	
	
}
