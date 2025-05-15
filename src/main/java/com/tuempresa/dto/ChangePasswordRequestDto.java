package com.tuempresa.dto;

import lombok.Data;

@Data
public class ChangePasswordRequestDto {
    private String username;
    private String currentPassword;
    private String newPassword;
}

