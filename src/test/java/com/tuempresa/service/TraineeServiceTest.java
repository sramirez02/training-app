package com.tuempresa.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.tuempresa.dao.*;
import com.tuempresa.dto.*;
import com.tuempresa.entity.*;

class TraineeServiceTest {

    @Mock private TraineeDAO traineeDAO;
    @Mock private UserService userService;
    @Mock private TraineeTrainerDAO traineeTrainerDAO;
    @Mock private TrainerDAO trainerDAO;
    @Mock private TrainingTypeDAO trainingTypeDAO;
    @Mock private PasswordEncoder encoder;

    @InjectMocks private TraineeService traineeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        traineeService = new TraineeService(traineeDAO, userService, encoder);
        traineeService = spy(traineeService); 
    }

    @Test
    void testCreateUserTrainee_createsCorrectly() {
        User user = new User("Sandy", "Ramirez", true);
        user.setId(1L);
        when(userService.createUser(any())).thenReturn(user);

        Trainee expectedTrainee = new Trainee(null, 1L, LocalDate.of(2000, 1, 1), "Dirección");
        when(traineeDAO.save(any())).thenReturn(expectedTrainee);

        Trainee result = traineeService.createUserTrainee(user, "2000-01-01", "Dirección");

        assertNotNull(result);
        assertEquals(1L, result.getUserId());
        verify(userService).createUser(any());
        verify(traineeDAO).save(any());
    }

    @Test
    void testGetByUsername_returnsTrainee() {
        User user = new User("user", "test", true);
        user.setId(1L);
        when(userService.getByUsername("testuser")).thenReturn(user);
        when(traineeDAO.findByUserId(1L)).thenReturn(new Trainee());

        Trainee result = traineeService.getByUsername("testuser");
        assertNotNull(result);
    }

    @Test
    void testGetTraineeProfile_success() {
        User user = new User("Sandy", "Rico", true);
        user.setId(1L);
        Trainee trainee = new Trainee(10L, 1L, LocalDate.of(1995, 5, 5), "Calle 123");
        when(userService.getByUsername("sandy")).thenReturn(user);
        when(traineeDAO.findByUserId(1L)).thenReturn(trainee);
        when(traineeTrainerDAO.findByTraineeId(10L)).thenReturn(Collections.emptyList());

        TraineeProfileResponseDto profile = traineeService.getTraineeProfile("sandy");

        assertNotNull(profile);
        assertEquals("Sandy", profile.getFirstName());
    }

    @Test
    void testCreateUserTrainee_withDTO() {
        CreateTraineeRequestDto dto = new CreateTraineeRequestDto();
        dto.setFirstName("Laura");
        dto.setLastName("Gómez");
        dto.setAddress("Calle Luna");
        dto.setDateOfBirth(LocalDate.of(2000, 2, 2));

        when(userService.generateRandomPassword()).thenReturn("123456");
        when(encoder.encode("123456")).thenReturn("encrypted");
        when(userService.createUser(any())).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            u.setId(2L);
            u.setUsername("lgomez");
            return u;
        });

        when(traineeDAO.save(any())).thenReturn(new Trainee());

        CreateGymUserResponseDto response = traineeService.createUserTrainee(dto);

        assertNotNull(response);
        assertEquals("lgomez", response.getUsername());
        assertEquals("123456", response.getPassword());
    }

    @Test
    void testToggleTraineeStatus_success() {
        User user = new User("Carlos", "Pérez", true);
        user.setIsActive(true);

        when(userService.findByUsername("carlos")).thenReturn(Optional.of(user));

        traineeService.toggleTraineeStatus("carlos", false);

        verify(userService).save(user);
        assertFalse(user.getIsActive());
    }

    @Test
    void testDeleteTraineeByUsername_success() {
        User user = new User("Juan", "Díaz", true);
        user.setId(5L);
        Trainee trainee = new Trainee(1L, 5L, LocalDate.of(1990, 1, 1), "Calle Sol");

        when(userService.getByUsername("juan")).thenReturn(user);
        when(traineeDAO.findByUserId(5L)).thenReturn(trainee);

        traineeService.deleteTraineeByUsername("juan");

        verify(traineeDAO).delete(trainee);
    }
}
