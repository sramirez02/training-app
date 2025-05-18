package com.tuempresa.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateTrainerRequestDto {

	@NotBlank
	private String username;
	@NotBlank
	private String firstName;
	@NotBlank
	private String lastName; 
	    
    private boolean isActive;
	
}
