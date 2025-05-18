package com.tuempresa.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TraineeUsernameRequest {

	@NotBlank
    private String username;
}
