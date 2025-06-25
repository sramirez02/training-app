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

import com.tuempresa.dto.ChangePasswordRequestDto;
import com.tuempresa.service.UserService;

@ExtendWith(MockitoExtension.class)
class GymUserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private GymUserController gymUserController;

    private ChangePasswordRequestDto validRequest;
    private ChangePasswordRequestDto invalidRequest;

    @BeforeEach
    void setUp() {
        validRequest = new ChangePasswordRequestDto();
        validRequest.setUsername("test.user");
        validRequest.setCurrentPassword("oldPassword");
        validRequest.setNewPassword("newPassword");

        invalidRequest = new ChangePasswordRequestDto();
        invalidRequest.setUsername("unknown");
        invalidRequest.setCurrentPassword("wrongPassword");
        invalidRequest.setNewPassword("newPassword");
    }

    @Test
    void changePassword_ShouldReturnOk_WhenPasswordChangedSuccessfully() {
        when(userService.changePassword(
            validRequest.getUsername(),
            validRequest.getCurrentPassword(),
            validRequest.getNewPassword()
        )).thenReturn(true);

        ResponseEntity<Void> response = gymUserController.changePassword(validRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService).changePassword(
            validRequest.getUsername(),
            validRequest.getCurrentPassword(),
            validRequest.getNewPassword()
        );
    }

    @Test
    void changePassword_ShouldReturnUnauthorized_WhenCredentialsAreInvalid() {
        when(userService.changePassword(
            invalidRequest.getUsername(),
            invalidRequest.getCurrentPassword(),
            invalidRequest.getNewPassword()
        )).thenReturn(false);

        ResponseEntity<Void> response = gymUserController.changePassword(invalidRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(userService).changePassword(
            invalidRequest.getUsername(),
            invalidRequest.getCurrentPassword(),
            invalidRequest.getNewPassword()
        );
    }

    @Test
    void changePassword_ShouldReturnUnauthorized_WhenUserNotFound() {
        when(userService.changePassword(
            "nonexistent",
            "anyPassword",
            "newPassword"
        )).thenReturn(false);

        ChangePasswordRequestDto request = new ChangePasswordRequestDto();
        request.setUsername("nonexistent");
        request.setCurrentPassword("anyPassword");
        request.setNewPassword("newPassword");

        ResponseEntity<Void> response = gymUserController.changePassword(request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void changePassword_ShouldReturnUnauthorized_WhenCurrentPasswordIsWrong() {
        when(userService.changePassword(
            validRequest.getUsername(),
            "wrongPassword",
            validRequest.getNewPassword()
        )).thenReturn(false);

        ChangePasswordRequestDto request = new ChangePasswordRequestDto();
        request.setUsername(validRequest.getUsername());
        request.setCurrentPassword("wrongPassword");
        request.setNewPassword(validRequest.getNewPassword());

        ResponseEntity<Void> response = gymUserController.changePassword(request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void changePassword_ShouldCallServiceWithCorrectParameters() {
        when(userService.changePassword(anyString(), anyString(), anyString())).thenReturn(true);

        gymUserController.changePassword(validRequest);

        verify(userService).changePassword(
            validRequest.getUsername(),
            validRequest.getCurrentPassword(),
            validRequest.getNewPassword()
        );
    }
}