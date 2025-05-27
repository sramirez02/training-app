package com.tuempresa.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.tuempresa.dto.LoginRequestDto;
import com.tuempresa.service.UserService;

@ExtendWith(MockitoExtension.class)
class LoginControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private LoginController loginController;

    private LoginRequestDto validLoginRequest;
    private LoginRequestDto invalidLoginRequest;
    private LoginRequestDto nullCredentialsRequest;

    @BeforeEach
    void setUp() {
        validLoginRequest = new LoginRequestDto();
        validLoginRequest.setUsername("validUser");
        validLoginRequest.setPassword("correctPassword");

        invalidLoginRequest = new LoginRequestDto();
        invalidLoginRequest.setUsername("invalidUser");
        invalidLoginRequest.setPassword("wrongPassword");

        nullCredentialsRequest = new LoginRequestDto();
       
    }

    @Test
    void login_ShouldReturnOk_WhenCredentialsAreValid() {
        
        when(userService.authenticate(validLoginRequest.getUsername(), validLoginRequest.getPassword()))
            .thenReturn(true);

        
        ResponseEntity<Void> response = loginController.login(validLoginRequest);

        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService).authenticate(validLoginRequest.getUsername(), validLoginRequest.getPassword());
    }

    @Test
    void login_ShouldReturnUnauthorized_WhenCredentialsAreInvalid() {
        
        when(userService.authenticate(invalidLoginRequest.getUsername(), invalidLoginRequest.getPassword()))
            .thenReturn(false);

        
        ResponseEntity<Void> response = loginController.login(invalidLoginRequest);

       
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(userService).authenticate(invalidLoginRequest.getUsername(), invalidLoginRequest.getPassword());
    }

    @Test
    void login_ShouldReturnBadRequest_WhenUsernameIsNull() {
        
        LoginRequestDto request = new LoginRequestDto();
        request.setUsername(null);
        request.setPassword("somePassword");

        
        ResponseEntity<Void> response = loginController.login(request);

        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verifyNoInteractions(userService);
    }

    @Test
    void login_ShouldReturnBadRequest_WhenPasswordIsNull() {
        
        LoginRequestDto request = new LoginRequestDto();
        request.setUsername("someUser");
        request.setPassword(null);

        
        ResponseEntity<Void> response = loginController.login(request);

        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verifyNoInteractions(userService);
    }

    @Test
    void login_ShouldReturnBadRequest_WhenBothCredentialsAreNull() {
        
        ResponseEntity<Void> response = loginController.login(nullCredentialsRequest);

        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verifyNoInteractions(userService);
    }

    @Test
    void login_ShouldCallServiceWithCorrectParameters() {
        
        when(userService.authenticate(anyString(), anyString()))
            .thenReturn(true);

       
        loginController.login(validLoginRequest);

        
        verify(userService).authenticate(
            eq(validLoginRequest.getUsername()),
            eq(validLoginRequest.getPassword()));
    }

    @Test
    void login_ShouldNotCallService_WhenRequestIsInvalid() {
        
        loginController.login(nullCredentialsRequest);

        
        verifyNoInteractions(userService);
    }
}