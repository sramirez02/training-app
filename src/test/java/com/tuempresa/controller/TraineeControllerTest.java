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
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TraineeControllerTest {

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainingService trainingService;

    @InjectMocks
    private TraineeController traineeController;

    private User sampleUser;

    @BeforeEach
    void setup() {
        sampleUser = new User("Ana", "Smith", true);
        sampleUser.setUsername("asmith");
    }

    @Test
    void testGetTraineeByUsername() {
        when(traineeService.getTraineeUserByUsernameUser("asmith")).thenReturn(sampleUser);

        User result = traineeController.getTraineeByUsername("asmith");

        assertEquals("asmith", result.getUsername());
        verify(traineeService).getTraineeUserByUsernameUser("asmith");
    }

    @Test
    void testCreateTrainee() {
        CreateTraineeRequestDto request = new CreateTraineeRequestDto("Ana", "Smith", LocalDate.of(1995, 5, 10), "123 Street");
        CreateGymUserResponseDto response = new CreateGymUserResponseDto("ana.smith", "pass123");

        when(traineeService.createUserTrainee(request)).thenReturn(response);

        CreateGymUserResponseDto result = traineeController.createTrainee(request);

        assertEquals("ana.smith", result.getUsername());
    }

    @Test
    void testGetTraineeProfile() {
        TraineeProfileResponseDto profile = TraineeProfileResponseDto.builder()
                .firstName("Ana")
                .lastName("Smith")
                .dateOfBirth(LocalDate.of(1995, 5, 10))
                .address("123 Street")
                .isActive(true)
                .trainersList(Collections.emptyList())
                .build();

        when(traineeService.getTraineeProfile("asmith")).thenReturn(profile);

        ResponseEntity<TraineeProfileResponseDto> response = traineeController.getTraineeProfile("asmith");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Ana", response.getBody().getFirstName());
    }

    @Test
    void testUpdateTraineeProfile() {
        UpdateTraineeRequestDto updateRequest = new UpdateTraineeRequestDto();
        updateRequest.setUsername("asmith");
        updateRequest.setFirstName("Ana");
        updateRequest.setLastName("Smith");
        updateRequest.setActive(true);

        TraineeProfileResponseDto updatedProfile = TraineeProfileResponseDto.builder()
                .firstName("Ana")
                .lastName("Smith")
                .isActive(true)
                .build();

        when(traineeService.updateTraineeProfile(updateRequest)).thenReturn(updatedProfile);

        ResponseEntity<TraineeProfileResponseDto> response = traineeController.updateTraineeProfile(updateRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isActive());
    }

    @Test
    void testDeleteTraineeProfile() {
        doNothing().when(traineeService).deleteTraineeByUsername("asmith");

        ResponseEntity<Void> response = traineeController.deleteTraineeProfile("asmith");

        assertEquals(200, response.getStatusCodeValue());
        verify(traineeService).deleteTraineeByUsername("asmith");
    }

    @Test
    void testUpdateTraineeTrainers() {
        UpdateTraineeTrainersRequest request = new UpdateTraineeTrainersRequest();
        request.setTraineeUsername("asmith");
        request.setTrainersUsernames(List.of("trainer1"));

        TrainerInfoDto trainerInfo = TrainerInfoDto.builder()
                .username("trainer1")
                .firstName("John")
                .lastName("Doe")
                .build();

        when(traineeService.updateTraineeTrainers(request)).thenReturn(List.of(trainerInfo));

        ResponseEntity<List<TrainerInfoDto>> response = traineeController.updateTraineeTrainers(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testGetTraineeTrainings() {
        TraineeTrainingsRequestDTO request = new TraineeTrainingsRequestDTO();
        request.setUsername("asmith");
        request.setPeriodFrom(new Date());
        request.setPeriodTo(new Date());
        request.setTrainerName("trainer1");
        request.setTrainingType("Yoga");

        TrainingResponseDTO training = new TrainingResponseDTO();
        training.setTrainingName("Yoga Morning");

        when(trainingService.getTraineeTrainings(
                any(), any(), any(), any(), any())).thenReturn(List.of(training));

        List<TrainingResponseDTO> trainings = traineeController.getTraineeTrainings(request);

        assertEquals(1, trainings.size());
        assertEquals("Yoga Morning", trainings.get(0).getTrainingName());
    }

    @Test
    void testUpdateTraineeStatus() {
        UpdateTraineeStatusRequestDTO request = new UpdateTraineeStatusRequestDTO();
        request.setUsername("asmith");
        request.setIsActive(true);

        doNothing().when(traineeService).toggleTraineeStatus("asmith", true);

        ResponseEntity<Void> response = traineeController.updateTraineeStatus(request);

        assertEquals(200, response.getStatusCodeValue());
        verify(traineeService).toggleTraineeStatus("asmith", true);
    }
}
