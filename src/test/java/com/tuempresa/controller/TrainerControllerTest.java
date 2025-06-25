package com.tuempresa.controller;

import com.tuempresa.dto.*;
import com.tuempresa.entity.User;
import com.tuempresa.service.TrainerService;
import com.tuempresa.service.TrainingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TrainerControllerTest {

    @Mock
    private TrainerService trainerService;

    @Mock
    private TrainingService trainingService;

    @InjectMocks
    private TrainerController trainerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetTrainerByUsername() {
        User mockUser = new User("John", "Doe", true);
        when(trainerService.getTrainerUserByUsernameUser("jdoe")).thenReturn(mockUser);

        User response = trainerController.getTrainerByUsername("jdoe");
        assertEquals("John", response.getFirstName());
        verify(trainerService).getTrainerUserByUsernameUser("jdoe");
    }

    @Test
    void testCreateTrainer() {
        CreateTrainerRequestDto request = new CreateTrainerRequestDto("Jane", "Smith", 1L);
        CreateGymUserResponseDto responseDto = new CreateGymUserResponseDto("jane.smith", "password");

        when(trainerService.createUserTrainer(request)).thenReturn(responseDto);

        CreateGymUserResponseDto response = trainerController.createTrainer(request);
        assertEquals("jane.smith", response.getUsername());
        verify(trainerService).createUserTrainer(request);
    }

    @Test
    void testGetTrainerProfile() {
        TrainerProfileRequestDto request = new TrainerProfileRequestDto();
        request.setUsername("trainer1");
        TrainerProfileResponseDto mockResponse = TrainerProfileResponseDto.builder()
                .firstName("Trainer")
                .lastName("One")
                .isActive(true)
                .traineesList(List.of())
                .build();

        when(trainerService.getTrainerProfile("trainer1")).thenReturn(mockResponse);

        ResponseEntity<TrainerProfileResponseDto> response = trainerController.getTrainerProfile(request);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals("Trainer", response.getBody().getFirstName());
    }

    @Test
    void testUpdateTrainerProfile() {
        UpdateTrainerRequestDto request = new UpdateTrainerRequestDto();
        request.setUsername("trainer1");
        request.setFirstName("Updated");
        request.setLastName("Name");
        request.setActive(true);

        UpdateTrainerProfileResponseDto mockResponse = UpdateTrainerProfileResponseDto.builder()
                .username("trainer1")
                .firstName("Updated")
                .lastName("Name")
                .isActive(true)
                .traineesList(List.of())
                .build();

        when(trainerService.updateTrainerProfile(request)).thenReturn(mockResponse);

        ResponseEntity<UpdateTrainerProfileResponseDto> response = trainerController.updateTrainerProfile(request);
        assertEquals("Updated", response.getBody().getFirstName());
    }

    @Test
    void testGetUnassignedActiveTrainers() {
        String traineeUsername = "trainee1";
        UnassignedTrainerDto dto = UnassignedTrainerDto.builder()
                .username("trainerX")
                .firstName("X")
                .lastName("Y")
                .build();

        when(trainerService.getUnassignedActiveTrainers(traineeUsername)).thenReturn(List.of(dto));

        ResponseEntity<List<UnassignedTrainerDto>> response = trainerController.getUnassignedActiveTrainers(traineeUsername);
        assertEquals(1, response.getBody().size());
        verify(trainerService).getUnassignedActiveTrainers(traineeUsername);
    }

    @Test
    void testGetTrainerTrainings() {
        TrainerTrainingsRequestDTO request = new TrainerTrainingsRequestDTO();
        request.setUsername("trainer1");
        request.setPeriodFrom(Date.valueOf("2024-01-01"));
        request.setPeriodTo(Date.valueOf("2024-12-31"));
        request.setTraineeName("trainee1");

        TrainerTrainingResponseDTO dto = new TrainerTrainingResponseDTO();
        dto.setTrainingName("Training A");

        when(trainingService.getTrainerTrainings("trainer1", request.getPeriodFrom(),
                request.getPeriodTo(), "trainee1")).thenReturn(List.of(dto));

        List<TrainerTrainingResponseDTO> response = trainerController.getTrainerTrainings(request);
        assertEquals(1, response.size());
        assertEquals("Training A", response.get(0).getTrainingName());
    }

    @Test
    void testUpdateTrainerStatus() {
        UpdateTrainerStatusRequestDTO request = new UpdateTrainerStatusRequestDTO();
        request.setUsername("trainer1");
        request.setIsActive(true);

        ResponseEntity<Void> response = trainerController.updateTrainerStatus(request);
        verify(trainerService).toggleTrainerStatus("trainer1", true);
        assertEquals(200, response.getStatusCodeValue());
    }
}
