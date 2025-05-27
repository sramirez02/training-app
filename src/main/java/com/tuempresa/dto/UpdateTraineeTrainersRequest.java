package com.tuempresa.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UpdateTraineeTrainersRequest {

	@NotBlank
    private String traineeUsername;
    
    @NotEmpty
    private List<@NotBlank String> trainersUsernames;
    
}
