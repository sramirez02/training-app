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

import com.tuempresa.dao.UserDAO;
import com.tuempresa.entity.User;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserDAO userDAO;
    
    @InjectMocks
    private UserService userService;
    
    private User testUser;
    private User newUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setUsername("john.doe");
        testUser.setPassword("secure123");
        testUser.setIsActive(true);
        
        newUser = new User("Jane", "Smith", true);
    }

    @Test
    void getUserById_ShouldReturnUser_WhenExists() {
        when(userDAO.findById(1L)).thenReturn(Optional.of(testUser));
        
        User result = userService.getUserById(1L);
        
        assertNotNull(result);
        assertEquals("john.doe", result.getUsername());
        verify(userDAO).findById(1L);
    }

    @Test
    void getUserById_ShouldReturnNull_WhenNotExists() {
        when(userDAO.findById(99L)).thenReturn(Optional.empty());
        
        User result = userService.getUserById(99L);
        
        assertNull(result);
        verify(userDAO).findById(99L);
    }

    @Test
    void getByUsername_ShouldReturnUser_WhenExists() {
        when(userDAO.findByUsername("john.doe")).thenReturn(Optional.of(testUser));
        
        User result = userService.getByUsername("john.doe");
        
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(userDAO).findByUsername("john.doe");
    }

    @Test
    void getByUsername_ShouldReturnNull_WhenNotExists() {
        when(userDAO.findByUsername("unknown")).thenReturn(Optional.empty());
        
        User result = userService.getByUsername("unknown");
        
        assertNull(result);
        verify(userDAO).findByUsername("unknown");
    }

    @Test
    void createUser_ShouldGenerateUsernameAndPassword() {
        when(userDAO.save(any(User.class))).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            u.setId(2L);
            return u;
        });
        
        User result = userService.createUser(newUser);
        
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("jane.smith", result.getUsername());
        assertNotNull(result.getPassword());
        assertEquals(10, result.getPassword().length());
        verify(userDAO).save(newUser);
    }

    @Test
    void authenticate_ShouldReturnTrue_WhenCredentialsMatch() {
        when(userDAO.findByUsername("john.doe")).thenReturn(Optional.of(testUser));
        
        boolean result = userService.authenticate("john.doe", "secure123");
        
        assertTrue(result);
        verify(userDAO).findByUsername("john.doe");
    }

    @Test
    void authenticate_ShouldReturnFalse_WhenUserNotFound() {
        when(userDAO.findByUsername("unknown")).thenReturn(Optional.empty());
        
        boolean result = userService.authenticate("unknown", "anypassword");
        
        assertFalse(result);
        verify(userDAO).findByUsername("unknown");
    }

    @Test
    void authenticate_ShouldReturnFalse_WhenPasswordWrong() {
        when(userDAO.findByUsername("john.doe")).thenReturn(Optional.of(testUser));
        
        boolean result = userService.authenticate("john.doe", "wrongpassword");
        
        assertFalse(result);
        verify(userDAO).findByUsername("john.doe");
    }

    @Test
    void changePassword_ShouldUpdate_WhenOldPasswordCorrect() {
        when(userDAO.findByUsername("john.doe")).thenReturn(Optional.of(testUser));
        when(userDAO.save(any(User.class))).thenReturn(testUser);
        
        boolean result = userService.changePassword("john.doe", "secure123", "newpassword");
        
        assertTrue(result);
        assertEquals("newpassword", testUser.getPassword());
        verify(userDAO).save(testUser);
    }

    @Test
    void changePassword_ShouldReturnFalse_WhenUserNotFound() {
        when(userDAO.findByUsername("unknown")).thenReturn(Optional.empty());
        
        boolean result = userService.changePassword("unknown", "old", "new");
        
        assertFalse(result);
        verify(userDAO, never()).save(any());
    }

    @Test
    void changePassword_ShouldReturnFalse_WhenOldPasswordWrong() {
        when(userDAO.findByUsername("john.doe")).thenReturn(Optional.of(testUser));
        
        boolean result = userService.changePassword("john.doe", "wrong", "new");
        
        assertFalse(result);
        verify(userDAO, never()).save(any());
    }

    @Test
    void getAllUsers_ShouldReturnAllUsers() {
        when(userDAO.findAll()).thenReturn(Arrays.asList(testUser, newUser));
        
        List<User> result = userService.getAllUsers();
        
        assertEquals(2, result.size());
        verify(userDAO).findAll();
    }

    @Test
    void updateUser_ShouldCallSave() {
        userService.updateUser(testUser);
        verify(userDAO).save(testUser);
    }

    @Test
    void findByUsername_ShouldReturnOptionalUser() {
        when(userDAO.findByUsername("john.doe")).thenReturn(Optional.of(testUser));
        
        Optional<User> result = userService.findByUsername("john.doe");
        
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(userDAO).findByUsername("john.doe");
    }

    @Test
    void save_ShouldCallDAOSave() {
        userService.save(testUser);
        verify(userDAO).save(testUser);
    }

    @Test
    void createUser_ShouldSetActiveStatus() {
        User inactiveUser = new User("Inactive", "User", false);
        when(userDAO.save(any(User.class))).thenReturn(inactiveUser);
        
        User result = userService.createUser(inactiveUser);
        
        assertFalse(result.getIsActive());
        verify(userDAO).save(inactiveUser);
    }
}