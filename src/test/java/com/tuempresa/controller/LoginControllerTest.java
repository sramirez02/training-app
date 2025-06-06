package com.tuempresa.controller;

import com.tuempresa.dto.LoginRequestDto;
import com.tuempresa.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.MockitoAnnotations;

public class LoginControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private LoginController loginController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void login_Success_ReturnsOk() {
        
        LoginRequestDto loginDto = new LoginRequestDto();
        loginDto.setUsername("usuario1");
        loginDto.setPassword("clave123");

        when(userService.authenticate("usuario1", "clave123")).thenReturn(true);

       
        ResponseEntity<Void> response = loginController.login(loginDto);

        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService).authenticate("usuario1", "clave123");
    }

    @Test
    void login_InvalidCredentials_ReturnsUnauthorized() {
        
        LoginRequestDto loginDto = new LoginRequestDto();
        loginDto.setUsername("usuario1");
        loginDto.setPassword("claveIncorrecta");

        when(userService.authenticate("usuario1", "claveIncorrecta")).thenReturn(false);

        
        ResponseEntity<Void> response = loginController.login(loginDto);

        
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(userService).authenticate("usuario1", "claveIncorrecta");
    }

    @Test
    void login_NullUsernameOrPassword_ReturnsBadRequest() {
        
        LoginRequestDto loginDto = new LoginRequestDto();
        loginDto.setUsername(null);
        loginDto.setPassword("clave123");

        
        ResponseEntity<Void> response = loginController.login(loginDto);

        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(userService, never()).authenticate(any(), any());
    }

    @Test
    void login_NullPassword_ReturnsBadRequest() {
        
        LoginRequestDto loginDto = new LoginRequestDto();
        loginDto.setUsername("usuario1");
        loginDto.setPassword(null);

        
        ResponseEntity<Void> response = loginController.login(loginDto);

        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(userService, never()).authenticate(any(), any());
    }
}
