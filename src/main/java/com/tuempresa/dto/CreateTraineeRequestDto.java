package com.tuempresa.dto;


import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateTraineeRequestDto {

	private String firstName;
	private String lastName;
	private LocalDate dateOfBirth;
    private String address;
}
