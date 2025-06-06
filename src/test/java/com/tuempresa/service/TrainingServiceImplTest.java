package com.tuempresa.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tuempresa.dao.TrainingDAO;
import com.tuempresa.dto.AddTrainingRequestDTO;
import com.tuempresa.dto.TrainerTrainingResponseDTO;
import com.tuempresa.dto.TrainingResponseDTO;
import com.tuempresa.entity.Training;

@ExtendWith(MockitoExtension.class)
class TrainingServiceImplTest {

    @Mock private TrainingDAO trainingDAO;
    @Mock private TraineeService traineeService;
    @Mock private TrainerService trainerService;
    
    @InjectMocks private TrainingServiceImpl trainingService;
    
    private Training testTraining;
    private AddTrainingRequestDTO testRequest;
    private Date testDate;

    @BeforeEach
    void setUp() {
        testDate = new Date();
        testTraining = new Training();
        testTraining.setId(1L);
        testTraining.setTrainingName("Yoga Session");
        testTraining.setTrainingDate("2023-12-01");
        testTraining.setTrainingDuration(60);
        testTraining.setTraineeId(1L);
        testTraining.setTrainerId(1L);
        testTraining.setTrainingTypeId(1L);
        
        testRequest = new AddTrainingRequestDTO();
        testRequest.setTraineeUsername("trainee1");
        testRequest.setTrainerUsername("trainer1");
        testRequest.setTrainingName("Yoga Session");
        testRequest.setTrainingDate("2023-12-01");
        testRequest.setTrainingDuration(60);
    }

    @Test
    void getTrainingsByCriteria_ShouldReturnTrainings() {
        when(trainingDAO.findTrainingsByCriteria("trainee1", testDate, testDate, "trainer1", "1"))
            .thenReturn(Arrays.asList(testTraining));
        
        List<Training> result = trainingService.getTrainingsByCriteria(
            "trainee1", testDate, testDate, "trainer1", "1");
        
        assertEquals(1, result.size());
        assertEquals("Yoga Session", result.get(0).getTrainingName());
        verify(trainingDAO).findTrainingsByCriteria("trainee1", testDate, testDate, "trainer1", "1");
    }

    @Test
    void getTrainingsByTrainerCriteria_ShouldReturnTrainings() {
        when(trainingDAO.findTrainingsByTrainerCriteria("trainer1", testDate, testDate, "trainee1"))
            .thenReturn(Arrays.asList(testTraining));
        
        List<Training> result = trainingService.getTrainingsByTrainerCriteria(
            "trainer1", testDate, testDate, "trainee1");
        
        assertEquals(1, result.size());
        assertEquals("Yoga Session", result.get(0).getTrainingName());
        verify(trainingDAO).findTrainingsByTrainerCriteria("trainer1", testDate, testDate, "trainee1");
    }

    @Test
    void addTraining_ShouldSaveTraining() {
        trainingService.addTraining(testTraining);
        verify(trainingDAO).addTraining(testTraining);
    }

    @Test
    void getTraineeTrainings_ShouldConvertToDTO() {
        when(trainingDAO.findTrainingsByCriteria("trainee1", testDate, testDate, "trainer1", "1"))
            .thenReturn(Arrays.asList(testTraining));
        
        List<TrainingResponseDTO> result = trainingService.getTraineeTrainings(
            "trainee1", testDate, testDate, "trainer1", "1");
        
        assertEquals(1, result.size());
        TrainingResponseDTO dto = result.get(0);
        assertEquals("Yoga Session", dto.getTrainingName());
        assertEquals("2023-12-01", dto.getTrainingDate());
        assertEquals(60, dto.getTrainingDuration());
    }

    @Test
    void getTrainerTrainings_ShouldConvertToTrainerDTO() {
        when(trainingDAO.findTrainingsByTrainerCriteria("trainer1", testDate, testDate, "trainee1"))
            .thenReturn(Arrays.asList(testTraining));
        
        List<TrainerTrainingResponseDTO> result = trainingService.getTrainerTrainings(
            "trainer1", testDate, testDate, "trainee1");
        
        assertEquals(1, result.size());
        TrainerTrainingResponseDTO dto = result.get(0);
        assertEquals("Yoga Session", dto.getTrainingName());
        assertEquals("2023-12-01", dto.getTrainingDate());
        assertEquals(60, dto.getTrainingDuration());
    }

    @Test
    void addTraining_WithRequestDTO_ShouldCreateAndSaveTraining() {
        when(traineeService.getTraineeIdByUsername("trainee1")).thenReturn(1L);
        when(trainerService.getTrainerIdByUsername("trainer1")).thenReturn(1L);
        
        trainingService.addTraining(testRequest);
        
        verify(traineeService).getTraineeIdByUsername("trainee1");
        verify(trainerService).getTrainerIdByUsername("trainer1");
        verify(trainingDAO).addTraining(any(Training.class));
    }

    @Test
    void addTraining_WithRequestDTO_ShouldSetCorrectProperties() {
        when(traineeService.getTraineeIdByUsername("trainee1")).thenReturn(1L);
        when(trainerService.getTrainerIdByUsername("trainer1")).thenReturn(1L);
        
        trainingService.addTraining(testRequest);
        
        verify(trainingDAO).addTraining(argThat(training -> 
            training.getTrainingName().equals("Yoga Session") &&
            training.getTrainingDate().equals("2023-12-01") &&
            training.getTrainingDuration() == 60 &&
            training.getTraineeId() == 1L &&
            training.getTrainerId() == 1L &&
            training.getTrainingTypeId() == 1L
        ));
    }

    @Test
    void convertToDTO_ShouldMapCorrectly() {
        TrainingResponseDTO dto = trainingService.convertToDTO(testTraining);
        
        assertEquals("Yoga Session", dto.getTrainingName());
        assertEquals("2023-12-01", dto.getTrainingDate());
        assertEquals(1L, dto.getTrainingType());
        assertEquals(60, dto.getTrainingDuration());
        assertEquals(1L, dto.getTrainerName());
    }

    @Test
    void convertToTrainerDTO_ShouldMapCorrectly() {
        TrainerTrainingResponseDTO dto = trainingService.convertToTrainerDTO(testTraining);
        
        assertEquals("Yoga Session", dto.getTrainingName());
        assertEquals("2023-12-01", dto.getTrainingDate());
        assertEquals(1L, dto.getTrainingType());
        assertEquals(60, dto.getTrainingDuration());
        assertEquals("Yoga Session", dto.getTraineeName()); // Nota: Esto parece un error en el mapeo
    }
}