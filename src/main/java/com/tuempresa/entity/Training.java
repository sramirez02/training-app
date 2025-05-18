package com.tuempresa.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table
public class Training {
	@Id
	private long id;
	private String trainingName;
	private String trainingDate;
	private int trainingDuration;

	private long traineeId;
	private long trainerId;
	private long trainingTypeId;

}
