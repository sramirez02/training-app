package com.tuempresa.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tuempresa.dto.CreateGymUserResponseDto;
import com.tuempresa.dto.CreateTrainerRequestDto;
import com.tuempresa.dto.TraineeUsernameRequest;
import com.tuempresa.dto.TrainerProfileRequestDto;
import com.tuempresa.dto.TrainerProfileResponseDto;
import com.tuempresa.dto.UnassignedTrainerDto;
import com.tuempresa.dto.UpdateTrainerProfileResponseDto;
import com.tuempresa.dto.UpdateTrainerRequestDto;
import com.tuempresa.entity.User;
import com.tuempresa.service.TrainerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

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
	


	@PostMapping("/profile") 
    public ResponseEntity<TrainerProfileResponseDto> getTrainerProfile(
            @RequestBody TrainerProfileRequestDto request) {
        
        TrainerProfileResponseDto response = trainerService.getTrainerProfile(request.getUsername());
        return ResponseEntity.ok(response);
    }
	
	@PutMapping("/profile")
	
	 public ResponseEntity<UpdateTrainerProfileResponseDto> updateTrainerProfile(
	            @Valid @RequestBody UpdateTrainerRequestDto request) {

	        UpdateTrainerProfileResponseDto response = trainerService.updateTrainerProfile(request);
	        return ResponseEntity.ok(response);
	    }
	   
	    
	    @GetMapping("/unassigned/{username}")
	    public ResponseEntity<List<UnassignedTrainerDto>> getUnassignedActiveTrainers(
	            @PathVariable String username) {
	        List<UnassignedTrainerDto> response = trainerService.getUnassignedActiveTrainers(username);
	        return ResponseEntity.ok(response);
	    
	    
	    
	}
	
//	Antiguo para get trainer profile
//	   @GetMapping("/profile")
//	    public ResponseEntity<TrainerProfileResponseDto> getTrainerProfile(
//	            @RequestParam("username") String username) {
//	        
//	        TrainerProfileResponseDto response = trainerService.getTrainerProfile(username);
//	        return ResponseEntity.ok(response);
//	    }
}
