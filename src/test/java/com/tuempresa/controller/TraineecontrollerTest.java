package com.tuempresa.controller;

import com.tuempresa.dto.*;
import com.tuempresa.entity.User;
import com.tuempresa.service.TraineeService;
import com.tuempresa.service.TrainingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TraineeControllerTest {

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainingService trainingService;

    @InjectMocks
    private TraineeController traineeController;

    private CreateTraineeRequestDto createTraineeRequest;
    private CreateGymUserResponseDto createGymUserResponse;
    private TraineeProfileResponseDto traineeProfileResponse;
    private UpdateTraineeRequestDto updateTraineeRequest;
    private UpdateTraineeTrainersRequest updateTrainersRequest;
    private TraineeTrainingsRequestDTO trainingsRequest;
    private TrainingResponseDTO trainingResponse;
    private UpdateTraineeStatusRequestDTO statusRequest;
    private User user;

    @BeforeEach
    void setUp() {
        
        createTraineeRequest = new CreateTraineeRequestDto();
        createTraineeRequest.setFirstName("John");
        createTraineeRequest.setLastName("Doe");
        createTraineeRequest.setDateOfBirth(LocalDate.of(1990, 1, 1));
        createTraineeRequest.setAddress("123 Main St");

        createGymUserResponse = new CreateGymUserResponseDto();
        createGymUserResponse.setUsername("john.doe");
        createGymUserResponse.setPassword("password123");

        TrainerInfoDto trainerInfo = TrainerInfoDto.builder()
                .username("trainer1")
                .firstName("Trainer")
                .lastName("One")
                .build();

        traineeProfileResponse = TraineeProfileResponseDto.builder()
                .firstName("John")
                .lastName("Doe")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .address("123 Main St")
                .isActive(true)
                .trainersList(Arrays.asList(trainerInfo))
                .build();

        updateTraineeRequest = new UpdateTraineeRequestDto();
        updateTraineeRequest.setUsername("john.doe");
        updateTraineeRequest.setFirstName("John");
        updateTraineeRequest.setLastName("Doe Updated");
        updateTraineeRequest.setDateOfBirth(LocalDate.of(1990, 1, 1));
        updateTraineeRequest.setAddress("456 Oak St");
        updateTraineeRequest.setActive(true);

        updateTrainersRequest = new UpdateTraineeTrainersRequest();
        updateTrainersRequest.setTraineeUsername("john.doe");
        updateTrainersRequest.setTrainersUsernames(Arrays.asList("trainer1", "trainer2"));

        trainingsRequest = new TraineeTrainingsRequestDTO();
        trainingsRequest.setUsername("john.doe");
        trainingsRequest.setPeriodFrom(new Date());
        trainingsRequest.setPeriodTo(new Date());
        trainingsRequest.setTrainerName("trainer1");
        trainingsRequest.setTrainingType("Cardio");

        trainingResponse = new TrainingResponseDTO();
        trainingResponse.setTrainingName("Morning Session");
        trainingResponse.setTrainingDate("2023-01-01");
        trainingResponse.setTrainingDuration(60);

        statusRequest = new UpdateTraineeStatusRequestDTO();
        statusRequest.setUsername("john.doe");
        statusRequest.setIsActive(false);

        user = new User();
        user.setUsername("john.doe");
        user.setFirstName("John");
        user.setLastName("Doe");
    }

    @Test
    void getTraineeByUsername_ShouldReturnUser() {
        when(traineeService.getTraineeUserByUsernameUser("john.doe")).thenReturn(user);

        User result = traineeController.getTraineeByUsername("john.doe");

        assertNotNull(result);
        assertEquals("john.doe", result.getUsername());
        verify(traineeService).getTraineeUserByUsernameUser("john.doe");
    }

    @Test
    void createTrainee_ShouldReturnResponseDto() {
        when(traineeService.createUserTrainee(any(CreateTraineeRequestDto.class))).thenReturn(createGymUserResponse);

        CreateGymUserResponseDto result = traineeController.createTrainee(createTraineeRequest);

        assertNotNull(result);
        assertEquals("john.doe", result.getUsername());
        verify(traineeService).createUserTrainee(createTraineeRequest);
    }

    @Test
    void getTraineeProfile_ShouldReturnProfile() {
        when(traineeService.getTraineeProfile("john.doe")).thenReturn(traineeProfileResponse);

        ResponseEntity<TraineeProfileResponseDto> response = traineeController.getTraineeProfile("john.doe");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("John", response.getBody().getFirstName());
        verify(traineeService).getTraineeProfile("john.doe");
    }

    @Test
    void updateTraineeProfile_ShouldReturnUpdatedProfile() {
        when(traineeService.updateTraineeProfile(any(UpdateTraineeRequestDto.class)))
                .thenReturn(traineeProfileResponse);

        ResponseEntity<TraineeProfileResponseDto> response = traineeController.updateTraineeProfile(updateTraineeRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Doe", response.getBody().getLastName());
        verify(traineeService).updateTraineeProfile(updateTraineeRequest);
    }

    @Test
    void deleteTraineeProfile_ShouldReturnOk() {
        doNothing().when(traineeService).deleteTraineeByUsername("john.doe");

        ResponseEntity<Void> response = traineeController.deleteTraineeProfile("john.doe");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(traineeService).deleteTraineeByUsername("john.doe");
    }

    @Test
    void updateTraineeTrainers_ShouldReturnTrainersList() {
        List<TrainerInfoDto> trainers = Arrays.asList(
                TrainerInfoDto.builder().username("trainer1").firstName("Trainer").lastName("One").build(),
                TrainerInfoDto.builder().username("trainer2").firstName("Trainer").lastName("Two").build()
        );
        when(traineeService.updateTraineeTrainers(any(UpdateTraineeTrainersRequest.class))).thenReturn(trainers);

        ResponseEntity<List<TrainerInfoDto>> response = traineeController.updateTraineeTrainers(updateTrainersRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(traineeService).updateTraineeTrainers(updateTrainersRequest);
    }

    @Test
    void getTraineeTrainings_ShouldReturnTrainingsList() {
        List<TrainingResponseDTO> trainings = Arrays.asList(trainingResponse);
        when(trainingService.getTraineeTrainings(
                anyString(), any(), any(), anyString(), anyString()))
                .thenReturn(trainings);

        List<TrainingResponseDTO> result = traineeController.getTraineeTrainings(trainingsRequest);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Morning Session", result.get(0).getTrainingName());
        verify(trainingService).getTraineeTrainings(
                trainingsRequest.getUsername(),
                trainingsRequest.getPeriodFrom(),
                trainingsRequest.getPeriodTo(),
                trainingsRequest.getTrainerName(),
                trainingsRequest.getTrainingType());
    }

    @Test
    void updateTraineeStatus_ShouldReturnOk() {
        doNothing().when(traineeService).toggleTraineeStatus("john.doe", false);

        ResponseEntity<Void> response = traineeController.updateTraineeStatus(statusRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(traineeService).toggleTraineeStatus("john.doe", false);
    }
}