package com.tuempresa.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import com.tuempresa.dto.CreateGymUserResponseDto;
import com.tuempresa.dto.CreateTraineeRequestDto;
import com.tuempresa.dto.TraineeProfileResponseDto;
import com.tuempresa.dto.TraineeTrainingsRequestDTO;
import com.tuempresa.dto.TrainingResponseDTO;
import com.tuempresa.dto.UpdateTraineeRequestDto;
import com.tuempresa.dto.UpdateTraineeStatusRequestDTO;
import com.tuempresa.dto.UpdateTraineeTrainersRequest;
import com.tuempresa.entity.User;
import com.tuempresa.service.TraineeService;
import com.tuempresa.service.TrainingService;

@ExtendWith(MockitoExtension.class)
class TraineeControllerTest {

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainingService trainingService;

    @InjectMocks
    private TraineeController traineeController;

    private CreateTraineeRequestDto createRequest;
    private UpdateTraineeRequestDto updateRequest;
    private UpdateTraineeTrainersRequest updateTrainersRequest;
    private TraineeTrainingsRequestDTO trainingsRequest;
    private UpdateTraineeStatusRequestDTO statusRequest;

    @BeforeEach
    void setUp() {
       
        createRequest = new CreateTraineeRequestDto("John", "Doe", LocalDate.of(1990, 1, 1), "123 Main St");
        updateRequest = new UpdateTraineeRequestDto();
        updateTrainersRequest = new UpdateTraineeTrainersRequest();
        trainingsRequest = new TraineeTrainingsRequestDTO();
        statusRequest = new UpdateTraineeStatusRequestDTO();
    }

    
    @Test
    void getTraineeByUsername_ShouldReturnUser() {
        User expectedUser = new User("john.doe", "password", true);
        when(traineeService.getTraineeUserByUsernameUser("john.doe")).thenReturn(expectedUser);

        User result = traineeController.getTraineeByUsername("john.doe");

        assertEquals(expectedUser, result);
        verify(traineeService).getTraineeUserByUsernameUser("john.doe");
    }

    @Test
    void createTrainee_ShouldReturnResponseDto() {
        CreateGymUserResponseDto expectedResponse = new CreateGymUserResponseDto("john.doe", "password123");
        when(traineeService.createUserTrainee(createRequest)).thenReturn(expectedResponse);

        CreateGymUserResponseDto result = traineeController.createTrainee(createRequest);

        assertEquals(expectedResponse, result);
        verify(traineeService).createUserTrainee(createRequest);
    }

    @Test
    void getTraineeProfile_ShouldReturnProfile() {
        TraineeProfileResponseDto expectedProfile = buildSampleProfile();
        when(traineeService.getTraineeProfile("john.doe")).thenReturn(expectedProfile);

        ResponseEntity<TraineeProfileResponseDto> response = traineeController.getTraineeProfile("john.doe");

        assertEquals(expectedProfile, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
    }

    
    private TraineeProfileResponseDto buildSampleProfile() {
	
		return null;
	}


	@Test
    void getTraineeProfile_ShouldThrowException_WhenUserNotFound() {
        when(traineeService.getTraineeProfile("unknown")).thenThrow(new RuntimeException("User not found"));
        
        assertThrows(RuntimeException.class, () -> {
            traineeController.getTraineeProfile("unknown");
        });
    }

    @Test
    void getTraineeTrainings_ShouldReturnEmptyList_WhenNoTrainingsFound() {
        when(trainingService.getTraineeTrainings(any(), any(), any(), any(), any()))
            .thenReturn(Collections.emptyList());
        
        List<TrainingResponseDTO> result = traineeController.getTraineeTrainings(trainingsRequest);
        
        assertTrue(result.isEmpty());
    }

    @Test
    void updateTraineeStatus_ShouldThrowException_WhenRequestIsNull() {
        assertThrows(NullPointerException.class, () -> {
            traineeController.updateTraineeStatus(null);
        });
    }

    
    @Test
    void updateTraineeTrainers_ShouldThrowException_WhenRequestInvalid() {
        UpdateTraineeTrainersRequest invalidRequest = new UpdateTraineeTrainersRequest();
        
        assertThrows(ResponseStatusException.class, () -> {
            traineeController.updateTraineeTrainers(invalidRequest);
        });
    }

    
    @Test
    void deleteTraineeProfile_ShouldReturnNoContent_WhenSuccessful() {
        doNothing().when(traineeService).deleteTraineeByUsername("john.doe");

        ResponseEntity<Void> response = traineeController.deleteTraineeProfile("john.doe");

        assertEquals(200, response.getStatusCodeValue());
    }


}