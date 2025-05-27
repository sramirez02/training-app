package com.tuempresa.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
    private AddTrainingRequestDTO invalidRequest;

    @BeforeEach
    void setUp() {
        validRequest = new AddTrainingRequestDTO();
        validRequest.setTrainingName("Yoga Session");
        validRequest.setTrainingDate("2023-06-15");
        validRequest.setTrainingDuration(60);
        validRequest.setTraineeUsername("trainee1");
        validRequest.setTrainerUsername("trainer1");

        invalidRequest = new AddTrainingRequestDTO();

    }

    @Test
    void addTraining_ShouldReturnOk_WhenRequestIsValid() {
        
        ResponseEntity<Void> response = trainingController.addTraining(validRequest);

        
        assertEquals(200, response.getStatusCodeValue());
        verify(trainingService).addTraining(validRequest);
    }

    @Test
    void addTraining_ShouldCallServiceWithCorrectParameters() {
        
        trainingController.addTraining(validRequest);

        
        verify(trainingService).addTraining(validRequest);
    }

    @Test
    void addTraining_ShouldHandleZeroDuration() {
        AddTrainingRequestDTO request = new AddTrainingRequestDTO();
        request.setTrainingDuration(0);
       

        ResponseEntity<Void> response = trainingController.addTraining(request);

        assertEquals(200, response.getStatusCodeValue());
    }
   
}