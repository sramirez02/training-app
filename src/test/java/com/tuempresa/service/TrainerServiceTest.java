package com.tuempresa.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import com.tuempresa.dto.CreateGymUserResponseDto;
import com.tuempresa.dto.CreateTrainerRequestDto;
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

    @Mock private UserService userService;
    @Mock private TrainerDAO trainerDAO;
    @Mock private TraineeTrainerDAO traineeTrainerDAO;
    @Mock private TraineeDAO traineeDAO;
    @Mock private TrainingTypeDAO trainingTypeDAO;
    
    @InjectMocks private TrainerService trainerService;
    
    private User testUser;
    private Trainer testTrainer;
    private TrainingType testTrainingType;
    private Trainee testTrainee;
    private TraineeTrainer testTraineeTrainer;
    private User traineeUser;

    @BeforeEach
    void setUp() {
        testUser = new User("John", "Trainer", true);
        testUser.setId(1L);
        testUser.setUsername("john.trainer");
        
        testTrainingType = new TrainingType(1L, "Fitness");
        
        testTrainer = new Trainer(1L, 1L, 1L);
        
        testTrainee = new Trainee(1L, 2L, LocalDate.of(1990, 1, 1), "123 Main St");
        
        traineeUser = new User("Alice", "Trainee", true);
        traineeUser.setId(2L);
        traineeUser.setUsername("alice.trainee");
        
        testTraineeTrainer = new TraineeTrainer(1L, 1L, 1L);
    }

    @Test
    void getTrainerByUsername_ShouldReturnTrainer_WhenUserExists() {
        when(userService.getByUsername("john.trainer")).thenReturn(testUser);
        when(trainerDAO.findByUserId(1L)).thenReturn(Optional.of(testTrainer));
        
        Trainer result = trainerService.getTrainerByUsername("john.trainer");
        
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(userService).getByUsername("john.trainer");
        verify(trainerDAO).findByUserId(1L);
    }

    @Test
    void getTrainerByUsername_ShouldReturnNull_WhenUserNotFound() {
        when(userService.getByUsername("unknown")).thenReturn(null);
        
        Trainer result = trainerService.getTrainerByUsername("unknown");
        
        assertNull(result);
    }

    @Test
    void getTrainerUserByUsernameUser_ShouldReturnUserWithTrainer() {
        when(userService.getByUsername("john.trainer")).thenReturn(testUser);
        when(trainerDAO.findByUserId(1L)).thenReturn(Optional.of(testTrainer));
        
        User result = trainerService.getTrainerUserByUsernameUser("john.trainer");
        
        assertNotNull(result);
        assertNotNull(result.getTrainer());
        assertEquals(1L, result.getTrainer().getId());
    }

    @Test
    void getTrainerUserByUsernameUser_ShouldThrowException_WhenUserNotFound() {
        when(userService.getByUsername("unknown")).thenReturn(null);
        
        assertThrows(RuntimeException.class, () -> {
            trainerService.getTrainerUserByUsernameUser("unknown");
        });
    }

    @Test
    void createUserTrainer_ShouldCreateNewTrainer() {
        CreateTrainerRequestDto request = new CreateTrainerRequestDto();
        request.setFirstName("New");
        request.setLastName("Trainer");
        request.setTrainingTypeId(1L);
        
        when(userService.createUser(any(User.class))).thenReturn(testUser);
        when(trainerDAO.save(any(Trainer.class))).thenReturn(testTrainer);
        
        CreateGymUserResponseDto result = trainerService.createUserTrainer(request);
        
        assertNotNull(result);
        assertNotNull(result.getUsername());
        assertNotNull(result.getPassword());
        verify(userService).createUser(any(User.class));
        verify(trainerDAO).save(any(Trainer.class));
    }

    @Test
    void getTrainerProfile_ShouldReturnCompleteProfile() {
        when(userService.getByUsername("john.trainer")).thenReturn(testUser);
        when(trainerDAO.findByUserId(1L)).thenReturn(Optional.of(testTrainer));
        when(trainingTypeDAO.findById(1L)).thenReturn(Optional.of(testTrainingType));
        when(traineeTrainerDAO.findByTrainerId(1L)).thenReturn(Collections.emptyList());
        
        TrainerProfileResponseDto result = trainerService.getTrainerProfile("john.trainer");
        
        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Trainer", result.getLastName());
        assertEquals("Fitness", result.getSpecialization().getTrainingTypeName());
        assertTrue(result.isActive());
        assertTrue(result.getTraineesList().isEmpty());
    }

    @Test
    void getTrainerProfile_ShouldThrowException_WhenUserNotFound() {
        when(userService.getByUsername("unknown")).thenReturn(null);
        
        assertThrows(RuntimeException.class, () -> {
            trainerService.getTrainerProfile("unknown");
        });
    }

    @Test
    void updateTrainerProfile_ShouldUpdateProfile() {
        UpdateTrainerRequestDto request = new UpdateTrainerRequestDto();
        request.setUsername("john.trainer");
        request.setFirstName("John Updated");
        request.setLastName("Trainer Updated");
        request.setActive(false);
        
        when(userService.getByUsername("john.trainer")).thenReturn(testUser);
        when(trainerDAO.findByUserId(1L)).thenReturn(Optional.of(testTrainer));
        when(trainingTypeDAO.findById(1L)).thenReturn(Optional.of(testTrainingType));
        when(traineeTrainerDAO.findByTrainerId(1L)).thenReturn(Collections.emptyList());
        
        UpdateTrainerProfileResponseDto result = trainerService.updateTrainerProfile(request);
        
        assertNotNull(result);
        assertEquals("John Updated", result.getFirstName());
        assertEquals("Trainer Updated", result.getLastName());
        assertFalse(result.getIsActive());
        verify(userService).save(testUser);
    }

    @Test
    void getUnassignedActiveTrainers_ShouldReturnUnassignedTrainers() {
        List<Trainer> activeTrainers = List.of(testTrainer);
        when(userService.getByUsername("alice.trainee")).thenReturn(traineeUser);
        when(trainerDAO.findAllActiveTrainers()).thenReturn(activeTrainers);
        when(traineeTrainerDAO.findTrainerIdsByTraineeUsername("alice.trainee")).thenReturn(Collections.emptyList());
        when(userService.getUserById(1L)).thenReturn(testUser);
        when(trainingTypeDAO.findById(1L)).thenReturn(Optional.of(testTrainingType));
        
        List<UnassignedTrainerDto> result = trainerService.getUnassignedActiveTrainers("alice.trainee");
        
        assertFalse(result.isEmpty());
        assertEquals("john.trainer", result.get(0).getUsername());
        assertEquals("Fitness", result.get(0).getSpecialization().getTrainingTypeName());
    }

    @Test
    void toggleTrainerStatus_ShouldActivateInactiveTrainer() {
        testUser.setIsActive(false);
        when(userService.findByUsername("john.trainer")).thenReturn(Optional.of(testUser));
        
        trainerService.toggleTrainerStatus("john.trainer", true);
        
        assertTrue(testUser.getIsActive());
        verify(userService).save(testUser);
    }

    @Test
    void toggleTrainerStatus_ShouldNotChange_WhenAlreadyActive() {
        testUser.setIsActive(true);
        when(userService.findByUsername("john.trainer")).thenReturn(Optional.of(testUser));
        
        trainerService.toggleTrainerStatus("john.trainer", true);
        
        verify(userService, never()).save(testUser);
    }

    @Test
    void getTrainerIdByUsername_ShouldReturnId() {
        when(userService.getByUsername("john.trainer")).thenReturn(testUser);
        when(trainerDAO.findByUserId(1L)).thenReturn(Optional.of(testTrainer));
        
        Long result = trainerService.getTrainerIdByUsername("john.trainer");
        
        assertEquals(1L, result);
    }

    @Test
    void createTrainer_ShouldSaveAndReturnTrainer() {
        when(trainerDAO.save(testTrainer)).thenReturn(testTrainer);
        
        Trainer result = trainerService.createTrainer(testTrainer);
        
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(trainerDAO).save(testTrainer);
    }

    @Test
    void getTrainer_ShouldReturnTrainer_WhenExists() {
        when(trainerDAO.findById(1L)).thenReturn(Optional.of(testTrainer));
        
        Trainer result = trainerService.getTrainer(1L);
        
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getTrainer_ShouldReturnNull_WhenNotExists() {
        when(trainerDAO.findById(99L)).thenReturn(Optional.empty());
        
        Trainer result = trainerService.getTrainer(99L);
        
        assertNull(result);
    }

    @Test
    void getAllTrainers_ShouldReturnAllTrainers() {
        when(trainerDAO.findAll()).thenReturn(List.of(testTrainer));
        
        List<Trainer> result = trainerService.getAllTrainers();
        
        assertFalse(result.isEmpty());
        assertEquals(1L, result.get(0).getId());
    }

    @Test
    void getByUserId_ShouldReturnTrainer() {
        when(trainerDAO.findByUserId(1L)).thenReturn(Optional.of(testTrainer));
        
        Trainer result = trainerService.getByUserId(1L);
        
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void toggleTrainerStatus1_ShouldChangeStatus() {
        when(userService.getByUsername("john.trainer")).thenReturn(testUser);
        
        trainerService.toggleTrainerStatus1("john.trainer", false);
        
        assertFalse(testUser.getIsActive());
        verify(userService).save(testUser);
    }
}