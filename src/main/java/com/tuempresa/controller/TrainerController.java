package com.tuempresa.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.tuempresa.service.TrainerService;

import lombok.RequiredArgsConstructor;

import com.tuempresa.dto.TrainerRequestDto;
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
	public User getTrainerByUsername(@RequestBody TrainerRequestDto trainerRequestDto) {
		User userToSave = new User();
		userToSave.setFirstName(trainerRequestDto.getFirstName());
		userToSave.setLastName(trainerRequestDto.getLastName());
		
		User savedUser = trainerService.createUserTrainer(userToSave, trainerRequestDto.getTrainingTypeId());
		
		User userToReturn = new User();
		userToReturn.setUsername(savedUser.getUsername());
		userToReturn.setPassword(savedUser.getPassword());
		
		return userToReturn;
	}
}
