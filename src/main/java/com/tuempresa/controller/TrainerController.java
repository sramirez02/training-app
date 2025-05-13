package com.tuempresa.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.tuempresa.service.TrainerService;

import lombok.RequiredArgsConstructor;

import com.tuempresa.dto.CreateGymUserResponseDto;
import com.tuempresa.dto.CreateTrainerRequestDto;
import com.tuempresa.entity.User;

@RestController
@RequestMapping("/trainer")
@RequiredArgsConstructor
public class TrainerController {

	private final TrainerService trainerService;

	@GetMapping("/username/{username}")
	public User getTrainerByUsername(@PathVariable String username) {
		return trainerService.getTrainerUserByUsernameUser(username);
	}

	@PostMapping("")
	public CreateGymUserResponseDto createTrainer(@RequestBody CreateTrainerRequestDto trainerRequestDto) {
		return trainerService.createUserTrainer(trainerRequestDto);
	}
}
