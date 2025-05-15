package com.tuempresa.controller;

import com.tuempresa.dto.ChangePasswordRequestDto;
import com.tuempresa.service.UserService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class GymUserController {

    private final UserService userService;

    public GymUserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/change-password")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordRequestDto request) {

        boolean success = userService.changePassword(
            request.getUsername(),
            request.getCurrentPassword(),
            request.getNewPassword()
        );

        return success 
            ? ResponseEntity.ok().build() // 200
            : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401
    }
}