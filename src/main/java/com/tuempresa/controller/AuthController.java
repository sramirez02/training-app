//package com.tuempresa.controller;
//
//
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.tuempresa.dto.LoginRequestDto;
//
//@RestController
//public class AuthController {
//
//    private final AuthenticationManager authenticationManager;
//
//    public AuthController(AuthenticationManager authenticationManager) {
//        this.authenticationManager = authenticationManager;
//    }
//
//    @PostMapping("/login")
//    public String login(@RequestBody LoginRequestDto loginRequest) {
//        Authentication authentication = authenticationManager.authenticate(
//            new UsernamePasswordAuthenticationToken(
//                loginRequest.getUsername(),
//                loginRequest.getPassword()
//            )
//        );
//        
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        return "Login successful!";
//    }
//}