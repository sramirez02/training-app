package com.tuempresa.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tuempresa.dto.LoginRequestDto;
import com.tuempresa.service.UserService;

@RestController
public class LoginController {
	
	private final UserService userService;
	
	public LoginController(UserService userService) {
		this.userService = userService;
	}
	
	@PostMapping("/login")
    public ResponseEntity<Void> login(
            @RequestBody LoginRequestDto loginRequestDto) {

        
        if (loginRequestDto.getUsername() == null || loginRequestDto.getPassword() == null) {
        	return ResponseEntity.badRequest().build();
        }

        return userService.authenticate(loginRequestDto.getUsername(), loginRequestDto.getPassword()) 
                ? ResponseEntity.ok().build()
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	
	}
}
