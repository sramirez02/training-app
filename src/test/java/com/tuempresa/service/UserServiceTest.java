package com.tuempresa.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.tuempresa.dao.UserDAO;
import com.tuempresa.entity.Trainee;
import com.tuempresa.entity.Trainer;
import com.tuempresa.entity.User;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserDAO userDAO;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("test.user");
        testUser.setPassword("encodedPassword");
        testUser.setFirstName("Test");
        testUser.setLastName("User");
        testUser.setIsActive(true);
    }

    @Test
    void getUserById_ShouldReturnUser_WhenExists() {
        when(userDAO.findById(1L)).thenReturn(Optional.of(testUser));

        User result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals("test.user", result.getUsername());
    }

    @Test
    void getUserById_ShouldReturnNull_WhenNotExists() {
        when(userDAO.findById(99L)).thenReturn(Optional.empty());

        User result = userService.getUserById(99L);

        assertNull(result);
    }

    @Test
    void getByUsername_ShouldReturnUser_WhenExists() {
        when(userDAO.findByUsername("test.user")).thenReturn(Optional.of(testUser));

        User result = userService.getByUsername("test.user");

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getByUsername_ShouldReturnNull_WhenNotExists() {
        when(userDAO.findByUsername("unknown")).thenReturn(Optional.empty());

        User result = userService.getByUsername("unknown");

        assertNull(result);
    }

    @Test
    void createUser_ShouldSaveAndReturnUser() {
        User newUser = new User("New", "User", true);
        when(userDAO.save(any(User.class))).thenReturn(testUser);

        User result = userService.createUser(newUser);

        assertNotNull(result);
        assertEquals("test.user", result.getUsername());
        verify(userDAO).save(newUser);
    }

    @Test
    void generateRandomPassword_ShouldReturnValidPassword() {
        String password = userService.generateRandomPassword();

        assertNotNull(password);
        assertEquals(10, password.length());
        assertTrue(password.matches("[A-Za-z0-9]+"));
    }

    @Test
    void authenticate_ShouldReturnTrue_WhenCredentialsMatch() {
        when(userDAO.findByUsername("test.user")).thenReturn(Optional.of(testUser));

        boolean result = userService.authenticate("test.user", "encodedPassword");

        assertTrue(result);
    }

    @Test
    void authenticate_ShouldReturnFalse_WhenPasswordNotMatch() {
        when(userDAO.findByUsername("test.user")).thenReturn(Optional.of(testUser));

        boolean result = userService.authenticate("test.user", "wrongPassword");

        assertFalse(result);
    }

    @Test
    void authenticate_ShouldReturnFalse_WhenUserNotFound() {
        when(userDAO.findByUsername("unknown")).thenReturn(Optional.empty());

        boolean result = userService.authenticate("unknown", "password");

        assertFalse(result);
    }

    @Test
    void changePassword_ShouldReturnTrue_WhenSuccessful() {
        User userWithOldPassword = new User();
        userWithOldPassword.setPassword("oldPassword");
        
        when(userDAO.findByUsername("test.user")).thenReturn(Optional.of(userWithOldPassword));
        when(userDAO.save(any(User.class))).thenReturn(testUser);

        boolean result = userService.changePassword("test.user", "oldPassword", "newPassword");

        assertTrue(result);
        assertEquals("newPassword", userWithOldPassword.getPassword());
    }

    @Test
    void changePassword_ShouldReturnFalse_WhenOldPasswordWrong() {
        when(userDAO.findByUsername("test.user")).thenReturn(Optional.of(testUser));

        boolean result = userService.changePassword("test.user", "wrongPassword", "newPassword");

        assertFalse(result);
        assertEquals("encodedPassword", testUser.getPassword());
    }

    @Test
    void changePassword_ShouldReturnFalse_WhenUserNotFound() {
        when(userDAO.findByUsername("unknown")).thenReturn(Optional.empty());

        boolean result = userService.changePassword("unknown", "oldPassword", "newPassword");

        assertFalse(result);
    }

    @Test
    void getAllUsers_ShouldReturnUserList() {
        when(userDAO.findAll()).thenReturn(Arrays.asList(testUser));

        List<User> users = userService.getAllUsers();

        assertEquals(1, users.size());
        assertEquals("test.user", users.get(0).getUsername());
    }

    @Test
    void updateUser_ShouldCallSave() {
        doNothing().when(userDAO).save(testUser);

        userService.updateUser(testUser);

        verify(userDAO).save(testUser);
    }

    @Test
    void findByUsername_ShouldReturnOptionalUser() {
        when(userDAO.findByUsername("test.user")).thenReturn(Optional.of(testUser));

        Optional<User> result = userService.findByUsername("test.user");

        assertTrue(result.isPresent());
        assertEquals("test.user", result.get().getUsername());
    }

    @Test
    void save_ShouldCallDAOSave() {
        doNothing().when(userDAO).save(testUser);

        userService.save(testUser);

        verify(userDAO).save(testUser);
    }

    @Test
    void createUserWithTrainee_ShouldSetTraineeReference() {
        User user = new User("Test", "Trainee", true);
        Trainee trainee = new Trainee();
        user.setTrainee(trainee);
        
        when(userDAO.save(any(User.class))).thenReturn(user);

        User result = userService.createUser(user);

        assertNotNull(result.getTrainee());
        verify(userDAO).save(user);
    }

    @Test
    void createUserWithTrainer_ShouldSetTrainerReference() {
        User user = new User("Test", "Trainer", true);
        Trainer trainer = new Trainer();
        user.setTrainer(trainer);
        
        when(userDAO.save(any(User.class))).thenReturn(user);

        User result = userService.createUser(user);

        assertNotNull(result.getTrainer());
        verify(userDAO).save(user);
    }
}