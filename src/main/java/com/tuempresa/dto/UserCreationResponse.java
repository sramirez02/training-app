package com.tuempresa.dto;

import com.tuempresa.entity.User;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserCreationResponse {
    private User user;
    private String plainPassword;
}