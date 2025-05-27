package com.tuempresa.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.tuempresa.dto.CreateGymUserResponseDto;
import com.tuempresa.dto.CreateTrainerRequestDto;
import com.tuempresa.dto.TrainerProfileRequestDto;
import com.tuempresa.dto.TrainerProfileResponseDto;
import com.tuempresa.dto.TrainerTrainingResponseDTO;
import com.tuempresa.dto.TrainerTrainingsRequestDTO;
import com.tuempresa.dto.UpdateTrainerProfileResponseDto;
import com.tuempresa.dto.UpdateTrainerRequestDto;
import com.tuempresa.dto.UpdateTrainerStatusRequestDTO;
import com.tuempresa.entity.User;
import com.tuempresa.service.TrainerService;
import com.tuempresa.service.TrainingService;

@ExtendWith(MockitoExtension.class)
class TrainerControllerTest {

    @Mock
    private TrainerService trainerService;

    @Mock
    private TrainingService trainingService;

    @InjectMocks
    private TrainerController trainerController;

    private CreateTrainerRequestDto createRequest;
    private TrainerProfileRequestDto profileRequest;
    private UpdateTrainerRequestDto updateRequest;
    private TrainerTrainingsRequestDTO trainingsRequest;
    private UpdateTrainerStatusRequestDTO statusRequest;

    @BeforeEach
    void setUp() {
        createRequest = new CreateTrainerRequestDto("John", "Doe", 1L);
        profileRequest = new TrainerProfileRequestDto();
        updateRequest = new UpdateTrainerRequestDto();
        trainingsRequest = new TrainerTrainingsRequestDTO();
        statusRequest = new UpdateTrainerStatusRequestDTO();
    }

    
    @Test
    void getTrainerByUsername_ShouldReturnUser() {
        User expectedUser = new User("john.doe", "password", true);
        when(trainerService.getTrainerUserByUsernameUser("john.doe")).thenReturn(expectedUser);

        User result = trainerController.getTrainerByUsername("john.doe");

        assertEquals(expectedUser, result);
        verify(trainerService).getTrainerUserByUsernameUser("john.doe");
    }

    @Test
    void createTrainer_ShouldReturnResponseDto() {
        CreateGymUserResponseDto expectedResponse = new CreateGymUserResponseDto("john.doe", "password123");
        when(trainerService.createUserTrainer(createRequest)).thenReturn(expectedResponse);

        CreateGymUserResponseDto result = trainerController.createTrainer(createRequest);

        assertEquals(expectedResponse, result);
        verify(trainerService).createUserTrainer(createRequest);
    }

    @Test
    void getTrainerProfile_ShouldReturnProfile() {
        TrainerProfileResponseDto expectedProfile = buildSampleProfile();
        when(trainerService.getTrainerProfile("john.doe")).thenReturn(expectedProfile);

        ResponseEntity<TrainerProfileResponseDto> response = trainerController.getTrainerProfile(profileRequest);

        assertEquals(expectedProfile, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void updateTrainerProfile_ShouldReturnUpdatedProfile() {
        UpdateTrainerProfileResponseDto expectedResponse = buildUpdatedProfile();
        when(trainerService.updateTrainerProfile(updateRequest)).thenReturn(expectedResponse);

        ResponseEntity<UpdateTrainerProfileResponseDto> response = trainerController.updateTrainerProfile(updateRequest);

        assertEquals(expectedResponse, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
    }

   

    @Test
    void getTrainerTrainings_ShouldReturnTrainings() {
        List<TrainerTrainingResponseDTO> expectedTrainings = List.of(
            new TrainerTrainingResponseDTO()
        );
        when(trainingService.getTrainerTrainings(
            "john.doe", 
            trainingsRequest.getPeriodFrom(), 
            trainingsRequest.getPeriodTo(), 
            trainingsRequest.getTraineeName()))
            .thenReturn(expectedTrainings);

        List<TrainerTrainingResponseDTO> result = trainerController.getTrainerTrainings(trainingsRequest);

        assertEquals(expectedTrainings, result);
    }

    @Test
    void updateTrainerStatus_ShouldReturnOk() {
        doNothing().when(trainerService).toggleTrainerStatus("john.doe", true);

        ResponseEntity<Void> response = trainerController.updateTrainerStatus(statusRequest);

        assertEquals(200, response.getStatusCodeValue());
        verify(trainerService).toggleTrainerStatus("john.doe", true);
    }

    
    @Test
    void getTrainerProfile_ShouldThrowException_WhenUserNotFound() {
        when(trainerService.getTrainerProfile("unknown")).thenThrow(new RuntimeException("User not found"));
        
        assertThrows(RuntimeException.class, () -> {
            trainerController.getTrainerProfile(new TrainerProfileRequestDto());
        });
    }

    @Test
    void getTrainerTrainings_ShouldReturnEmptyList_WhenNoTrainingsFound() {
        when(trainingService.getTrainerTrainings(any(), any(), any(), any()))
            .thenReturn(Collections.emptyList());
        
        List<TrainerTrainingResponseDTO> result = trainerController.getTrainerTrainings(trainingsRequest);
        
        assertTrue(result.isEmpty());
    }

    @Test
    void updateTrainerStatus_ShouldThrowException_WhenRequestIsNull() {
        assertThrows(NullPointerException.class, () -> {
            trainerController.updateTrainerStatus(null);
        });
    }

    
    private TrainerProfileResponseDto buildSampleProfile() {
        return TrainerProfileResponseDto.builder()
            .firstName("John")
            .lastName("Doe")
            .isActive(true)
            .traineesList(Collections.emptyList())
            .build();
    }

    private UpdateTrainerProfileResponseDto buildUpdatedProfile() {
        return UpdateTrainerProfileResponseDto.builder()
            .username("john.doe")
            .firstName("John")
            .lastName("Doe")
            .isActive(true)
            .traineesList(Collections.emptyList())
            .build();
    }
}