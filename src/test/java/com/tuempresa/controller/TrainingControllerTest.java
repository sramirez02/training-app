package com.tuempresa.controller;

import com.tuempresa.dto.AddTrainingRequestDTO;
import com.tuempresa.service.TrainingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingControllerTest {

    @Mock
    private TrainingService trainingService;

    @InjectMocks
    private TrainingController trainingController;

    private AddTrainingRequestDTO validRequest;

    @BeforeEach
    void setUp() {
        validRequest = new AddTrainingRequestDTO();
        validRequest.setTraineeUsername("trainee01");
        validRequest.setTrainerUsername("trainer01");
        validRequest.setTrainingName("Cardio Workout");
        validRequest.setTrainingDate("2025-06-21");
        validRequest.setTrainingDuration(45);
    }

    @Test
    void addTraining_ValidRequest_ReturnsOk() {
        // Arrange
        doNothing().when(trainingService).addTraining(any(AddTrainingRequestDTO.class));

        // Act
        ResponseEntity<Void> response = trainingController.addTraining(validRequest);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        verify(trainingService, times(1)).addTraining(validRequest);
        verifyNoMoreInteractions(trainingService);
    }

    @Test
    void addTraining_CallsServiceWithCorrectData() {
        // Act
        trainingController.addTraining(validRequest);

        // Assert
        verify(trainingService).addTraining(validRequest);
    }
}
