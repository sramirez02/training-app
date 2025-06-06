package com.tuempresa.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.tuempresa.dto.AddTrainingRequestDTO;
import com.tuempresa.service.TrainingService;

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
        validRequest.setTraineeUsername("trainee.user");
        validRequest.setTrainerUsername("trainer.user");
        validRequest.setTrainingName("Morning Session");
        validRequest.setTrainingDate("2023-01-01");
        validRequest.setTrainingDuration(60);
    }

    @SuppressWarnings("deprecation")
	@Test
    void addTraining_WithValidRequest_ShouldReturnOk() {
        
        doNothing().when(trainingService).addTraining(any(AddTrainingRequestDTO.class));

        
        ResponseEntity<Void> response = trainingController.addTraining(validRequest);

        
        assertEquals(200, response.getStatusCodeValue());
        verify(trainingService).addTraining(validRequest);
        verifyNoMoreInteractions(trainingService);
    }

    @Test
    void addTraining_ShouldCallServiceWithCorrectParameters() {
        
        doNothing().when(trainingService).addTraining(any(AddTrainingRequestDTO.class));

        
        trainingController.addTraining(validRequest);

        
        verify(trainingService).addTraining(validRequest);
    }

    @SuppressWarnings("deprecation")
	@Test
    void addTraining_ShouldReturnCorrectResponseStatus() {
        
        doNothing().when(trainingService).addTraining(any(AddTrainingRequestDTO.class));

        
        ResponseEntity<Void> response = trainingController.addTraining(validRequest);

        
        assertEquals(200, response.getStatusCodeValue());
    }
}