package com.tuempresa.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import com.tuempresa.dao.TraineeDAO;
import com.tuempresa.dao.TraineeTrainerDAO;
import com.tuempresa.dao.TrainerDAO;
import com.tuempresa.dao.TrainingTypeDAO;
import com.tuempresa.dto.CreateGymUserResponseDto;
import com.tuempresa.dto.CreateTrainerRequestDto;
import com.tuempresa.dto.TrainerProfileResponseDto;
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
    private PasswordEncoder encoder;

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
    private TraineeTrainer testTraineeTrainer;

    @BeforeEach
    void setUp() {
        testUser = new User("John", "Doe", true);
        testUser.setId(1L);
        testUser.setUsername("john.doe");

        testTrainingType = new TrainingType();
        testTrainingType.setId(1L);
        // No hay setName en TrainingType según el código proporcionado

        testTrainer = new Trainer();
        testTrainer.setId(1L);
        testTrainer.setUserId(testUser.getId());
        testTrainer.setTrainingTypeId(testTrainingType.getId());

        testTrainee = new Trainee();
        testTrainee.setId(1L);
        testTrainee.setUserId(2L);

        testTraineeTrainer = new TraineeTrainer();
        testTraineeTrainer.setId(1L);
        testTraineeTrainer.setTraineeId(testTrainee.getId());
        testTraineeTrainer.setTrainerId(testTrainer.getId());
    }

    @Test
    void getTrainerByUsername_ShouldReturnTrainer_WhenUserExists() {
        when(userService.getByUsername("john.doe")).thenReturn(testUser);
        when(trainerDAO.findByUserId(testUser.getId())).thenReturn(Optional.of(testTrainer));

        Trainer result = trainerService.getTrainerByUsername("john.doe");

        assertNotNull(result);
        assertEquals(testTrainer.getId(), result.getId());
    }

    @Test
    void createUserTrainer_ShouldCreateTrainerAndReturnResponse() {
        CreateTrainerRequestDto requestDto = new CreateTrainerRequestDto();
        requestDto.setFirstName("John");
        requestDto.setLastName("Doe");
        requestDto.setTrainingTypeId(1L);

        String generatedPassword = "randomPass123";
        String encodedPassword = "encodedRandomPass123";

        when(userService.generateRandomPassword()).thenReturn(generatedPassword);
        when(encoder.encode(generatedPassword)).thenReturn(encodedPassword);
        when(userService.createUser(any(User.class))).thenReturn(testUser);
        when(trainerDAO.save(any(Trainer.class))).thenReturn(testTrainer);

        CreateGymUserResponseDto response = trainerService.createUserTrainer(requestDto);

        assertNotNull(response);
        assertEquals(testUser.getUsername(), response.getUsername());
        assertEquals(generatedPassword, response.getPassword());
    }

    @Test
    void getTrainerProfile_ShouldReturnProfileDto() {
        when(userService.getByUsername("john.doe")).thenReturn(testUser);
        when(trainerDAO.findByUserId(testUser.getId())).thenReturn(Optional.of(testTrainer));
        when(trainingTypeDAO.findById(testTrainer.getTrainingTypeId())).thenReturn(Optional.of(testTrainingType));
        when(traineeTrainerDAO.findByTrainerId(testTrainer.getId())).thenReturn(Collections.emptyList());

        TrainerProfileResponseDto profile = trainerService.getTrainerProfile("john.doe");

        assertNotNull(profile);
        assertEquals(testUser.getFirstName(), profile.getFirstName());
        assertEquals(testUser.getLastName(), profile.getLastName());
        assertEquals(testTrainingType, profile.getSpecialization());
        assertTrue(profile.isActive());
        assertTrue(profile.getTraineesList().isEmpty());
    }

    @Test
    void updateTrainerProfile_ShouldUpdateAndReturnProfile() {
        UpdateTrainerRequestDto request = new UpdateTrainerRequestDto();
        request.setUsername("john.doe");
        request.setFirstName("Johnny");
        request.setLastName("Doey");
        request.setActive(false);

        when(userService.getByUsername("john.doe")).thenReturn(testUser);
        when(trainerDAO.findByUserId(testUser.getId())).thenReturn(Optional.of(testTrainer));
        when(trainingTypeDAO.findById(testTrainer.getTrainingTypeId())).thenReturn(Optional.of(testTrainingType));
        when(traineeTrainerDAO.findByTrainerId(testTrainer.getId())).thenReturn(Collections.emptyList());
        
        
        doNothing().when(userService).save(testUser);

        UpdateTrainerProfileResponseDto response = trainerService.updateTrainerProfile(request);

        assertNotNull(response);
        assertEquals("Johnny", response.getFirstName());
        assertEquals("Doey", response.getLastName());
        assertEquals(testTrainingType, response.getSpecialization());
        
        assertFalse(response.getIsActive());
    }


    @Test
    void toggleTrainerStatus1_ShouldDeactivate_WhenActive1() {
        testUser.setIsActive(true);
        when(userService.getByUsername("john.doe")).thenReturn(testUser);
        
        
        doNothing().when(userService).save(testUser);

        trainerService.toggleTrainerStatus1("john.doe", false);

        assertFalse(testUser.getIsActive());
        verify(userService).save(testUser);
    }

    @Test
    void toggleTrainerStatus_ShouldNotChange_WhenAlreadyInDesiredState() {
        testUser.setIsActive(true);
        when(userService.findByUsername("john.doe")).thenReturn(Optional.of(testUser));

        trainerService.toggleTrainerStatus("john.doe", true);

        assertTrue(testUser.getIsActive());
        verify(userService, never()).save(any());
    }

    @Test
    void getTrainerIdByUsername_ShouldReturnId() {
        when(userService.getByUsername("john.doe")).thenReturn(testUser);
        when(trainerDAO.findByUserId(testUser.getId())).thenReturn(Optional.of(testTrainer));

        Long id = trainerService.getTrainerIdByUsername("john.doe");

        assertEquals(testTrainer.getId(), id);
    }

    @Test
    void getTrainerIdByUsername_ShouldThrowException_WhenUserNotFound() {
        when(userService.getByUsername("unknown")).thenReturn(null);

        assertThrows(RuntimeException.class, () -> 
            trainerService.getTrainerIdByUsername("unknown"));
    }

    @Test
    void createTrainer_ShouldSaveAndReturnTrainer() {
        when(trainerDAO.save(testTrainer)).thenReturn(testTrainer);

        Trainer result = trainerService.createTrainer(testTrainer);

        assertNotNull(result);
        assertEquals(testTrainer.getId(), result.getId());
    }

    @Test
    void getTrainer_ShouldReturnTrainer_WhenExists() {
        when(trainerDAO.findById(1L)).thenReturn(Optional.of(testTrainer));

        Trainer result = trainerService.getTrainer(1L);

        assertNotNull(result);
        assertEquals(testTrainer.getId(), result.getId());
    }

    @Test
    void getTrainer_ShouldReturnNull_WhenNotExists() {
        when(trainerDAO.findById(99L)).thenReturn(Optional.empty());

        Trainer result = trainerService.getTrainer(99L);

        assertNull(result);
    }

    @Test
    void getAllTrainers_ShouldReturnList() {
        when(trainerDAO.findAll()).thenReturn(List.of(testTrainer));

        List<Trainer> trainers = trainerService.getAllTrainers();

        assertNotNull(trainers);
        assertEquals(1, trainers.size());
        assertEquals(testTrainer.getId(), trainers.get(0).getId());
    }

    @Test
    void getByUserId_ShouldReturnTrainer() {
        when(trainerDAO.findByUserId(1L)).thenReturn(Optional.of(testTrainer));

        Trainer result = trainerService.getByUserId(1L);

        assertNotNull(result);
        assertEquals(testTrainer.getId(), result.getId());
    }

    @Test
    void getByUserId_ShouldThrowException_WhenNotFound() {
        when(trainerDAO.findByUserId(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> 
            trainerService.getByUserId(99L));
    }

    @Test
    void toggleTrainerStatus1_ShouldActivate_WhenInactive() {
        testUser.setIsActive(false);
        when(userService.getByUsername("john.doe")).thenReturn(testUser);
        
        
        doNothing().when(userService).save(testUser);

        trainerService.toggleTrainerStatus1("john.doe", true);

        assertTrue(testUser.getIsActive());
        
        verify(userService).save(testUser);
    }

    @Test
    void toggleTrainerStatus1_ShouldDeactivate_WhenActive() {
        testUser.setIsActive(true);
        when(userService.getByUsername("john.doe")).thenReturn(testUser);
        
        
        doNothing().when(userService).save(testUser);

        trainerService.toggleTrainerStatus1("john.doe", false);

        assertFalse(testUser.getIsActive());
        
        verify(userService).save(testUser);
    }

    @Test
    void toggleTrainerStatus1_ShouldThrowException_WhenUserNotFound() {
        when(userService.getByUsername("unknown")).thenReturn(null);

        assertThrows(RuntimeException.class, () -> 
            trainerService.toggleTrainerStatus1("unknown", true));
        
        
        verify(userService, never()).save(any());
    }
}