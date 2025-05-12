package com.tuempresa.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.tuempresa.dao.TraineeDAO;
import com.tuempresa.entity.Trainee;
import com.tuempresa.entity.User;

class TraineeServiceTest {

    @Mock
    private TraineeDAO traineeDAO;
    
    @Mock
    private UserService userService;

    @InjectMocks
    private TraineeService traineeService;
    private User user;
    private Trainee trainee;

    @BeforeEach
    void setUp() {
    	user = new User();
        user.setId(1L);
        user.setUsername("john.doe");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setIsActive(true);

        trainee = new Trainee();
        trainee.setId(1L);
        trainee.setUserId(1L);
        trainee.setAddress("123 Street");
        trainee.setDateOfBirth(LocalDate.of(2000, 1, 1));
    	
    	MockitoAnnotations.openMocks(this);
        
    }

    @Test
    void testGetByUserId() {
        
        Long userId = 1L;
        Trainee mockTrainee = new Trainee(10L, userId, null, "123 avenue 3");
        
        when(traineeDAO.findByUserId(userId)).thenReturn(mockTrainee);
                
        Trainee result = traineeService.getByUserId(userId);
        
        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals("123 avenue 3", result.getAddress());
        verify(traineeDAO, times(1)).findByUserId(userId);
    }
    
    @Test
    void testGetByUsername() {
        
        String username = "jane";
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername(username);
        
        Trainee mockTrainee = new Trainee(100L, 1L, LocalDate.of(1995, 1, 1), "123 Main St");

        when(userService.getByUsername(username)).thenReturn(mockUser);
        when(traineeDAO.findByUserId(1L)).thenReturn(mockTrainee);

        // Act
        Trainee result = traineeService.getByUsername(username);

        // Assert
        assertNotNull(result);
        assertEquals(100L, result.getId());
        assertEquals("123 Main St", result.getAddress());
        verify(userService).getByUsername(username);
        verify(traineeDAO).findByUserId(1L);
    }
    
    @Test
    void testCreateUserTrainee() {
        // Arrange
        User userInput = new User();
        userInput.setUsername("jane");

        User savedUser = new User();
        savedUser.setId(2L);
        savedUser.setUsername("jane");

        String dateOfBirth = "2000-05-15";
        String address = "456 Elm St";
        LocalDate parsedDate = LocalDate.of(2000, 5, 15);

        Trainee savedTrainee = new Trainee(10L, 2L, parsedDate, address);

        when(userService.createUser(userInput)).thenReturn(savedUser);
        when(traineeDAO.save(any(Trainee.class))).thenReturn(savedTrainee);

        
        Trainee result = traineeService.createUserTrainee(userInput, dateOfBirth, address);

        
        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals(2L, result.getUserId());
        assertEquals(parsedDate, result.getDateOfBirth());
        assertEquals(address, result.getAddress());

        verify(userService).createUser(userInput);
        verify(traineeDAO).save(any(Trainee.class));
    }


    @Test
    void testGetTraineeUserByUsername() {
        when(userService.getByUsername("john.doe")).thenReturn(user);
        when(traineeDAO.findByUserId(1L)).thenReturn(trainee);

        User result = traineeService.getTraineeUserByUsername("john.doe");

        assertNotNull(result);
        assertEquals("john.doe", result.getUsername());
        assertEquals(trainee, result.getTrainee());
    }

    @Test
    void testUpdateTraineeProfile() {
        when(userService.findByUsername("john.doe")).thenReturn(Optional.of(user));
        when(traineeDAO.findByUserId(1L)).thenReturn(trainee);

        traineeService.updateTraineeProfile("john.doe", "Jane", "Smith", LocalDate.of(1995, 5, 15), "New Address");

        verify(userService).save(argThat(u -> 
            u.getFirstName().equals("Jane") &&
            u.getLastName().equals("Smith") &&
            u.getUsername().equals("jane.smith")
        ));

        verify(traineeDAO).save(argThat(t -> 
            t.getDateOfBirth().equals(LocalDate.of(1995, 5, 15)) &&
            t.getAddress().equals("New Address")
        ));
    }

    @Test
    void testUpdateTraineeProfile_UserNotFound() {
        when(userService.findByUsername("unknown")).thenReturn(Optional.empty());

        traineeService.updateTraineeProfile("unknown", "Test", "User", LocalDate.now(), "Nowhere");

        verify(userService, never()).save(any());
        verify(traineeDAO, never()).save(any());
    }

    @Test
    void testToggleTraineeStatus_Activate() {
        user.setIsActive(false);
        when(userService.findByUsername("john.doe")).thenReturn(Optional.of(user));

        traineeService.toggleTraineeStatus("john.doe", true);

        verify(userService).save(argThat(u -> Boolean.TRUE.equals(u.getIsActive())));
    }

    @Test
    void testToggleTraineeStatus_AlreadyActive() {
        user.setIsActive(true);
        when(userService.findByUsername("john.doe")).thenReturn(Optional.of(user));

        traineeService.toggleTraineeStatus("john.doe", true);

        verify(userService, never()).save(any());
    }

    @Test
    void testToggleTraineeStatus_UserNotFound() {
        when(userService.findByUsername("unknown")).thenReturn(Optional.empty());

        traineeService.toggleTraineeStatus("unknown", true);

        verify(userService, never()).save(any());
    }

    @Test
    void testCreateTrainee() {
        when(traineeDAO.save(trainee)).thenReturn(trainee);

        Trainee result = traineeService.createTrainee(trainee);

        assertEquals(trainee, result);
        verify(traineeDAO).save(trainee);
    }

    @Test
    void testGetTrainee() {
        when(traineeDAO.findById(1L)).thenReturn(Optional.of(trainee));

        Trainee result = traineeService.getTrainee(1L);

        assertEquals(trainee, result);
    }

    @Test
    void testGetTrainee_NotFound() {
        when(traineeDAO.findById(2L)).thenReturn(Optional.empty());

        Trainee result = traineeService.getTrainee(2L);

        assertNull(result);
    }

    @Test
    void testGetAllTrainees() {
        List<Trainee> trainees = List.of(trainee);
        when(traineeDAO.findAll()).thenReturn(trainees);

        List<Trainee> result = traineeService.getAllTrainees();

        assertEquals(1, result.size());
        assertEquals(trainee, result.get(0));
    }

    @Test
    void testGetByUserId1() {
        when(traineeDAO.findByUserId(1L)).thenReturn(trainee);

        Trainee result = traineeService.getByUserId(1L);

        assertEquals(trainee, result);
    }

    @Test
    void testDeleteTraineeByUsername() {
        when(userService.getByUsername("john.doe")).thenReturn(user);
        when(traineeDAO.findByUserId(1L)).thenReturn(trainee);

        traineeService.deleteTraineeByUsername("john.doe");

        verify(traineeDAO).delete(trainee);
    }

    @Test
    void testDeleteTraineeByUsername_UserNotFound() {
        when(userService.getByUsername("unknown")).thenReturn(null);

        traineeService.deleteTraineeByUsername("unknown");

        verify(traineeDAO, never()).delete(any());
    }

    @Test
    void testDeleteTraineeByUsername_TraineeNotFound() {
        when(userService.getByUsername("john.doe")).thenReturn(user);
        when(traineeDAO.findByUserId(1L)).thenReturn(null);

        traineeService.deleteTraineeByUsername("john.doe");

        verify(traineeDAO, never()).delete(any());
    }

    
}
