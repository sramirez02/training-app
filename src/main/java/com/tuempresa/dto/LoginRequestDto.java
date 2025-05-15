package com.tuempresa.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class LoginRequestDto {
	private String username;
	private String password;
}
