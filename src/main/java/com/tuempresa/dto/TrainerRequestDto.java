package com.tuempresa.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TrainerRequestDto {

	
	private String firstName;
	
    private String lastName;
    private String username;
    private String password;
    Long trainingTypeId;
}
