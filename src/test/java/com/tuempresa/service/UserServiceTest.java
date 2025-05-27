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

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = new User();
        user1.setId(1L);
        user1.setUsername("user1");
        user1.setPassword("password1");
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setIsActive(true);

        user2 = new User();
        user2.setId(2L);
        user2.setUsername("user2");
        user2.setPassword("password2");
        user2.setFirstName("Jane");
        user2.setLastName("Smith");
        user2.setIsActive(true);
    }

    @Test
    void testGetUserById_Success() {
      
        when(userDAO.findById(1L)).thenReturn(Optional.of(user1));

    
        User result = userService.getUserById(1L);

 
        assertNotNull(result);
        assertEquals("user1", result.getUsername());
        verify(userDAO).findById(1L);
    }

    @Test
    void testGetUserById_NotFound() {
        
        when(userDAO.findById(99L)).thenReturn(Optional.empty());

      
        User result = userService.getUserById(99L);

        
        assertNull(result);
        verify(userDAO).findById(99L);
    }

    @Test
    void testGetByUsername_Success() {
        
        when(userDAO.findByUsername("user1")).thenReturn(Optional.of(user1));

        
        User result = userService.getByUsername("user1");

        
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(userDAO).findByUsername("user1");
    }

    @Test
    void testGetByUsername_NotFound() {
      
        when(userDAO.findByUsername("unknown")).thenReturn(Optional.empty());

        
        User result = userService.getByUsername("unknown");

        
        assertNull(result);
        verify(userDAO).findByUsername("unknown");
    }

    @Test
    void testCreateUser() {
      
        User newUser = new User();
        newUser.setUsername("newUser");
        newUser.setPassword("newPass");
        
        when(userDAO.save(newUser)).thenReturn(newUser);

        
        User result = userService.createUser(newUser);

        
        assertNotNull(result);
        assertEquals("newUser", result.getUsername());
        verify(userDAO).save(newUser);
    }

    @Test
    void testAuthenticate_Success() {
     
        when(userDAO.findByUsername("user1")).thenReturn(Optional.of(user1));

       
        boolean result = userService.authenticate("user1", "password1");

       
        assertTrue(result);
        verify(userDAO).findByUsername("user1");
    }

    @Test
    void testAuthenticate_WrongPassword() {
        
        when(userDAO.findByUsername("user1")).thenReturn(Optional.of(user1));

    
        boolean result = userService.authenticate("user1", "wrongpassword");

        
        assertFalse(result);
        verify(userDAO).findByUsername("user1");
    }

    @Test
    void testAuthenticate_UserNotFound() {
        
        when(userDAO.findByUsername("unknown")).thenReturn(Optional.empty());

       
        boolean result = userService.authenticate("unknown", "password");

        
        assertFalse(result);
        verify(userDAO).findByUsername("unknown");
    }

    @Test
    void testChangePassword_Success() {
        
        when(userDAO.findByUsername("user1")).thenReturn(Optional.of(user1));
        when(userDAO.save(user1)).thenReturn(user1);

        
        boolean result = userService.changePassword("user1", "password1", "newPassword");

        
        assertTrue(result);
        assertEquals("newPassword", user1.getPassword());
        verify(userDAO).findByUsername("user1");
        verify(userDAO).save(user1);
    }

    @Test
    void testChangePassword_WrongOldPassword() {
        
        when(userDAO.findByUsername("user1")).thenReturn(Optional.of(user1));

        
        boolean result = userService.changePassword("user1", "wrongpassword", "newPassword");

     
        assertFalse(result);
        assertEquals("password1", user1.getPassword()); // Password no cambió
        verify(userDAO).findByUsername("user1");
        verify(userDAO, never()).save(any());
    }

    @Test
    void testChangePassword_UserNotFound() {
       
        when(userDAO.findByUsername("unknown")).thenReturn(Optional.empty());

       
        boolean result = userService.changePassword("unknown", "password", "newPassword");

        
        assertFalse(result);
        verify(userDAO).findByUsername("unknown");
        verify(userDAO, never()).save(any());
    }

    @Test
    void testGetAllUsers() {
        
        when(userDAO.findAll()).thenReturn(Arrays.asList(user1, user2));

    
        List<User> result = userService.getAllUsers();

      
        assertEquals(2, result.size());
        verify(userDAO).findAll();
    }

    @Test
    void testUpdateUser() {
        
        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setUsername("updatedUser");
        
        
        userService.updateUser(updatedUser);

        
        verify(userDAO).save(updatedUser);
    }

    @Test
    void testFindByUsername() {
      
        when(userDAO.findByUsername("user1")).thenReturn(Optional.of(user1));

      
        Optional<User> result = userService.findByUsername("user1");

        
        assertTrue(result.isPresent());
        assertEquals("user1", result.get().getUsername());
        verify(userDAO).findByUsername("user1");
    }

    @Test
    void testSave() {
        
        User newUser = new User();
        newUser.setUsername("newUser");

        
        userService.save(newUser);

      
        verify(userDAO).save(newUser);
    }
}