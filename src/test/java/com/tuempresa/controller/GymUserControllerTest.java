package com.tuempresa.controller;

import com.tuempresa.dto.ChangePasswordRequestDto;
import com.tuempresa.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.MockitoAnnotations;

public class GymUserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private GymUserController gymUserController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void changePassword_Success_ReturnsOk() {
        
        ChangePasswordRequestDto request = new ChangePasswordRequestDto();
        request.setUsername("usuario1");
        request.setCurrentPassword("passwordActual");
        request.setNewPassword("nuevoPassword");

        when(userService.changePassword("usuario1", "passwordActual", "nuevoPassword"))
                .thenReturn(true);

       
        ResponseEntity<Void> response = gymUserController.changePassword(request);

        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService).changePassword("usuario1", "passwordActual", "nuevoPassword");
    }

    @Test
    void changePassword_Failure_ReturnsUnauthorized() {
        
        ChangePasswordRequestDto request = new ChangePasswordRequestDto();
        request.setUsername("usuario1");
        request.setCurrentPassword("passwordIncorrecto");
        request.setNewPassword("nuevoPassword");

        when(userService.changePassword("usuario1", "passwordIncorrecto", "nuevoPassword"))
                .thenReturn(false);

        
        ResponseEntity<Void> response = gymUserController.changePassword(request);

        
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(userService).changePassword("usuario1", "passwordIncorrecto", "nuevoPassword");
    }
}
