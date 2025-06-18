package com.tuempresa.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuempresa.dto.LoginRequestDto;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;	
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JsonAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public JsonAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.setAuthenticationManager(authenticationManager);
        this.setRequiresAuthenticationRequestMatcher(
            new AntPathRequestMatcher("/login", "POST")  
        );
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, 
                                             HttpServletResponse response) 
            throws AuthenticationException {
        try {
            LoginRequestDto loginRequest = objectMapper.readValue(
                request.getInputStream(), 
                LoginRequestDto.class
            );
            
            UsernamePasswordAuthenticationToken authRequest = 
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
                );
            
            return this.getAuthenticationManager().authenticate(authRequest);
            
        } catch (IOException e) {
            throw new AuthenticationServiceException("Error parsing login request", e);
        }
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult) throws IOException {
        
        SecurityContextHolder.getContext().setAuthentication(authResult);
        response.setContentType("application/json");
        response.getWriter().write("{\"message\":\"Login successful\"}");
    }

    @Override
    protected void unsuccessfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException failed) throws IOException {
        
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\":\"Authentication failed\"}");
    }
}