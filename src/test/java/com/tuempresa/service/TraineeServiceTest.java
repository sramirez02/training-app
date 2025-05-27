package com.tuempresa.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tuempresa.dao.TraineeDAO;
import com.tuempresa.dao.TraineeTrainerDAO;
import com.tuempresa.dao.TrainerDAO;
import com.tuempresa.dao.TrainingTypeDAO;
import com.tuempresa.dto.*;
import com.tuempresa.entity.*;

@ExtendWith(MockitoExtension.class)
class TraineeServiceTest {

    @Mock
    private TraineeDAO traineeDAO;
    
    @Mock
    private UserService userService;
    
    @Mock
    private TraineeTrainerDAO traineeTrainerDAO;
    
    @Mock
    private TrainerDAO trainerDAO;
    
    @Mock
    private TrainingTypeDAO trainingTypeDAO;
    
    @InjectMocks
    private TraineeService traineeService;

    private User testUser;
    private Trainee testTrainee;
    private Trainer testTrainer;
    private TrainingType testTrainingType;

    @BeforeEach
    void setUp() {
        testUser = new User("John", "Doe", true);
        testUser.setId(1L);
        
        testTrainee = new Trainee(1L, testUser.getId(), LocalDate.now(), "123 Main St");
        
        testTrainer = new Trainer();
        testTrainer.setId(1L);
        testTrainer.setUserId(2L);
        testTrainer.setTrainingTypeId(1L);
        
        testTrainingType = new TrainingType(1L, "Cardio");
    }

    @Test
    void getTraineeProfile_Success() {
        // Arrange
        when(userService.getByUsername("john.doe")).thenReturn(testUser);
        when(traineeDAO.findByUserId(testUser.getId())).thenReturn(testTrainee);
        when(traineeTrainerDAO.findByTraineeId(testTrainee.getId()))
            .thenReturn(Collections.emptyList());
        
        
        TraineeProfileResponseDto response = traineeService.getTraineeProfile("john.doe");
        
        
        assertNotNull(response);
        assertEquals("John", response.getFirstName());
        assertEquals("Doe", response.getLastName());
        assertEquals(testTrainee.getDateOfBirth(), response.getDateOfBirth());
        assertTrue(response.getTrainersList().isEmpty());
    }

    @Test
    void getTraineeProfile_UserNotFound() {
        when(userService.getByUsername("unknown")).thenReturn(null);
        
        assertThrows(RuntimeException.class, () -> {
            traineeService.getTraineeProfile("unknown");
        });
    }

    @Test
    void createUserTrainee_Success() {
        
        CreateTraineeRequestDto request = new CreateTraineeRequestDto();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setDateOfBirth(LocalDate.now());
        request.setAddress("123 Main St");
        
        User savedUser = new User("John", "Doe", true);
        savedUser.setId(1L);
        savedUser.setUsername("john.doe");
        savedUser.setPassword("password");
        
        when(userService.createUser(any(User.class))).thenReturn(savedUser);
        when(traineeDAO.save(any(Trainee.class))).thenReturn(testTrainee);
        
        
        CreateGymUserResponseDto response = traineeService.createUserTrainee(request);
        
        
        assertNotNull(response);
        assertEquals(savedUser.getUsername(), response.getUsername());
        assertEquals(savedUser.getPassword(), response.getPassword());
    }

    @Test
    void toggleTraineeStatus_Activate() {
       
        User user = new User("john", "doe", false);
        when(userService.findByUsername("john.doe")).thenReturn(Optional.of(user));
        
        
        traineeService.toggleTraineeStatus("john.doe", true);
        
        
        assertTrue(user.getIsActive());
        verify(userService).save(user);
    }

    @Test
    void toggleTraineeStatus_Deactivate() {
        
        User user = new User("john", "doe", true);
        when(userService.findByUsername("john.doe")).thenReturn(Optional.of(user));
        
        
        traineeService.toggleTraineeStatus("john.doe", false);
        
        
        assertFalse(user.getIsActive());
        verify(userService).save(user);
    }

    @Test
    void getTraineeIdByUsername_Success() {
       
        when(userService.getByUsername("john.doe")).thenReturn(testUser);
        when(traineeDAO.findByUserId(testUser.getId())).thenReturn(testTrainee);
        
       
        Long traineeId = traineeService.getTraineeIdByUsername("john.doe");
        
      
        assertEquals(testTrainee.getId(), traineeId);
    }

    @Test
    void updateTraineeTrainers_Success() {
        
        UpdateTraineeTrainersRequest request = new UpdateTraineeTrainersRequest();
        request.setTraineeUsername("john.doe");
        request.setTrainersUsernames(List.of("trainer1"));
        
        when(traineeDAO.findByUserUsername("john.doe"))
        .thenReturn(Optional.of(testTrainee));
        when(trainerDAO.findByUserUsername("trainer1"))
        .thenReturn(Optional.of(testTrainer));
        when(userService.getUserById(testTrainer.getUserId()))
            .thenReturn(new User("Trainer", "One", true));
        when(trainingTypeDAO.findById(testTrainer.getTrainingTypeId()))
            .thenReturn(Optional.of(testTrainingType));
        
        
        List<TrainerInfoDto> response = traineeService.updateTraineeTrainers(request);
        
        
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("Trainer", response.get(0).getFirstName());
        verify(traineeTrainerDAO).saveAll(anyList());
    }

    @Test
    void deleteTraineeByUsername_Success() {
       
        when(userService.getByUsername("john.doe")).thenReturn(testUser);
        when(traineeDAO.findByUserId(testUser.getId())).thenReturn(testTrainee);
        
      
        traineeService.deleteTraineeByUsername("john.doe");
        
        
        verify(traineeDAO).delete(testTrainee);
    }

    @Test
    void getTrainersForTrainee_Success() {
        
        TraineeTrainer relation = new TraineeTrainer();
        relation.setTraineeId(1L);
        relation.setTrainerId(1L);
        
        when(traineeTrainerDAO.findByTraineeId(1L)).thenReturn(List.of(relation));
        when(trainerDAO.findById(1L)).thenReturn(Optional.of(testTrainer));
        when(userService.getUserById(testTrainer.getUserId()))
            .thenReturn(new User("Trainer", "One", true));
        when(trainingTypeDAO.findById(testTrainer.getTrainingTypeId()))
            .thenReturn(Optional.of(testTrainingType));
        
       
        List<TrainerInfoDto> result = traineeService.getTrainersForTrainee(1L);
        
       
        assertEquals(1, result.size());
        assertEquals("Trainer", result.get(0).getFirstName());
        assertEquals(testTrainingType, result.get(0).getSpecialization());
    }
}