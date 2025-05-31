package com.tuempresa.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tuempresa.dto.CreateGymUserResponseDto;
import com.tuempresa.dto.CreateTraineeRequestDto;
import com.tuempresa.dto.TraineeProfileResponseDto;
import com.tuempresa.dto.TraineeTrainingsRequestDTO;
import com.tuempresa.dto.TrainerInfoDto;
import com.tuempresa.dto.TrainingResponseDTO;
import com.tuempresa.dto.UpdateTraineeRequestDto;
import com.tuempresa.dto.UpdateTraineeStatusRequestDTO;
import com.tuempresa.dto.UpdateTraineeTrainersRequest;
import com.tuempresa.entity.User;
import com.tuempresa.service.TraineeService;
import com.tuempresa.service.TrainingService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/trainee")
@RequiredArgsConstructor
public class TraineeController {
	private final TraineeService traineeService;

	
	@Autowired
    private TrainingService trainingService;
	

	@GetMapping("/username/{username}")
	public User getTraineeByUsername(@PathVariable String username) {
		return traineeService.getTraineeUserByUsernameUser(username);
	}

	@PostMapping("/create-trainee")
	public CreateGymUserResponseDto createTrainee(@RequestBody CreateTraineeRequestDto traineeRequestDto) {
		return traineeService.createUserTrainee(traineeRequestDto);

	}

	@GetMapping("/profile")
	public ResponseEntity<TraineeProfileResponseDto> getTraineeProfile(@RequestParam("username") String username) {
		
		TraineeProfileResponseDto response = traineeService.getTraineeProfile(username);
		return ResponseEntity.ok(response);
	}
	
	@PutMapping("/profile")
    public ResponseEntity<TraineeProfileResponseDto> updateTraineeProfile(
            @Valid @RequestBody UpdateTraineeRequestDto request) {

        TraineeProfileResponseDto updatedProfile = traineeService.updateTraineeProfile(request);
        return ResponseEntity.ok(updatedProfile);
    }
	

	@DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteTraineeProfile(@PathVariable String username) {
        traineeService.deleteTraineeByUsername(username);
        return ResponseEntity.ok().build();
    }
	
	@PutMapping("/update-trainer")
	@ResponseBody
	public ResponseEntity<List<TrainerInfoDto>> updateTraineeTrainers(
	        @Valid @RequestBody UpdateTraineeTrainersRequest request) {
	    List<TrainerInfoDto> response = traineeService.updateTraineeTrainers(request);
	    return ResponseEntity.ok(response);
	}
	
	 @GetMapping("/trainings")
	    public List<TrainingResponseDTO> getTraineeTrainings(
	            @RequestBody TraineeTrainingsRequestDTO request) {
	        
	        return trainingService.getTraineeTrainings(
	            request.getUsername(),
	            request.getPeriodFrom(),
	            request.getPeriodTo(),
	            request.getTrainerName(),
	            request.getTrainingType()
	        );
	    }
	 
	 @PatchMapping("/status")
	 public ResponseEntity<Void> updateTraineeStatus(
	         @Valid @RequestBody UpdateTraineeStatusRequestDTO request) {
	     traineeService.toggleTraineeStatus(request.getUsername(), request.getIsActive());
	     return ResponseEntity.ok().build();
	 }
	
}
