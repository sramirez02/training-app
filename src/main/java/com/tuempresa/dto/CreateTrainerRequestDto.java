package com.tuempresa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateTrainerRequestDto {

	private String firstName;
	private String lastName;
	Long trainingTypeId;
}
