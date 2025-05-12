package com.tuempresa.service;

import com.tuempresa.dao.TrainerDAO;
import com.tuempresa.entity.Trainer;
import com.tuempresa.entity.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class TrainerServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private TrainerDAO trainerDAO;

    @InjectMocks
    private TrainerService trainerService;

    private User user;
    private Trainer trainer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setUsername("john.doe");
        user.setIsActive(true);

        trainer = new Trainer(1L, 1L, 100L);
    }

    @Test
    void testGetTrainerByUsername() {
        when(userService.getByUsername("john.doe")).thenReturn(user);
        when(trainerDAO.findByUserId(1L)).thenReturn(trainer);

        Trainer result = trainerService.getTrainerByUsername("john.doe");
        assertEquals(trainer, result);
    }

    @Test
    void testGetTrainerUserByUsernameUser() {
        when(userService.getByUsername("john.doe")).thenReturn(user);
        when(trainerDAO.findByUserId(1L)).thenReturn(trainer);

        User result = trainerService.getTrainerUserByUsernameUser("john.doe");
        assertEquals(user, result);
        assertEquals(trainer, result.getTrainer());
    }

    @Test
    void testCreateUserTrainer() {
        when(userService.createUser(any(User.class))).thenReturn(user);
        when(trainerDAO.save(any(Trainer.class))).thenReturn(trainer);

        User result = trainerService.createUserTrainer(user, 100L);
        assertEquals(user, result);
        verify(trainerDAO).save(any(Trainer.class));
    }

    @Test
    void testUpdateTrainerProfile() {
        when(userService.findByUsername("john.doe")).thenReturn(Optional.of(user));
        when(trainerDAO.findByUserId(1L)).thenReturn(trainer);

        trainerService.updateTrainerProfile("john.doe", "Jane", "Smith", 200L);

        assertEquals("Jane", user.getFirstName());
        assertEquals("Smith", user.getLastName());
        assertEquals("jane.smith", user.getUsername());
        assertEquals(200L, trainer.getTrainingTypeId());

        verify(userService).save(user);
        verify(trainerDAO).save(trainer);
    }

    @Test
    void testToggleTrainerStatus_Activate() {
        user.setIsActive(false);
        when(userService.findByUsername("john.doe")).thenReturn(Optional.of(user));

        trainerService.toggleTrainerStatus("john.doe", true);

        assertTrue(user.getIsActive());
        verify(userService).save(user);
    }

    @Test
    void testToggleTrainerStatus_Deactivate() {
        user.setIsActive(true);
        when(userService.findByUsername("john.doe")).thenReturn(Optional.of(user));

        trainerService.toggleTrainerStatus("john.doe", false);

        assertFalse(user.getIsActive());
        verify(userService).save(user);
    }

    @Test
    void testCreateTrainer() {
        when(trainerDAO.save(trainer)).thenReturn(trainer);
        Trainer result = trainerService.createTrainer(trainer);
        assertEquals(trainer, result);
    }

    @Test
    void testGetTrainer() {
        when(trainerDAO.findById(1L)).thenReturn(Optional.of(trainer));
        Trainer result = trainerService.getTrainer(1L);
        assertEquals(trainer, result);
    }

    @Test
    void testGetAllTrainers() {
        List<Trainer> trainers = Arrays.asList(trainer);
        when(trainerDAO.findAll()).thenReturn(trainers);
        List<Trainer> result = trainerService.getAllTrainers();
        assertEquals(1, result.size());
        assertEquals(trainer, result.get(0));
    }

    @Test
    void testGetByUserId() {
        when(trainerDAO.findByUserId(1L)).thenReturn(trainer);
        Trainer result = trainerService.getByUserId(1L);
        assertEquals(trainer, result);
    }
    
    @Test
    void getTrainerByUsername_userExists_returnsTrainer() {
        User user = new User();
        user.setId(1L);

        Trainer trainer = new Trainer();
        when(userService.getByUsername("test")).thenReturn(user);
        when(trainerDAO.findByUserId(1L)).thenReturn(trainer);

        Trainer result = trainerService.getTrainerByUsername("test");
        assertEquals(trainer, result);
    }

    @Test
    void getTrainerByUsername_userDoesNotExist_returnsNull() {
        when(userService.getByUsername("test")).thenReturn(null);
        Trainer result = trainerService.getTrainerByUsername("test");
        assertNull(result);
    }

    @Test
    void getTrainerUserByUsernameUser_returnsUserWithTrainer() {
        User user = new User();
        user.setId(1L);
        Trainer trainer = new Trainer();

        when(userService.getByUsername("test")).thenReturn(user);
        when(trainerDAO.findByUserId(1L)).thenReturn(trainer);

        User result = trainerService.getTrainerUserByUsernameUser("test");
        assertEquals(trainer, result.getTrainer());
    }

    @Test
    void createUserTrainer_savesUserAndTrainer() {
        User user = new User();
        user.setId(2L);
        Long trainingTypeId = 3L;

        when(userService.createUser(user)).thenReturn(user);

        User result = trainerService.createUserTrainer(user, trainingTypeId);

        assertEquals(user, result);
        verify(trainerDAO).save(any(Trainer.class));
    }

    @Test
    void updateTrainerProfile_userAndTrainerExist_updatesSuccessfully() {
        User user = new User();
        user.setId(1L);
        user.setUsername("old.username");

        Trainer trainer = new Trainer();

        when(userService.findByUsername("test")).thenReturn(Optional.of(user));
        when(trainerDAO.findByUserId(1L)).thenReturn(trainer);

        trainerService.updateTrainerProfile("test", "John", "Doe", 10L);

        assertEquals("john.doe", user.getUsername());
        assertEquals(10L, trainer.getTrainingTypeId());
        verify(userService).save(user);
        verify(trainerDAO).save(trainer);
    }

    @Test
    void updateTrainerProfile_userExistsButTrainerNotFound_logsWarning() {
        User user = new User();
        user.setId(1L);

        when(userService.findByUsername("test")).thenReturn(Optional.of(user));
        when(trainerDAO.findByUserId(1L)).thenReturn(null);

        trainerService.updateTrainerProfile("test", "Ana", "Lopez", 7L);

        verify(userService).save(user);
        
        verify(trainerDAO, never()).save(any());
    }

    @Test
    void updateTrainerProfile_userNotFound_logsWarning() {
        when(userService.findByUsername("test")).thenReturn(Optional.empty());
        trainerService.updateTrainerProfile("test", "Ana", "Lopez", 7L);

        verify(userService, never()).save(any());
        verify(trainerDAO, never()).save(any());
    }

    @Test
    void toggleTrainerStatus_activate_userIsInactive_activatesUser() {
        User user = new User();
        user.setIsActive(false);

        when(userService.findByUsername("test")).thenReturn(Optional.of(user));

        trainerService.toggleTrainerStatus("test", true);

        assertTrue(user.getIsActive());
        verify(userService).save(user);
    }

    @Test
    void toggleTrainerStatus_deactivate_userIsActive_deactivatesUser() {
        User user = new User();
        user.setIsActive(true);

        when(userService.findByUsername("test")).thenReturn(Optional.of(user));

        trainerService.toggleTrainerStatus("test", false);

        assertFalse(user.getIsActive());
        verify(userService).save(user);
    }

    @Test
    void toggleTrainerStatus_activate_userAlreadyActive_logsWarning() {
        User user = new User();
        user.setIsActive(true);

        when(userService.findByUsername("test")).thenReturn(Optional.of(user));

        trainerService.toggleTrainerStatus("test", true);

        verify(userService, never()).save(user);
    }

    @Test
    void toggleTrainerStatus_deactivate_userAlreadyInactive_logsWarning() {
        User user = new User();
        user.setIsActive(false);

        when(userService.findByUsername("test")).thenReturn(Optional.of(user));

        trainerService.toggleTrainerStatus("test", false);

        verify(userService, never()).save(user);
    }

    @Test
    void toggleTrainerStatus_userNotFound_logsWarning() {
        when(userService.findByUsername("test")).thenReturn(Optional.empty());

        trainerService.toggleTrainerStatus("test", true);

        verify(userService, never()).save(any());
    }

}
