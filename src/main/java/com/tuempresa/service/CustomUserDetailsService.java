package com.tuempresa.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tuempresa.dao.UserDAO;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserDAO userDAO;

    public CustomUserDetailsService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.tuempresa.entity.User user = userDAO.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        if (!user.getIsActive()) {
            throw new UsernameNotFoundException("User is not active");
        }

        return User.builder()
            .username(user.getUsername())
            .password(user.getPassword())
            .roles("USER") 
            .accountLocked(!user.getIsActive()) 
            .build();
    }
}