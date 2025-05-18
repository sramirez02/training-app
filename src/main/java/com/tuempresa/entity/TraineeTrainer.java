package com.tuempresa.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table("trainee_trainer")
public class TraineeTrainer {
	@Id
	private Long id;
	private Long traineeId;
	private Long trainerId; 
}
