package com.tuempresa.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import com.tuempresa.dto.CreateGymUserResponseDto;
import com.tuempresa.dto.CreateTrainerRequestDto;
import com.tuempresa.dto.TraineeInfoDto;
import com.tuempresa.dto.TrainerProfileResponseDto;
import com.tuempresa.dto.UnassignedTrainerDto;
import com.tuempresa.dto.UpdateTrainerProfileResponseDto;
import com.tuempresa.dto.UpdateTrainerRequestDto;
import com.tuempresa.entity.Trainee;
import com.tuempresa.entity.TraineeTrainer;
import com.tuempresa.entity.Trainer;
import com.tuempresa.entity.TrainingType;
import com.tuempresa.entity.User;

@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {

    @Mock
    private UserService userService;
    
    @Mock
    private TrainerDAO trainerDAO;
    
    @Mock
    private TraineeTrainerDAO traineeTrainerDAO;
    
    @Mock
    private TraineeDAO traineeDAO;
    
    @Mock
    private TrainingTypeDAO trainingTypeDAO;
    
    @InjectMocks
    private TrainerService trainerService;
    
    private User testUser;
    private Trainer testTrainer;
    private TrainingType testTrainingType;
    private Trainee testTrainee;

    @BeforeEach
    void setUp() {
        testUser = new User("John", "Trainer", true);
        testUser.setId(1L);
        
        testTrainer = new Trainer();
        testTrainer.setId(1L);
        testTrainer.setUserId(testUser.getId());
        testTrainer.setTrainingTypeId(1L);
        
        testTrainingType = new TrainingType(1L, "Cardio");
        
        testTrainee = new Trainee();
        testTrainee.setId(1L);
        testTrainee.setUserId(2L);
    }

    @Test
    void getTrainerProfile_Success() {
        // Arrange
        when(userService.getByUsername("john.trainer")).thenReturn(testUser);
        when(trainerDAO.findByUserId(testUser.getId())).thenReturn(Optional.of(testTrainer));
        when(trainingTypeDAO.findById(testTrainer.getTrainingTypeId()))
            .thenReturn(Optional.of(testTrainingType));
        when(traineeTrainerDAO.findByTrainerId(testTrainer.getId()))
            .thenReturn(Collections.emptyList());
        
        // Act
        TrainerProfileResponseDto response = trainerService.getTrainerProfile("john.trainer");
        
        // Assert
        assertNotNull(response);
        assertEquals("John", response.getFirstName());
        assertEquals("Trainer", response.getLastName());
        assertEquals(testTrainingType, response.getSpecialization());
        assertTrue(response.getTraineesList().isEmpty());
    }

    @Test
    void createUserTrainer_Success() {
        // Arrange
        CreateTrainerRequestDto request = new CreateTrainerRequestDto();
        request.setFirstName("John");
        request.setLastName("Trainer");
        request.setTrainingTypeId(1L);
        
        User savedUser = new User("John", "Trainer", true);
        savedUser.setId(1L);
        savedUser.setUsername("john.trainer");
        savedUser.setPassword("password");
        
        when(userService.createUser(any(User.class))).thenReturn(savedUser);
        when(trainerDAO.save(any(Trainer.class))).thenReturn(testTrainer);
        
        // Act
        CreateGymUserResponseDto response = trainerService.createUserTrainer(request);
        
        // Assert
        assertNotNull(response);
        assertEquals(savedUser.getUsername(), response.getUsername());
        assertEquals(savedUser.getPassword(), response.getPassword());
    }

    @Test
    void toggleTrainerStatus_Activate() {
        // Arrange
        User user = new User("john", "trainer", false);
        when(userService.findByUsername("john.trainer")).thenReturn(Optional.of(user));
        
        // Act
        trainerService.toggleTrainerStatus("john.trainer", true);
        
        // Assert
        assertTrue(user.getIsActive());
        verify(userService).save(user);
    }

    @Test
    void getTrainerIdByUsername_Success() {
        // Arrange
        when(userService.getByUsername("john.trainer")).thenReturn(testUser);
        when(trainerDAO.findByUserId(testUser.getId())).thenReturn(Optional.of(testTrainer));
        
        // Act
        Long trainerId = trainerService.getTrainerIdByUsername("john.trainer");
        
        // Assert
        assertEquals(testTrainer.getId(), trainerId);
    }

    @Test
    void getUnassignedActiveTrainers_Success() {
        // Arrange
        User traineeUser = new User("trainee", "one", true);
        traineeUser.setId(2L);
        
        when(userService.getByUsername("trainee.one")).thenReturn(traineeUser);
        when(trainerDAO.findAllActiveTrainers()).thenReturn(List.of(testTrainer));
        when(traineeTrainerDAO.findTrainerIdsByTraineeUsername("trainee.one"))
            .thenReturn(Collections.emptyList());
        when(userService.getUserById(testTrainer.getUserId())).thenReturn(testUser);
        when(trainingTypeDAO.findById(testTrainer.getTrainingTypeId()))
            .thenReturn(Optional.of(testTrainingType));
        
        // Act
        List<UnassignedTrainerDto> result = trainerService.getUnassignedActiveTrainers("trainee.one");
        
        // Assert
        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals(testTrainingType, result.get(0).getSpecialization());
    }

    @Test
    void updateTrainerProfile_Success() {
        // Arrange
        UpdateTrainerRequestDto request = new UpdateTrainerRequestDto();
        request.setUsername("john.trainer");
        request.setFirstName("John Updated");
        request.setLastName("Trainer Updated");
        request.setActive(true);
        
        when(userService.getByUsername("john.trainer")).thenReturn(testUser);
        when(trainerDAO.findByUserId(testUser.getId())).thenReturn(Optional.of(testTrainer));
        when(trainingTypeDAO.findById(testTrainer.getTrainingTypeId()))
            .thenReturn(Optional.of(testTrainingType));
        when(traineeTrainerDAO.findByTrainerId(testTrainer.getId()))
            .thenReturn(Collections.emptyList());
        
        // Act
        UpdateTrainerProfileResponseDto response = trainerService.updateTrainerProfile(request);
        
        // Assert
        assertNotNull(response);
        assertEquals("John Updated", response.getFirstName());
        assertEquals("Trainer Updated", response.getLastName());
        assertEquals(testTrainingType, response.getSpecialization());
        verify(userService).save(testUser);
    }

    @Test
    void getTrainerByUsername_Success() {
        // Arrange
        when(userService.getByUsername("john.trainer")).thenReturn(testUser);
        when(trainerDAO.findByUserId(testUser.getId())).thenReturn(Optional.of(testTrainer));
        
        // Act
        Trainer result = trainerService.getTrainerByUsername("john.trainer");
        
        // Assert
        assertNotNull(result);
        assertEquals(testTrainer.getId(), result.getId());
    }

    @Test
    void getTrainerUserByUsernameUser_Success() {
        // Arrange
        when(userService.getByUsername("john.trainer")).thenReturn(testUser);
        when(trainerDAO.findByUserId(testUser.getId())).thenReturn(Optional.of(testTrainer));
        
        // Act
        User result = trainerService.getTrainerUserByUsernameUser("john.trainer");
        
        // Assert
        assertNotNull(result);
        assertEquals(testUser.getFirstName(), result.getFirstName());
        assertNotNull(result.getTrainer());
        assertEquals(testTrainer.getId(), result.getTrainer().getId());
    }

    @Test
    void getTraineesForTrainer_Success() {
        // Arrange
        TraineeTrainer relation = new TraineeTrainer();
        relation.setTrainerId(testTrainer.getId());
        relation.setTraineeId(testTrainee.getId());
        
        User traineeUser = new User("Trainee", "One", true);
        traineeUser.setId(2L);
        
        when(traineeTrainerDAO.findByTrainerId(testTrainer.getId())).thenReturn(List.of(relation));
        when(traineeDAO.findById(testTrainee.getId())).thenReturn(Optional.of(testTrainee));
        when(userService.getUserById(testTrainee.getUserId())).thenReturn(traineeUser);
        
        // Act
        List<TraineeInfoDto> result = trainerService.getTraineesForTrainer(testTrainer.getId());
        
        // Assert
        assertEquals(1, result.size());
        assertEquals("Trainee", result.get(0).getFirstName());
        assertEquals("One", result.get(0).getLastName());
    }
}