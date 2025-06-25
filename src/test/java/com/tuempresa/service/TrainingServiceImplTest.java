package com.tuempresa.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    @Mock
    private TrainingDAO trainingDAO;

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainerService trainerService;

    @InjectMocks
    private TrainingServiceImpl trainingService;

    private Training testTraining;
    private Date testDate;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @BeforeEach
    void setUp() throws ParseException {
        testDate = dateFormat.parse("2023-01-01");
        testTraining = new Training();
        testTraining.setId(1L);
        testTraining.setTrainingName("Test Training");
        testTraining.setTrainingDate("2023-01-01");
        testTraining.setTrainingDuration(60);
        testTraining.setTraineeId(1L);
        testTraining.setTrainerId(1L);
        testTraining.setTrainingTypeId(1L);
    }

    @Test
    void getTrainingsByCriteria_ShouldReturnTrainings() {
        when(trainingDAO.findTrainingsByCriteria("trainee1", testDate, testDate, "trainer1", "1"))
            .thenReturn(Arrays.asList(testTraining));

        List<Training> result = trainingService.getTrainingsByCriteria(
            "trainee1", testDate, testDate, "trainer1", "1");

        assertEquals(1, result.size());
        assertEquals("Test Training", result.get(0).getTrainingName());
    }

    @Test
    void getTrainingsByTrainerCriteria_ShouldReturnTrainings() {
        when(trainingDAO.findTrainingsByTrainerCriteria("trainer1", testDate, testDate, "trainee1"))
            .thenReturn(Arrays.asList(testTraining));

        List<Training> result = trainingService.getTrainingsByTrainerCriteria(
            "trainer1", testDate, testDate, "trainee1");

        assertEquals(1, result.size());
        assertEquals("Test Training", result.get(0).getTrainingName());
    }

    @Test
    void addTraining_ShouldCallDAOMethod() {
        doNothing().when(trainingDAO).addTraining(testTraining);

        trainingService.addTraining(testTraining);

        verify(trainingDAO).addTraining(testTraining);
    }

    @Test
    void getTraineeTrainings_ShouldReturnDTOList() {
        when(trainingDAO.findTrainingsByCriteria("trainee1", testDate, testDate, "trainer1", "1"))
            .thenReturn(Arrays.asList(testTraining));

        List<TrainingResponseDTO> result = trainingService.getTraineeTrainings(
            "trainee1", testDate, testDate, "trainer1", "1");

        assertEquals(1, result.size());
        assertEquals("Test Training", result.get(0).getTrainingName());
        assertEquals("2023-01-01", result.get(0).getTrainingDate());
    }

    @Test
    void getTrainerTrainings_ShouldReturnDTOList() {
        when(trainingDAO.findTrainingsByTrainerCriteria("trainer1", testDate, testDate, "trainee1"))
            .thenReturn(Arrays.asList(testTraining));

        List<TrainerTrainingResponseDTO> result = trainingService.getTrainerTrainings(
            "trainer1", testDate, testDate, "trainee1");

        assertEquals(1, result.size());
        assertEquals("Test Training", result.get(0).getTrainingName());
        assertEquals("2023-01-01", result.get(0).getTrainingDate());
    }

    @Test
    void addTrainingWithDTO_ShouldCreateAndSaveTraining() {
        AddTrainingRequestDTO request = new AddTrainingRequestDTO();
        request.setTrainingName("New Training");
        request.setTrainingDate("2023-01-01");
        request.setTrainingDuration(45);
        request.setTraineeUsername("trainee1");
        request.setTrainerUsername("trainer1");

        when(traineeService.getTraineeIdByUsername("trainee1")).thenReturn(1L);
        when(trainerService.getTrainerIdByUsername("trainer1")).thenReturn(1L);
        doNothing().when(trainingDAO).addTraining(any(Training.class));

        trainingService.addTraining(request);

        verify(traineeService).getTraineeIdByUsername("trainee1");
        verify(trainerService).getTrainerIdByUsername("trainer1");
        verify(trainingDAO).addTraining(any(Training.class));
    }

    @Test
    void convertToDTO_ShouldMapCorrectly() {
        TrainingResponseDTO dto = trainingService.convertToDTO(testTraining);

        assertEquals("Test Training", dto.getTrainingName());
        assertEquals("2023-01-01", dto.getTrainingDate());
        assertEquals(60, dto.getTrainingDuration());
        assertEquals(1L, dto.getTrainingType());
        assertEquals(1L, dto.getTrainerName());
    }

    @Test
    void convertToTrainerDTO_ShouldMapCorrectly() {
        TrainerTrainingResponseDTO dto = trainingService.convertToTrainerDTO(testTraining);

        assertEquals("Test Training", dto.getTrainingName());
        assertEquals("2023-01-01", dto.getTrainingDate());
        assertEquals(60, dto.getTrainingDuration());
        assertEquals(1L, dto.getTrainingType());
        assertEquals("Test Training", dto.getTraineeName());
    }

    @Test
    void addTrainingWithDTO_ShouldHandleNullValues() {
        AddTrainingRequestDTO request = new AddTrainingRequestDTO();
        request.setTrainingName(null);
        request.setTrainingDate(null);
        request.setTrainingDuration(0);
        request.setTraineeUsername(null);
        request.setTrainerUsername(null);

        when(traineeService.getTraineeIdByUsername(null)).thenThrow(new RuntimeException());
        when(trainerService.getTrainerIdByUsername(null)).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> trainingService.addTraining(request));
    }
}