package com.tuempresa.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateTrainerStatusRequestDTO {

	@NotBlank
	private String username;

	@NotNull
	private Boolean isActive;

}
