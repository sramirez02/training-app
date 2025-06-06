package com.tuempresa.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.tuempresa.dao.TraineeDAO;
import com.tuempresa.dao.TraineeTrainerDAO;
import com.tuempresa.dao.TrainerDAO;
import com.tuempresa.dao.TrainingTypeDAO;
import com.tuempresa.dto.CreateGymUserResponseDto;
import com.tuempresa.dto.CreateTraineeRequestDto;
import com.tuempresa.entity.Trainee;
import com.tuempresa.entity.User;

public class TraineeServiceTest {

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

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetByUsername() {
        User user = new User();
        user.setId(1L);
        when(userService.getByUsername("user1")).thenReturn(user);

        Trainee trainee = new Trainee(1L, 1L, LocalDate.now(), "address");
        when(traineeDAO.findByUserId(1L)).thenReturn(trainee);

        Trainee result = traineeService.getByUsername("user1");
        assertEquals(trainee, result);
    }

    @Test
    public void testCreateUserTrainee() {
        User user = new User();
        user.setId(10L);
        when(userService.createUser(any())).thenReturn(user);

        when(traineeDAO.save(any())).thenAnswer(i -> i.getArgument(0));

        Trainee result = traineeService.createUserTrainee(user, "2000-01-01", "Some Address");
        assertEquals(user.getId(), result.getUserId());
        assertEquals("Some Address", result.getAddress());
        assertEquals(LocalDate.of(2000, 1, 1), result.getDateOfBirth());
    }

    @Test
    public void testGetTraineeUserByUsernameUser() {
        User user = new User();
        user.setId(2L);
        when(userService.getByUsername("user2")).thenReturn(user);

        Trainee trainee = new Trainee(2L, 2L, LocalDate.of(1990, 5, 15), "Address2");
        when(traineeDAO.findByUserId(2L)).thenReturn(trainee);

        User result = traineeService.getTraineeUserByUsernameUser("user2");
        assertEquals(trainee, result.getTrainee());
    }

    @Test
    public void testCreateUserTraineeWithDTO() {
        CreateTraineeRequestDto dto = new CreateTraineeRequestDto();
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setDateOfBirth(LocalDate.of(1995, 4, 10));
        dto.setAddress("Street 123");

        User user = new User("John", "Doe", true);
        user.setUsername("john.doe");
        user.setPassword("1234");
        user.setId(5L);

        when(userService.createUser(any())).thenReturn(user);
        when(traineeDAO.save(any())).thenAnswer(i -> i.getArgument(0));

        CreateGymUserResponseDto response = traineeService.createUserTrainee(dto);
        assertEquals("john.doe", response.getUsername());
        assertEquals("1234", response.getPassword());
    }

    @Test
    public void testToggleTraineeStatus() {
        User user = new User();
        user.setIsActive(true);
        when(userService.findByUsername("userX")).thenReturn(Optional.of(user));

        traineeService.toggleTraineeStatus("userX", false);

        verify(userService).save(user);
        assertFalse(user.getIsActive());
    }

    @Test
    public void testGetTraineeIdByUsername() {
        User user = new User();
        user.setId(7L);
        when(userService.getByUsername("trainee7")).thenReturn(user);

        Trainee trainee = new Trainee(77L, 7L, null, null);
        when(traineeDAO.findByUserId(7L)).thenReturn(trainee);

        Long id = traineeService.getTraineeIdByUsername("trainee7");
        assertEquals(77L, id);
    }

    @Test
    public void testGetAllTrainees() {
        Trainee t1 = new Trainee(1L, 1L, null, "address1");
        Trainee t2 = new Trainee(2L, 2L, null, "address2");

        when(traineeDAO.findAll()).thenReturn(Arrays.asList(t1, t2));

        List<Trainee> list = traineeService.getAllTrainees();
        assertEquals(2, list.size());
        assertTrue(list.contains(t1));
        assertTrue(list.contains(t2));
    }

    @Test
    public void testDeleteTraineeByUsername() {
        User user = new User();
        user.setId(10L);
        when(userService.getByUsername("deleteMe")).thenReturn(user);

        Trainee trainee = new Trainee(20L, 10L, null, null);
        when(traineeDAO.findByUserId(10L)).thenReturn(trainee);

        traineeService.deleteTraineeByUsername("deleteMe");
        verify(traineeDAO).delete(trainee);
    }
}
