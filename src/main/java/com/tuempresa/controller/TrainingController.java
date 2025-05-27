package com.tuempresa.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tuempresa.dto.AddTrainingRequestDTO;
import com.tuempresa.service.TrainingService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/training")
@RequiredArgsConstructor
public class TrainingController {

	
	 private final TrainingService trainingService;

	    @PostMapping("")
	    public ResponseEntity<Void> addTraining(@Valid @RequestBody AddTrainingRequestDTO request) {
	        trainingService.addTraining(request);
	        return ResponseEntity.ok().build();
	    }
}
