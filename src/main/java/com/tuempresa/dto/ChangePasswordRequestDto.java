package com.tuempresa.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangePasswordRequestDto {
	@NotNull
	@NotBlank
    private String username;

	@NotNull
	@NotBlank
    private String currentPassword;

	@NotNull
	@NotBlank
    private String newPassword;
}

