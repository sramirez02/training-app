package com.tuempresa.service;

import com.tuempresa.dao.TrainingDAO;
import com.tuempresa.entity.Training;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainingServiceImplTest {

    @Mock
    private TrainingDAO trainingDAO;

    @InjectMocks
    private TrainingServiceImpl trainingService;

    private Training training;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        training = new Training();
        training.setId(1L);
        training.setTrainingName("Java Basics");
        training.setTrainingDate("2025-04-21");
        training.setTrainingDuration(90);
        training.setTraineeId(10L);
        training.setTrainerId(20L);
        training.setTrainingTypeId(5L);
    }

    @Test
    void testGetTrainingsByCriteria() {
        Date fromDate = new GregorianCalendar(2025, Calendar.APRIL, 1).getTime();
        Date toDate = new GregorianCalendar(2025, Calendar.APRIL, 30).getTime();
        List<Training> trainings = List.of(training);

        when(trainingDAO.findTrainingsByCriteria("traineeUser", fromDate, toDate, "trainerName", "Tech"))
                .thenReturn(trainings);

        List<Training> result = trainingService.getTrainingsByCriteria("traineeUser", fromDate, toDate, "trainerName", "Tech");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Java Basics", result.get(0).getTrainingName());
    }

    @Test
    void testGetTrainingsByTrainerCriteria() {
        Date fromDate = new GregorianCalendar(2025, Calendar.APRIL, 1).getTime();
        Date toDate = new GregorianCalendar(2025, Calendar.APRIL, 30).getTime();
        List<Training> trainings = List.of(training);

        when(trainingDAO.findTrainingsByTrainerCriteria("trainerUser", fromDate, toDate, "Trainee Name"))
                .thenReturn(trainings);

        List<Training> result = trainingService.getTrainingsByTrainerCriteria("trainerUser", fromDate, toDate, "Trainee Name");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(training.getId(), result.get(0).getId());
    }

    @Test
    void testAddTraining() {
        doNothing().when(trainingDAO).addTraining(training);

        trainingService.addTraining(training);

        verify(trainingDAO, times(1)).addTraining(training);
    }
}
