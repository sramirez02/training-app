package com.tuempresa.service;

import com.tuempresa.dao.UserDAO;
import com.tuempresa.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserDAO userDAO;


    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        user.setUsername("john.doe");
        user.setPassword("1234");
        user.setFirstName("John");
        user.setLastName("Doe");
    }

    @Test
    void testGetUserById() {
        when(userDAO.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.getUserById(1L);
        assertEquals(user, result);
    }

    @Test
    void testGetUserById_NotFound() {
        when(userDAO.findById(1L)).thenReturn(Optional.empty());

        User result = userService.getUserById(1L);
        assertNull(result);
    }

    @Test
    void testGetByUsername() {
        when(userDAO.findByUsername("john.doe")).thenReturn(Optional.of(user));

        User result = userService.getByUsername("john.doe");
        assertEquals(user, result);
    }

    @Test
    void testGetByUsername_NotFound() {
        when(userDAO.findByUsername("john.doe")).thenReturn(Optional.empty());

        User result = userService.getByUsername("john.doe");
        assertNull(result);
    }

    @Test
    void testCreateUser() {
        when(userDAO.save(user)).thenReturn(user);

        User result = userService.createUser(user);
        assertEquals(user, result);
        verify(userDAO).save(user);
    }

    @Test
    void testAuthenticate_Success() {
        List<User> users = Arrays.asList(user);
        when(userDAO.findAll()).thenReturn(users);

        Optional<User> result = userService.authenticate("john.doe", "1234");
        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    void testAuthenticate_Fail() {
        List<User> users = Arrays.asList(user);
        when(userDAO.findAll()).thenReturn(users);

        Optional<User> result = userService.authenticate("john.doe", "wrongpass");
        assertFalse(result.isPresent());
    }

    @Test
    void testGetAllUsers() {
        List<User> users = Arrays.asList(user);
        when(userDAO.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();
        assertEquals(1, result.size());
        assertEquals(user, result.get(0));
    }

    @Test
    void testUpdateUser() {
        userService.updateUser(user);
        verify(userDAO).save(user);
    }

    @Test
    void testFindByUsername() {
        when(userDAO.findByUsername("john.doe")).thenReturn(Optional.of(user));

        Optional<User> result = userService.findByUsername("john.doe");
        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    void testSave() {
        userService.save(user);
        verify(userDAO).save(user);
    }
}
