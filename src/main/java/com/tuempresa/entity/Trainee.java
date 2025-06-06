package com.tuempresa.entity;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@AllArgsConstructor
@NoArgsConstructor
@Data
@Table
public class Trainee {
	@Id
	private Long id;
	private Long userId;
    private LocalDate dateOfBirth;
    private String address;
    
}