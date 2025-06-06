package com.tuempresa.controller;

import com.tuempresa.dto.*;
import com.tuempresa.entity.TrainingType;
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
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainerControllerTest {

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
        User user = new User("John", "Doe", true);
        user.setUsername("john.doe");

        when(trainerService.getTrainerUserByUsernameUser("john.doe")).thenReturn(user);

        User result = trainerController.getTrainerByUsername("john.doe");

        assertEquals("john.doe", result.getUsername());
        verify(trainerService).getTrainerUserByUsernameUser("john.doe");
    }

    @Test
    void testCreateTrainer() {
        CreateTrainerRequestDto request = new CreateTrainerRequestDto("Ana", "Smith", 1L);
        CreateGymUserResponseDto response = new CreateGymUserResponseDto("ana.smith", "pass123");

        when(trainerService.createUserTrainer(request)).thenReturn(response);

        CreateGymUserResponseDto result = trainerController.createTrainer(request);

        assertEquals("ana.smith", result.getUsername());
        verify(trainerService).createUserTrainer(request);
    }

    @SuppressWarnings("deprecation")
	@Test
    void testGetTrainerProfile() {
        TrainerProfileRequestDto request = new TrainerProfileRequestDto();
        request.setUsername("trainer1");

        TrainerProfileResponseDto response = TrainerProfileResponseDto.builder()
                .firstName("Carlos")
                .lastName("López")
                .specialization(new TrainingType())
                .isActive(true)
                .traineesList(Collections.emptyList())
                .build();

        when(trainerService.getTrainerProfile("trainer1")).thenReturn(response);

        ResponseEntity<TrainerProfileResponseDto> result = trainerController.getTrainerProfile(request);

        assertEquals(200, result.getStatusCodeValue());
        assertEquals("Carlos", result.getBody().getFirstName());
        verify(trainerService).getTrainerProfile("trainer1");
    }

    @SuppressWarnings("deprecation")
	@Test
    void testUpdateTrainerProfile() {
        UpdateTrainerRequestDto request = new UpdateTrainerRequestDto();
        request.setUsername("trainer1");
        request.setFirstName("Mario");
        request.setLastName("Gómez");
        request.setActive(true);

        UpdateTrainerProfileResponseDto response = UpdateTrainerProfileResponseDto.builder()
                .username("trainer1")
                .firstName("Mario")
                .lastName("Gómez")
                .isActive(true)
                .specialization(new TrainingType())
                .traineesList(Collections.emptyList())
                .build();

        when(trainerService.updateTrainerProfile(request)).thenReturn(response);

        ResponseEntity<UpdateTrainerProfileResponseDto> result = trainerController.updateTrainerProfile(request);

        assertEquals(200, result.getStatusCodeValue());
        assertEquals("Mario", result.getBody().getFirstName());
        verify(trainerService).updateTrainerProfile(request);
    }

    @SuppressWarnings("deprecation")
	@Test
    void testGetUnassignedActiveTrainers() {
        UnassignedTrainerDto dto = UnassignedTrainerDto.builder()
                .username("trainerX")
                .firstName("Ximena")
                .lastName("Martínez")
                .specialization(new TrainingType())
                .build();

        when(trainerService.getUnassignedActiveTrainers("admin1")).thenReturn(List.of(dto));

        ResponseEntity<List<UnassignedTrainerDto>> result = trainerController.getUnassignedActiveTrainers("admin1");

        assertEquals(200, result.getStatusCodeValue());
        assertEquals(1, result.getBody().size());
        verify(trainerService).getUnassignedActiveTrainers("admin1");
    }

    @Test
    void testGetTrainerTrainings() {
        TrainerTrainingsRequestDTO request = new TrainerTrainingsRequestDTO();
        request.setUsername("trainer1");
        request.setPeriodFrom(Date.valueOf("2023-01-01"));
        request.setPeriodTo(Date.valueOf("2023-12-31"));
        request.setTraineeName("Juan");

        TrainerTrainingResponseDTO dto = new TrainerTrainingResponseDTO();
        dto.setTrainingName("Cardio");
        dto.setTraineeName("Juan");

        when(trainingService.getTrainerTrainings("trainer1",
                request.getPeriodFrom(),
                request.getPeriodTo(),
                "Juan")).thenReturn(List.of(dto));

        List<TrainerTrainingResponseDTO> result = trainerController.getTrainerTrainings(request);

        assertEquals(1, result.size());
        assertEquals("Cardio", result.get(0).getTrainingName());
        verify(trainingService).getTrainerTrainings("trainer1",
                request.getPeriodFrom(),
                request.getPeriodTo(),
                "Juan");
    }

    @SuppressWarnings("deprecation")
	@Test
    void testUpdateTrainerStatus() {
        UpdateTrainerStatusRequestDTO request = new UpdateTrainerStatusRequestDTO();
        request.setUsername("trainer2");
        request.setIsActive(false);

        doNothing().when(trainerService).toggleTrainerStatus("trainer2", false);

        ResponseEntity<Void> response = trainerController.updateTrainerStatus(request);

        assertEquals(200, response.getStatusCodeValue());
        verify(trainerService).toggleTrainerStatus("trainer2", false);
    }
}
